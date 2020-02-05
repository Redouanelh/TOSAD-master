package generate.business.domain.factory;

import generate.business.domain.businessrules.*;
import generate.business.domain.businessrules.ruleattributes.Operator;
import generate.business.domain.businessrules.ruleattributes.Table;

public class TypeBasedBusinessRuleFactory implements BusinessRuleFactory {
    String type;

    public TypeBasedBusinessRuleFactory(String type) {
        this.type = type;
    }

    @Override
    public BusinessRule createRule(Operator operator, Table table, String failuremessage, String name, String constraintTemplate, String declareTemplate) {
        if (type.equals("Attribute Range rule")) {
            return new AttributeRangeRule(operator, table, failuremessage, name, constraintTemplate, "");
        } else if (type.equals("Attribute Compare rule")) {
            return new AttributeCompareRule(operator, table, failuremessage, name, constraintTemplate, "");
        } else if (type.equals("Attribute List rule")) {
            return new AttributeListRule(operator, table, failuremessage, name, constraintTemplate, "");
        } else if (type.equals("Attribute Other rule")) {
            return new AttributeOtherRule(operator, table, failuremessage, name, constraintTemplate, "");
        } else if (type.equals("Tuple Compare rule")) {
            return new TupleCompareRule(operator, table, failuremessage, name, constraintTemplate, "");
        } else if (type.equals("Tuple Other rule")) {
            return new TupleOtherRule(operator, table, failuremessage, name, constraintTemplate, "");
        } else if (type.equals("Inter-Entity Compare rule")) {
            return new InterEntityCompareRule(operator, table, failuremessage, name, constraintTemplate, declareTemplate);
        } else if (type.equals("Entity Other rule")) {
            return new EntityOtherRule(operator, table, failuremessage, name, constraintTemplate, declareTemplate);
        } else {
            return new ModifyRule(operator, table, failuremessage, name, constraintTemplate, declareTemplate);
        }
    }
}
