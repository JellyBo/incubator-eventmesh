package org.apache.eventmesh.connector.storage.jdbc.SQL;

/**
 *
 */
public interface BaseSQLOperation {

    public String createDatabases();
    
    public String queryConsumerGroupTableSQL();
    
    public String queryCloudEventTablesSQL();
}
