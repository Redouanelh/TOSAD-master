package define.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TargetOracleDao implements TargetDao {
	private BaseDao dbconnection;
	
	public TargetOracleDao(BaseDao dbconnection) {
		this.dbconnection = dbconnection;
	}

	@Override
	public HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> loadTargetDatabase() {
		String query = "select user_tables.table_name, all_tab_columns.column_name, all_tab_columns.data_type from user_tables, all_tab_columns where all_tab_columns.table_name = user_tables.table_name";
        HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> data = new HashMap<>();        
        
        try (Connection conn = dbconnection.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultset = statement.executeQuery();
            
            data.put("datatable", new HashMap<String, HashMap<String, HashMap<String,String>>>());
            while(resultset.next()) {
            	if(!data.get("datatable").containsKey(resultset.getString("TABLE_NAME"))) {
            		data.get("datatable").put(resultset.getString("TABLE_NAME"), new HashMap<String, HashMap<String,String>>());
            	}
            	data.get("datatable").get(resultset.getString("TABLE_NAME")).put(resultset.getString("COLUMN_NAME"), new HashMap<String, String>());
            	data.get("datatable").get(resultset.getString("TABLE_NAME")).get(resultset.getString("COLUMN_NAME")).put("type", resultset.getString("DATA_TYPE"));
            }
            resultset.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbconnection.closeConnection();
        return data;
	}

	public HashMap<String, String> testConnection() {
        Connection conn = dbconnection.getConnection();
        HashMap<String, String> result = new HashMap();
        if (conn != null) {
            result.put("message", "Success");
        } else {
            result.put("error", "Database credentials are incorrect");
        }
        dbconnection.closeConnection();
        return result;
    }

}
