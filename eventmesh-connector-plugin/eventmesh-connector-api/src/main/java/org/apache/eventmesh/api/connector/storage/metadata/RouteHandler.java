package org.apache.eventmesh.api.connector.storage.metadata;

import org.apache.eventmesh.api.connector.storage.StorageConnector;

import java.util.List;

import lombok.Setter;

public class RouteHandler {

    @Setter
    List<StorageConnector>  storageConnector;
    
    
    private RouteSelect routeSelect;
    
    
    public StorageConnector select() {
        return routeSelect.select(storageConnector);
    }
}
