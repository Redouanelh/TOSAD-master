package generate.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;

public class TargetOracleDao implements TargetDao {
    private BaseDao dbconnection;

    public TargetOracleDao(BaseDao dbconnection) {
        this.dbconnection = dbconnection;
    }

    public ArrayList<String> executeCode (String triggerCode) {
        String query = triggerCode;

        ArrayList<String> result = new ArrayList();
        try (Connection conn = dbconnection.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(query);

            result.add("Trigger compiled!");

            statement.close();
        } catch (Exception e) {
            result.add("Trigger failed!");
            e.printStackTrace();
        }
        dbconnection.closeConnection();
        return result;
    }

    @Override
    public ArrayList<String> deleteOrUpdateTrigger(String triggerName) {
        String query = "DROP TRIGGER " + triggerName;
        ArrayList<String> result = new ArrayList();
        try (Connection conn = dbconnection.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(query);

            result.add("Trigger deleted!");
            statement.close();
        } catch (Exception e) {
            result.add("Trigger does not exist in the database!");
        }
        dbconnection.closeConnection();
        return result;
    }
}
