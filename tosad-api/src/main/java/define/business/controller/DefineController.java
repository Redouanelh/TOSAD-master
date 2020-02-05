package define.business.controller;

import java.util.ArrayList;
import java.util.HashMap;

import define.persistence.targetdaofactory.TargetDaoFactory;
import define.persistence.dao.DefineDao;
import define.persistence.dao.OracleBaseDao;
import org.json.JSONObject;

import define.business.domain.businessrulefactory.BusinessRuleFactory;
import define.business.domain.businessrulefactory.TypeBasedBusinessRuleFactory;
import define.business.domain.businessrules.BusinessRule;
import define.persistence.targetdaofactory.TypeBasedTargetDaoFactory;
import define.persistence.dao.BaseDao;
import define.persistence.dao.DefineOracleDao;

public class DefineController {

	private DefineDao definedao;

    public DefineController() {
    	BaseDao defineconnection = new OracleBaseDao("jdbc:oracle:thin:@//ondora04.hu.nl:1521/EDUC11", "cursist", "cursist8101");
    	this.definedao = new DefineOracleDao(defineconnection);
	}

	public HashMap<String, HashMap> getDefineData(String data) {
        try {
			JSONObject jsondata = new JSONObject(data);

        	HashMap<String, HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>> ruledata = new HashMap<>();
        	ruledata = definedao.getAvailableInput();
        
        	HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> targetdata = new HashMap<>();
        	TargetDaoFactory targetdaofactory = new TypeBasedTargetDaoFactory(jsondata.getString("type"), "jdbc:oracle:thin:@//"+jsondata.getString("url"), jsondata.getString("username"), jsondata.getString("password"));
        	targetdata = targetdaofactory.getTargetDao().loadTargetDatabase();
        	
			HashMap<String, HashMap> totaldata= new HashMap();
        	totaldata.put("categories", ruledata.get("categories"));
        	totaldata.put("datatable", targetdata.get("datatable"));
        	
        	return totaldata;
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Failed to execute method.");
        return null;
    }
	
	public String saveDefineData(String data) {
		JSONObject jsondata = new JSONObject(data);
		String ruletype = jsondata.get("ruletype").toString();
				
		BusinessRuleFactory factory = new TypeBasedBusinessRuleFactory(ruletype);
		BusinessRule rule = factory.createRule(jsondata);
		
    	String ruledata = definedao.defineRule(rule);
    	
    	System.out.println(ruledata); // prints "failed" or "succes"
		
		return ruledata;
	}
	
	public String deleteBusinessRule(String data) {
		JSONObject jsondata = new JSONObject(data);
		String rulename = jsondata.get("name").toString();
		
		return definedao.deleteBusinessRule(rulename);
	}
	
	public HashMap<String, String> login(String data) {
		JSONObject jsondata = new JSONObject(data);
    	TargetDaoFactory targetdaofactory = new TypeBasedTargetDaoFactory(jsondata.getString("type"), "jdbc:oracle:thin:@//"+jsondata.getString("url"), jsondata.getString("username"), jsondata.getString("password"));
    	return targetdaofactory.getTargetDao().testConnection();
	}
	
	

}
