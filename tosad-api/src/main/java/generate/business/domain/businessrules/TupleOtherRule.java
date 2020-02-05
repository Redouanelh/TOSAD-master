package generate.business.domain.businessrules;

import generate.business.domain.businessrules.ruleattributes.LiteralValue;
import generate.business.domain.businessrules.ruleattributes.Operator;
import generate.business.domain.businessrules.ruleattributes.Table;

import java.util.ArrayList;

public class TupleOtherRule implements BusinessRule {
    private Operator operator;
    private ArrayList<LiteralValue> values = new ArrayList<>();
    private Table table;
    private String failuremessage;
    private String name;
    private String constraintTemplate;
    private String declareTemplate;

    public TupleOtherRule(Operator operator, Table table, String failuremessage, String name, String constraintTemplate, String declareTemplate) {
        this.operator = operator;
        this.table = table;
        this.failuremessage = failuremessage;
        this.name = name;
        this.constraintTemplate = constraintTemplate;
        this.declareTemplate = declareTemplate;
    }

    public String getName() {
        return name;
    }

    public Table getTable () {
        return table;
    }

    public void addValue(LiteralValue value) {
        values.add(value);
    }

    public String generateDynamicPart() {
        String template = "--" + name + "\n";
        constraintTemplate = constraintTemplate.replace("[value]", values.get(0).getValue());
        constraintTemplate = constraintTemplate.replace("[failuremessage]", failuremessage);
        template += constraintTemplate + "\n\n";
        return template;
    }

    public String generateDeclare() {
        return "";
    }

}