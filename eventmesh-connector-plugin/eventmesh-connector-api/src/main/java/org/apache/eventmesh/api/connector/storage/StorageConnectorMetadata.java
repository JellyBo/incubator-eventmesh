package org.apache.eventmesh.api.connector.storage;

import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;

import java.util.List;
import java.util.Set;

/**
 *
 */
public interface StorageConnectorMetadata {

    Set<String> getTopic() throws Exception;

    List<ConsumerGroupInfo> getConsumerGroupInfo() throws Exception;

    List<TopicInfo> geTopicInfos(List<PullRequest> pullRequests) throws Exception;

    int createTopic(TopicInfo topicInfo) throws Exception;

    int createConsumerGroupInfo(ConsumerGroupInfo consumerGroupInfo) throws Exception;

}
