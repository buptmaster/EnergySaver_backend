package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceDetail;
import cn.edu.njupt.energy_saver.mqtt.MyGateWay;
import cn.edu.njupt.energy_saver.repo.DeviceDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息格式 xxx:xxx
 */
@Service
public class MessageService {

    @Autowired
    MyGateWay myGateWay;

    @Autowired
    DeviceDetailRepo deviceDetailRepo;

    public void setTopicChangedMessage(String newTopic, String deviceId){
        myGateWay.sendToMqtt("change:"+newTopic, deviceId);
    }

    /**
     * deviceId为设备对MQTT消息队列的实质订阅
     * @param order
     * @param topic
     */
    public void setOrderMessageByGroup(String order, String topic){
        List<DeviceDetail> deviceDetails = deviceDetailRepo.findDeviceGroup(topic);
        deviceDetails.forEach(d -> {
            myGateWay.sendToMqtt(order, d.getDeviceId());
        });
    }

    public void setOrderMessage(String order, String deviceId){
        myGateWay.sendToMqtt(order, deviceId);
    }

}
