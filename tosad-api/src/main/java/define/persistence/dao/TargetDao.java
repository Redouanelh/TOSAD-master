package define.persistence.dao;

import java.util.HashMap;

public interface TargetDao {
	public HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> loadTargetDatabase();
	public HashMap<String, String> testConnection();
}
