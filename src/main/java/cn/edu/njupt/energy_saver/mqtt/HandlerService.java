package cn.edu.njupt.energy_saver.mqtt;

import cn.edu.njupt.energy_saver.config.GlobalConfig;
import cn.edu.njupt.energy_saver.dataobject.DeviceDetail;
import cn.edu.njupt.energy_saver.repo.DeviceDetailRepo;
import cn.edu.njupt.energy_saver.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HandlerService {

    @Autowired
    DeviceDetailRepo deviceDetailRepo;

    public static Map<String, String> mqttStatus = new HashMap<>();


    public void handleMessage(String message, String topic) {
        if (topic.equals("subscribe")) {
            addDevice(message);
        } else if (topic.equals("alive")) {
            //设备发送其id向平台展示其正常
            GlobalConfig.deviceStatus.put(topic, true);
        } else if (topic.equals("$SYS/broker/bytes/received")) {
            mqttStatus.put("in", message);
            System.out.println(message);
        } else if (topic.equals("$SYS/broker/bytes/sent")) {
            mqttStatus.put("out", message);
            System.out.println(message);
        } else if (topic.equals("$SYS/broker/clients/connected")) {
            mqttStatus.put("connected", message);
            System.out.println(message);
        } else if (topic.equals("$SYS/broker/clients/disconnected")) {
            mqttStatus.put("disconnected", message);
            System.out.println(message);
        } else if (topic.equals("$SYS/broker/clients/total")) {
            mqttStatus.put("total", message);
            System.out.println(message);
        }
    }

    private void addDevice(String message) {

        DeviceDetail deviceDetail = new DeviceDetail();
        deviceDetail.setStatus("allow");
        deviceDetail.setDeviceId(IdGenerator.newId());

        if (message.contains("/")) {
            int last = message.lastIndexOf("/");
            String deviceName = message.substring(last + 1);
            deviceDetail.setDeviceName(deviceName);
            deviceDetail.setDeviceTopic(message.substring(0, last));
        } else {
            deviceDetail.setDeviceName(message);
        }

        deviceDetailRepo.save(deviceDetail);
    }
}
