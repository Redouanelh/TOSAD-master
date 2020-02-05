package generate.business.domain.factory;

import generate.business.domain.businessrules.BusinessRule;
import generate.business.domain.businessrules.ruleattributes.Operator;
import generate.business.domain.businessrules.ruleattributes.Table;

public interface BusinessRuleFactory {
    BusinessRule createRule(Operator operator, Table table, String failuremessage, String name, String constraintTemplate, String declareTemplate);
}
