package org.apache.eventmesh.api.connector.storage.metadata;

import org.apache.eventmesh.api.connector.storage.StorageConnector;
import org.apache.eventmesh.api.connector.storage.StorageConnectorMetadata;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.Metadata;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;
import org.apache.eventmesh.api.connector.storage.pull.StoragePullService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Setter;

public class StorageMetaServcie {

    protected static final Logger messageLogger = LoggerFactory.getLogger("message");

    private static final String PROCESS_SIGN = Long.toString(System.currentTimeMillis());
    
    @Setter
    private ScheduledExecutorService scheduledExecutor;

    @Setter
    private Executor executor;
    
    @Setter
    private StoragePullService storagePullService;

    private Map<StorageConnectorMetadata, Metadata> metaDataMap = new ConcurrentHashMap<>();

    public void init() {
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                StorageMetaServcie.this.pullMeteData();
            }
        }, 5, 1000, TimeUnit.MILLISECONDS);
    }

    public void registerStorageConnector(StorageConnectorMetadata storageConnector) {
        metaDataMap.put(storageConnector, new Metadata());
    }

    public void registerPullRequest(List<PullRequest> pullRequests, StorageConnector storageConnector) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                StorageMetaServcie.this.doRegisterPullRequest(pullRequests, storageConnector);
            }

        });
    }

    public void doRegisterPullRequest(List<PullRequest> pullRequests, StorageConnector storageConnector) {
        try {
            StorageConnectorMetadata storageConnectorMetadata = null;
            if (storageConnector instanceof StorageConnectorMetadata) {
                storageConnectorMetadata = (StorageConnectorMetadata) storageConnector;
            }

            Map<String, ConsumerGroupInfo> consumerGroupInfoMap = new HashMap<>();
            Set<String> topicSet = new HashSet<>();
            Map<String, TopicInfo> topicInfoMap = new HashMap<>();
            if (Objects.nonNull(storageConnectorMetadata)) {
                List<ConsumerGroupInfo> consumerGroupInfos = storageConnectorMetadata.getConsumerGroupInfo();
                consumerGroupInfos.forEach(value -> consumerGroupInfoMap.put(value.getConsumerGroupName(), value));
                topicSet = storageConnectorMetadata.getTopic();
                storageConnectorMetadata.geTopicInfos(pullRequests)
                        .forEach(value -> topicInfoMap.put(value.getTopicName(), value));
            }
            for (PullRequest pullRequest : pullRequests) {
                if (Objects.nonNull(storageConnectorMetadata) && !topicSet.contains(pullRequest.getTopicName())) {
                    try {
                        if (!topicSet.contains(pullRequest.getTopicName())) {
                            TopicInfo topicInfo = new TopicInfo();
                            storageConnectorMetadata.createTopic(topicInfo);
                        }
                        if (!consumerGroupInfoMap.containsKey(pullRequest.getConsumerGroupName())) {
                            ConsumerGroupInfo consumerGroupInfo = new ConsumerGroupInfo();
                            storageConnectorMetadata.createConsumerGroupInfo(consumerGroupInfo);
                        }
                        TopicInfo topicInfo = topicInfoMap.get(pullRequest.getTopicName());
                        pullRequest.setNextId(Long.toString(topicInfo.getCurrentId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                pullRequest.setProcessSign(PROCESS_SIGN);
                pullRequest.setStorageConnector(storageConnector);
                storagePullService.executePullRequestLater(pullRequest);
            }
        } catch (Exception e) {
            messageLogger.error(e.getMessage(), e);
        }
    }

    public void pullMeteData() {
        for (StorageConnectorMetadata storageConnectorMetadata : metaDataMap.keySet()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    StorageMetaServcie.this.doPullMeteData(storageConnectorMetadata);
                }
            });
        }
    }

    public void doPullMeteData(StorageConnectorMetadata storageConnectorMetadata) {
        try {
            Metadata metadata = new Metadata();
            metadata.setTopicSet(storageConnectorMetadata.getTopic());
            metaDataMap.put(storageConnectorMetadata, metadata);
        } catch (Exception e) {
            messageLogger.error(e.getMessage(), e);
        }
    }

    public boolean isTopic(StorageConnector storageConnector, String topic) {
        if (storageConnector instanceof StorageConnectorMetadata) {
            return metaDataMap.get((StorageConnectorMetadata) storageConnector).getTopicSet().contains(topic);
        }
        return true;

    }
}