package generate.business.domain.businessrules.ruleattributes;

import generate.business.domain.businessrules.BusinessRule;
import generate.persistence.dao.DefineDao;
import java.util.ArrayList;

public class BusinessRuleExtraController implements BusinessRuleExtraControllerInterface {
    private DefineDao defineDao;

    public BusinessRuleExtraController(DefineDao defineDao) {
        this.defineDao = defineDao;
    }

    public Operator createOperator(String operatorName) {
        return new Operator(operatorName);
    }

    public Table createTable(String tableName, String attributeName) {
        TableAttribute attribute = new TableAttribute(attributeName);
        return new Table(tableName, attribute);
    }

    public BusinessRule addValuesToRule(BusinessRule rule) {
        ArrayList<String> values = defineDao.getValuesFromRule(rule.getName());

        for (String value : values) {
            LiteralValue litValue = new LiteralValue(value);
            rule.addValue(litValue);
        }
        return rule;
    }
}
