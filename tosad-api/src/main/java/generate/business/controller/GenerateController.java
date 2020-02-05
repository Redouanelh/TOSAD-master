package generate.business.controller;

import generate.persistence.targetdaofactory.TargetDaoFactory;
import generate.persistence.targetdaofactory.TypeBasedTargetDaoFactory;
import generate.persistence.dao.*;
import generate.business.domain.businessrules.BusinessRule;
import generate.business.domain.businessrules.BusinessRuleController;
import generate.business.domain.businessrules.BusinessRuleControllerInterface;
import generate.business.domain.businessrules.ruleattributes.*;
import org.json.JSONObject;

import java.util.ArrayList;

public class GenerateController {
    private DefineDao definedao;
    private TargetDao targetDao;

    public GenerateController() {
        BaseDao defineconnection = new OracleBaseDao("jdbc:oracle:thin:@//ondora04.hu.nl:1521/EDUC11", "cursist", "cursist8101");
        this.definedao = new DefineOracleDao(defineconnection);
    }

    public void setTargetDao(JSONObject data) {
        TargetDaoFactory targetdaofactory = new TypeBasedTargetDaoFactory(data.get("type").toString(), "jdbc:oracle:thin:@//"+data.get("url").toString(), data.get("username").toString(), data.get("password").toString());
        this.targetDao = targetdaofactory.getTargetDao();
    }


    public ArrayList<String> returnTriggers(String data) {
        JSONObject jsondata = new JSONObject(data);
        setTargetDao(jsondata);
        ArrayList<String> triggerData = definedao.getTriggerInfo(jsondata.getString("name"));

        return triggerData;
    }

    public ArrayList<String> returnRulesByTrigger(String data) {
        JSONObject jsondata = new JSONObject(data);
        Trigger trigger = new Trigger(jsondata.get("name").toString());
        ArrayList<String> triggerData = definedao.getRulesByTrigger(trigger.getTriggercode());

        return triggerData;
    }

    public ArrayList<String> generateTriggerCode(String data) {
        JSONObject jsondata = new JSONObject(data);
        JSONObject credentials = jsondata.getJSONObject("credentials");
        Trigger trigger = new Trigger(jsondata.get("name").toString());
        ArrayList<String> returnList = new ArrayList<>();
        System.out.println();

        BusinessRuleControllerInterface rulecomponentcontroller = new BusinessRuleController(definedao);
        trigger = rulecomponentcontroller.fillTriggerWithRules(definedao.getAllDataFromTrigger(trigger.getTriggercode(), credentials.getString("type")), trigger);

        if(trigger.getBusinessRules().size() > 0) {
            ArrayList<BusinessRule> ruleList = trigger.getBusinessRules();
            String tablename = "";
            String bRuleString = "";
            String bRuleDeclare = "";

            for (BusinessRule bRule : ruleList) {
                tablename = bRule.getTable().getName();
                bRuleString += bRule.generateDynamicPart();
                bRuleDeclare += bRule.generateDeclare();
            }

            String triggerString = "create or replace trigger " + trigger.getTriggercode() + "\n" +
                    trigger.getTriggerevent() + "\n" +
                    "  on " + tablename + "\n" +
                    "  for each row\n" +
                    "declare\n" +
                    "  l_passed boolean := true;\n" +
                    "  l_error_stack varchar2(4000);\n" +
                    bRuleDeclare +
                    " begin\n";

            triggerString += bRuleString;

            triggerString += "end " + trigger.getTriggercode() + ";";
            returnList.add(triggerString);
        }else {
        	returnList.add("No rules in trigger");
        }
        return returnList;
    }

    public ArrayList<String> generateTrigger(String data) {
        JSONObject jsondata = new JSONObject(data);
        JSONObject credentials = jsondata.getJSONObject("credentials");
        setTargetDao(credentials);
        ArrayList<String> triggercode = generateTriggerCode(data);
        ArrayList<String> triggerData = new ArrayList<String>();
        if(triggercode.get(0).equals("No rules in trigger")) {
        	triggerData = triggercode;
        } else {
            triggerData = targetDao.executeCode(triggercode.get(0));	
        }

        return triggerData;
    }

    public ArrayList<String> deleteOrUpdateTrigger(String data) {
        JSONObject jsondata = new JSONObject(data);
        JSONObject credentials = jsondata.getJSONObject("credentials");
        String triggerName = jsondata.getString("name");
        setTargetDao(credentials);
        ArrayList<String> returnData;
        returnData = targetDao.deleteOrUpdateTrigger(triggerName);
        ArrayList<String> triggerData = generateTrigger(data);
        for(String s : triggerData) {
            returnData.add(s);
        }

        return returnData;
    }


}
