package define.business.domain.businessrules;

import java.util.ArrayList;

import define.business.domain.LiteralValue;
import define.business.domain.Operator;
import define.business.domain.Table;
import define.business.domain.Trigger;

public interface BusinessRule {
	public Operator getOperator();
	public Trigger getTrigger();
	public Table getTable();
	public ArrayList<LiteralValue> getValues();
	public String getType();
	public String getName();
}
