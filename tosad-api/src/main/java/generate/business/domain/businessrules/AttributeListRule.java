package generate.business.domain.businessrules;

import generate.business.domain.businessrules.ruleattributes.LiteralValue;
import generate.business.domain.businessrules.ruleattributes.Operator;
import generate.business.domain.businessrules.ruleattributes.Table;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;

public class AttributeListRule implements BusinessRule {
    private Operator operator;
    private ArrayList<LiteralValue> values = new ArrayList<>();
    private Table table;
    private String failuremessage;
    private String name;
    private String constraintTemplate;
    private String declareTemplate;

    public AttributeListRule(Operator operator,
                             Table table, String failuremessage, String name, String constraintTemplate, String declareTemplate) {
        this.operator = operator;
        this.table = table;
        this.failuremessage = failuremessage;
        this.name = name;
        this.constraintTemplate = constraintTemplate;
        this.declareTemplate = declareTemplate;
    }

    public void addValue(LiteralValue value) {
        values.add(value);
    }

    public String getName() {
        return name;
    }

    public Table getTable () {
        return table;
    }

    public String generateDynamicPart() {
        String template = "--" + name + "\n";
        constraintTemplate = constraintTemplate.replace("[selectedTableAttributeName]", table.getSelectedTableAttribute().getName());
        constraintTemplate = constraintTemplate.replace("[operator]", operator.getName());
        constraintTemplate = constraintTemplate.replace("[generateList]", generateList());
        constraintTemplate = constraintTemplate.replace("[failuremessage]", failuremessage);
        template += constraintTemplate + "\n\n";
        return template;
    }

    public String generateList() {
        String value = "";
        boolean first = true;
        if (!values.isEmpty()) {
            for (LiteralValue v : values) {
                if(NumberUtils.isNumber(v.getValue())) {
                    if (first) {
                        value += "" + v.getValue();
                        first = false;
                    } else {
                        value += ", " + v.getValue();
                    }
                } else {
                    if (first) {
                        value += "'" + v.getValue() + "'";
                        first = false;
                    } else {
                        value += ", '" + v.getValue() + "'";
                    }
                }
            }
        }
        return value;
    }

    public String generateDeclare() {
        return "";
    }
}