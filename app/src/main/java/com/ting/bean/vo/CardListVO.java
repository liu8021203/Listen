package com.ting.bean.vo;

public class CardListVO {


    /**
     * id : 3
     * hostId : 5463
     * startTime : 2018-10-10 22:41:05
     * endTime : 2018-12-09 22:41:05
     * hostImage : ./Uploads/201605/5742761ccf2ac.jpg
     * hostName : null
     */

    private String id;
    private String hostId;
    private String startTime;
    private String endTime;
    private String hostImage;
    private String hostName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getHostImage() {
        return hostImage;
    }

    public void setHostImage(String hostImage) {
        this.hostImage = hostImage;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
