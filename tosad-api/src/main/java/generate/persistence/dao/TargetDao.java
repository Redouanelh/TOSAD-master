package generate.persistence.dao;

import java.util.ArrayList;

public interface TargetDao {
    ArrayList<String> executeCode(String s);
    ArrayList<String> deleteOrUpdateTrigger(String triggerName);
}
