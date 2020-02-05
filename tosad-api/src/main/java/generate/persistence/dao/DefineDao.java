package generate.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface DefineDao {
    ArrayList<String> getTriggerInfo(String name);
    ArrayList<String> getRulesByTrigger(String triggername);

    ArrayList<HashMap<String, String>> getAllDataFromTrigger(String triggercode, String dbtype);

    ArrayList<String> getValuesFromRule(String name);
}
