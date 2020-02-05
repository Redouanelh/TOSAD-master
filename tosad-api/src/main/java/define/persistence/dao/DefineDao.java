package define.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;

import define.business.domain.businessrules.BusinessRule;

public interface DefineDao {
	public HashMap<String, HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>> getAvailableInput();
	public String defineRule(BusinessRule rule);
	public String deleteBusinessRule(String name);	
}
