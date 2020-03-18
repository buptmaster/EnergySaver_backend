package cn.edu.njupt.energy_saver.mqtt;

import cn.edu.njupt.energy_saver.dataobject.DeviceDetail;
import cn.edu.njupt.energy_saver.repo.DeviceDetailRepo;
import cn.edu.njupt.energy_saver.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandlerService {

    @Autowired
    DeviceDetailRepo deviceDetailRepo;


    public void handleMessage(String message, String topic) {
        if(topic.equals("subscribe")){
            addDevice(message);
        } else {

        }
    }

    private void addDevice(String message){

        DeviceDetail deviceDetail = new DeviceDetail();
        deviceDetail.setStatus("allow");
        deviceDetail.setDeviceId(IdGenerator.newId());

        if(message.contains("/")){
            int last = message.lastIndexOf("/");
            String deviceName = message.substring(last + 1);
            deviceDetail.setDeviceName(deviceName);
            deviceDetail.setDeviceTopic(message.substring(0, last));
        }else {
            deviceDetail.setDeviceName(message);
        }

        deviceDetailRepo.save(deviceDetail);
    }
}
