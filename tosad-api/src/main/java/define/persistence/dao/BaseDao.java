package define.persistence.dao;

import java.sql.Connection;

public interface BaseDao {
    public Connection getConnection();
    public void closeConnection();
}
