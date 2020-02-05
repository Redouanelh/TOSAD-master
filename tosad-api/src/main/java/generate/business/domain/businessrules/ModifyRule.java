package generate.business.domain.businessrules;

import generate.business.domain.businessrules.ruleattributes.LiteralValue;
import generate.business.domain.businessrules.ruleattributes.Operator;
import generate.business.domain.businessrules.ruleattributes.Table;

import java.util.ArrayList;

public class ModifyRule implements BusinessRule {
    private Operator operator;
    private ArrayList<LiteralValue> values = new ArrayList<>();
    private Table table;
    private String failuremessage;
    private String name;
    private String constraintTemplate;
    private String declareTemplate;

    public ModifyRule(Operator operator, Table table, String failuremessage, String name, String constraintTemplate, String declareTemplate) {
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
        String template = "--" + name + " constraint\n";
        constraintTemplate = constraintTemplate.replace("[value 1]", values.get(1).getValue());
        constraintTemplate = constraintTemplate.replace("[failuremessage]", failuremessage);
        template += constraintTemplate + "\n\n";
        return template;
    }

    public String generateDeclare() {
        String template = "--" + name + " declare \n";
        declareTemplate = declareTemplate.replace("[value 0]", values.get(0).getValue());
        template += declareTemplate + "\n";
        return template;
    }
}
