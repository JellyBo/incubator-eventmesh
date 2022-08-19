/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.eventmesh.api.connector.storage;

import java.util.List;
import java.util.Properties;

import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.LifeCycle;
import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;
import org.apache.eventmesh.spi.EventMeshExtensionType;
import org.apache.eventmesh.spi.EventMeshSPI;

import io.cloudevents.CloudEvent;
import io.grpc.Metadata;

@EventMeshSPI(isSingleton = false, eventMeshExtensionType = EventMeshExtensionType.CONNECTOR)
public interface StorageConnector extends LifeCycle{

	
	void init(Properties properties) throws Exception;
	
    void publish(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception;

    void request(CloudEvent cloudEvent, RequestReplyCallback rrCallback, long timeout) throws Exception;
	
    /**
     * 有数据，无数据
     * @return
     */
	public List<CloudEvent> pull(PullRequest pullRequest);
	
	void updateOffset(List<CloudEvent> cloudEvents, AbstractContext context);
	
	public Metadata queryMetaData();
	
	public int deleteCloudEvent();
	
	/**
	 * 
	 * 创建表
	 * @param topicInfo
	 * @return
	 */
	public int createTopic(TopicInfo topicInfo);
	
	public int createConsumerGroup(ConsumerGroupInfo consumerGroupInfo);
	
	@Override
	public default boolean isStarted() {
		return true;
	}

	@Override
	public default boolean isClosed() {
		return false;
	}

	
}
