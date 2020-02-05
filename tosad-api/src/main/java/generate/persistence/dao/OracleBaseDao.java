package generate.persistence.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class OracleBaseDao implements BaseDao {

    private String url = "";
    private String user = "";
    private String pass = "";
    protected Connection myConn;
    protected Statement myStmt;

    public OracleBaseDao(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public Connection getConnection() {
        try {
            myConn = DriverManager.getConnection(url, user, pass);
            myStmt = myConn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myConn;
    }

    public void closeConnection() {
        try {
            myConn.close();
            myStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
