package com.lb.voyagermq.broker.model;

import java.util.List;

public class VoyagerMqTopicModel {

    private String topic;
    private List<QueueModel> queueList;
    private Long createAt;
    private Long updateAt;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<QueueModel> getQueueList() {
        return queueList;
    }

    public void setQueueList(List<QueueModel> queueList) {
        this.queueList = queueList;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "EagleMqTopicModel{" +
                "topic='" + topic + '\'' +
                ", queueList=" + queueList +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
