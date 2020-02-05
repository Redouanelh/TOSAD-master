package generate.business.domain.businessrules;

import generate.business.domain.businessrules.ruleattributes.LiteralValue;
import generate.business.domain.businessrules.ruleattributes.Operator;
import generate.business.domain.businessrules.ruleattributes.Table;

import java.util.ArrayList;

public class InterEntityCompareRule implements BusinessRule {
    private Operator operator;
    private ArrayList<LiteralValue> values = new ArrayList<>();
    private Table table;
    private String failuremessage;
    private String name;
    private String constraintTemplate;
    private String declareTemplate;

    public InterEntityCompareRule(Operator operator, Table table, String failuremessage, String name, String constraintTemplate, String declareTemplate) {
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
        String template =  "--" + name + " declare\n";
        constraintTemplate = constraintTemplate.replace("[templateid]", name.split("_")[5]);
        constraintTemplate = constraintTemplate.replace("[targettable]", values.get(0).getValue());
        constraintTemplate = constraintTemplate.replace("[targettableattribute]", values.get(1).getValue());
        constraintTemplate = constraintTemplate.replace("[targetkey]", values.get(3).getValue());
        constraintTemplate = constraintTemplate.replace("[selectedkey]", values.get(2).getValue());
        constraintTemplate = constraintTemplate.replace("[selectedtableattribute]", table.getSelectedTableAttribute().getName());
        constraintTemplate = constraintTemplate.replace("[operator]", operator.getName());
        template += constraintTemplate + "\n";

//        String template = "--" + name + " code\n" +
//                "open lc_first;\n" +
//                "fetch lc_first into l_targetValue;\n" +
//                "close lc_first;\n" +
//                "l_passed := :new." + table.getSelectedTableAttribute().getName() + " " + operator.getName() + " l_targetValue;\n" +
//                "  if not l_passed\n" +
//                "  then\n" +
//                "    l_error_stack := '" + failuremessage + "';\n" +
//                "    raise_application_error( -20800, l_error_stack );\n" +
//                "  end if;\n";
        return template;
    }

    public String generateDeclare() {
        String template =  "--" + name + " declare\n";
        declareTemplate = declareTemplate.replace("[templateid]", name.split("_")[5]);
        declareTemplate = declareTemplate.replace("[targettable]", values.get(0).getValue());
        declareTemplate = declareTemplate.replace("[targettableattribute]", values.get(1).getValue());
        template += declareTemplate + "\n";
        System.out.println(template);
//        String template =  "--" + name + " declare\n" +
//                "cursor lc_first is\n" +
//                "select " + values.get(1).getValue() + "\n" +
//                "from " + values.get(0).getValue()  + "\n" +
//                "l_targetValue " + values.get(0).getValue() + "." + values.get(1).getValue() + "%type;\n";
        return template;
    }

}