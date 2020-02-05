package define.business.domain.businessrules;

import java.util.ArrayList;

import define.business.domain.LiteralValue;
import define.business.domain.Operator;
import define.business.domain.Table;
import define.business.domain.Trigger;

public class ModifyRule implements BusinessRule {
	private String name;
    private Operator operator;
    private Trigger trigger;
    private ArrayList<LiteralValue> values;
    private Table table;
    private String type;
    
	public ModifyRule(String name, Operator operator, Trigger trigger, Table table,
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
		// TODO Auto-generated method stub
		return operator;
	}

	@Override
	public Trigger getTrigger() {
		// TODO Auto-generated method stub
		return trigger;
	}

	@Override
	public Table getTable() {
		// TODO Auto-generated method stub
		return table;
	}

	@Override
	public ArrayList<LiteralValue> getValues() {
		return values;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
}
