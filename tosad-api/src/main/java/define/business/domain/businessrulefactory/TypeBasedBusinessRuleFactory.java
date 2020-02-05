package define.business.domain.businessrulefactory;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import define.business.domain.LiteralValue;
import define.business.domain.Operator;
import define.business.domain.Table;
import define.business.domain.TableAttribute;
import define.business.domain.Trigger;
import define.business.domain.businessrules.AttributeCompareRule;
import define.business.domain.businessrules.AttributeListRule;
import define.business.domain.businessrules.AttributeOtherRule;
import define.business.domain.businessrules.BusinessRule;
import define.business.domain.businessrules.EntityOtherRule;
import define.business.domain.businessrules.InterEntityCompareRule;
import define.business.domain.businessrules.ModifyRule;
import define.business.domain.businessrules.RangeRule;
import define.business.domain.businessrules.TupleCompareRule;
import define.business.domain.businessrules.TupleOtherRule;

public class TypeBasedBusinessRuleFactory implements BusinessRuleFactory {
    String type;

    public TypeBasedBusinessRuleFactory(String type) {
        this.type = type;
    };

    @Override
    public BusinessRule createRule(JSONObject jsondata) {
    	JSONArray jsonArray = (JSONArray) jsondata.get("values");
    	JSONObject credentials = jsondata.getJSONObject("credentials");
    	ArrayList<LiteralValue> values = new ArrayList<>();
    	
		for (Object value : jsonArray) {
			LiteralValue lvalue = new LiteralValue(value.toString());
			values.add(lvalue);
		}

    	Operator operator = new Operator(jsondata.get("operator").toString());
    	String generatedname = credentials.getString("name").substring(0, 3) + credentials.getString("name").substring(credentials.getString("name").length() - 2);
    	String triggercode = "BRG_" + generatedname + "_" + jsondata.get("table").toString().substring(0, 2) + jsondata.get("table").toString().substring(jsondata.get("table").toString().length() - 1) + "_trigger";
    	String rulename = "BRG_" + generatedname + "_" + jsondata.get("table").toString().substring(0, 2) + jsondata.get("table").toString().substring(jsondata.get("table").toString().length() - 1) + "_CNS_";
    	Trigger trigger = new Trigger(triggercode, jsondata.get("triggerEvent").toString() , jsondata.get("failureMessage").toString());
    	Table table = new Table(jsondata.get("table").toString(), new TableAttribute(jsondata.get("attribute").toString()));	
    	
    	if (type.equals("Attribute Range rule")) {
    		return new RangeRule(rulename, operator, trigger, table, values, type);	
        } else if (type.equals("Attribute Compare rule")) {
        	return new AttributeCompareRule(rulename, operator, trigger, table, values, type);
        } else if (type.equals("Attribute List rule")) {
        	return new AttributeListRule(rulename, operator, trigger, table, values, type);
        } else if (type.equals("Attribute Other rule")) {
        	return new AttributeOtherRule(rulename, operator, trigger, table, values, type);
        } else if (type.equals("Tuple Compare rule")) {
        	return new TupleCompareRule(rulename, operator, trigger, table, values, type);
        } else if (type.equals("Tuple Other rule")) {
        	return new TupleOtherRule(rulename, operator, trigger, table, values, type);
        } else if (type.equals("Inter-Entity Compare rule")) {
        	return new InterEntityCompareRule(rulename, operator, trigger, table, values, type);
        } else if (type.equals("Entity Other rule")) {
        	return new EntityOtherRule(rulename, operator, trigger, table, values, type);
        } else {
        	return new ModifyRule(rulename, operator, trigger, table, values, type);
        }
    }
}
