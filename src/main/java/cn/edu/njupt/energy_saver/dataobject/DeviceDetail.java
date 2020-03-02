package cn.edu.njupt.energy_saver.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 设备表
 */
@Entity
@Table(name = "device_detail")
public class  DeviceDetail {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @NotNull
    private String deviceId;

    private String deviceTopic;

    @NotNull
    private String deviceName;

    @NotNull
    private String status;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceTopic() {
        return deviceTopic;
    }

    public void setDeviceTopic(String deviceTopic) {
        this.deviceTopic = deviceTopic;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
