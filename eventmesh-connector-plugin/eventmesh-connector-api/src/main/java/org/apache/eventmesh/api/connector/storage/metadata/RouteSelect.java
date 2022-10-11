package org.apache.eventmesh.api.connector.storage.metadata;

import org.apache.eventmesh.api.connector.storage.StorageConnector;

import java.util.List;

/**
 * 
 */
public interface RouteSelect {

    public StorageConnector select(List<StorageConnector>  storageConnector);
}
