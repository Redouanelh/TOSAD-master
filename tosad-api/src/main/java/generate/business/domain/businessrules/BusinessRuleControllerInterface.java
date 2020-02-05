package generate.business.domain.businessrules;

import java.util.ArrayList;
import java.util.HashMap;

import generate.business.domain.businessrules.ruleattributes.Trigger;

public interface BusinessRuleControllerInterface {

	public Trigger fillTriggerWithRules (ArrayList<HashMap<String, String>> ruleData, Trigger trigger);
}
