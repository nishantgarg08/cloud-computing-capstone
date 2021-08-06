package com.cca.kafka.common.payload;

public class PartitionInfo {
    Integer partitionId;
    Long offsetNumber;
    boolean hotStatus;

    public PartitionInfo(Integer partitionId, Long offsetNumber, Boolean hotStatus) {
        this.partitionId = partitionId;
        this.offsetNumber = offsetNumber;
        this.hotStatus = hotStatus;
    }

    public boolean isHotStatus() {
        return hotStatus;
    }

    public void setHotStatus(boolean hotStatus) {
        this.hotStatus = hotStatus;
    }

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }

    public Long getOffsetNumber() {
        return offsetNumber;
    }

    public void setOffsetNumber(Long offsetNumber) {
        this.offsetNumber = offsetNumber;
    }
}
