package com.lb.voyagermq.broker.model;

public class QueueModel {

    private Integer id;
    private Long minOffset;
    private Long maxOffset;
    private Long currentOffset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMinOffset() {
        return minOffset;
    }

    public void setMinOffset(Long minOffset) {
        this.minOffset = minOffset;
    }

    public Long getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(Long maxOffset) {
        this.maxOffset = maxOffset;
    }

    public Long getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Long currentOffset) {
        this.currentOffset = currentOffset;
    }

    @Override
    public String toString() {
        return "QueueModel{" +
                "id=" + id +
                ", minOffset=" + minOffset +
                ", maxOffset=" + maxOffset +
                ", currentOffset=" + currentOffset +
                '}';
    }
}
