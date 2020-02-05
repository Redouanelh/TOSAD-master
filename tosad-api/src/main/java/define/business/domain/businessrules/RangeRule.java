package define.business.domain.businessrules;

import java.util.ArrayList;

import define.business.domain.LiteralValue;
import define.business.domain.Operator;
import define.business.domain.Table;
import define.business.domain.Trigger;

public class RangeRule implements BusinessRule {
	private String name;
    private Operator operator;
    private Trigger trigger;
    private Table table;
    private ArrayList<LiteralValue> values = new ArrayList<>();
    private String type;

    public RangeRule(String name, Operator operator, Trigger trigger, Table table, ArrayList<LiteralValue> values, String type) {
        this.operator = operator;
        this.trigger = trigger;
        this.table = table;
        this.values = values;
        this.type = type;
        this.name = name;
    }

	public Operator getOperator() {
		return operator;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public Table getTable() {
		return table;
	}

	public ArrayList<LiteralValue> getValues() {
		return values;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
       

}
