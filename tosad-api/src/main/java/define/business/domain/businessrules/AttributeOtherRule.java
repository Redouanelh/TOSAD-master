package define.business.domain.businessrules;

import define.business.domain.*;

import java.util.ArrayList;

public class AttributeOtherRule implements BusinessRule {
	private String name;
    private Operator operator;
    private Trigger trigger;
    private ArrayList<LiteralValue> values;
    private Table table;
    private String type;

    public AttributeOtherRule(String name, Operator operator, Trigger trigger, Table table,
                              ArrayList<LiteralValue> values, String type) {
        this.operator = operator;
        this.trigger = trigger;
        this.values = values;
        this.table = table;
        this.name = name;
        this.type = type;
    }

	@Override
	public Operator getOperator() {
		return operator;
	}

	@Override
	public Trigger getTrigger() {
		return trigger;
	}

	@Override
	public Table getTable() {
		return table;
	}

	@Override
	public ArrayList<LiteralValue> getValues() {
		return values;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

}