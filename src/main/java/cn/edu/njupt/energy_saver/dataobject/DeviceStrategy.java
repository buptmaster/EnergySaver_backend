package cn.edu.njupt.energy_saver.dataobject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 * 设置策略表
 */
@Table
@Entity
public class DeviceStrategy {

    @NotNull
    private int priority;

    private String strategyName;

    @Id
    private String deviceGroup;

    /**
     * 日期+时间的JSON
     * ["2010-10-1 20:00~2010-10-1 21:00"]
     */
    @Column(columnDefinition = "text")
    private String time;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
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
}
