package generate.business.domain.businessrules.ruleattributes;

import generate.business.domain.businessrules.BusinessRule;

public interface BusinessRuleExtraControllerInterface {
	public Operator createOperator(String operatorName);
	public Table createTable(String tableName, String attributeName);
	public BusinessRule addValuesToRule(BusinessRule rule);
	

}
