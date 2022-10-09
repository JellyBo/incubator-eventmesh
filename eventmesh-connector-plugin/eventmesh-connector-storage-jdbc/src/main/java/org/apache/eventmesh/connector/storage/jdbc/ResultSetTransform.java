package org.apache.eventmesh.connector.storage.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author laohu
 * @param <T>
 */
public interface ResultSetTransform<T> {

    public T transform(ResultSet resultSet)  throws SQLException;
}
