package cn.edu.njupt.energy_saver.dataobject;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity
public class DeviceRequest {

    @Id
    String username;

    String reason;

    String deviceGroup;

    String time;

    @CreatedDate
    Date createTime;

    /**
     * unauthorized
     * granted
     * denied
     */
    String status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(String deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
