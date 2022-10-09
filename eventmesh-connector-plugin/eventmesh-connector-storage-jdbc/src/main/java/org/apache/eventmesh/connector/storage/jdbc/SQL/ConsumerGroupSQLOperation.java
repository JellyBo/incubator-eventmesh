package org.apache.eventmesh.connector.storage.jdbc.SQL;

/**
 *
 */
public interface ConsumerGroupSQLOperation {

    public String createConsumerGroupSQL();
    
    public String insertConsumerGroupSQL();
    
    public String selectConsumerGroupSQL();
}
