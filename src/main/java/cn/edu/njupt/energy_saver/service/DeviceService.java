package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceDetail;
import cn.edu.njupt.energy_saver.dataobject.projection.DeviceDetailProj;
import cn.edu.njupt.energy_saver.repo.DeviceDetailRepo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    DeviceDetailRepo deviceDetailRepo;


    public Map<String,Integer> getDeviceCount(){
        HashMap<String, Integer> map = new HashMap<>();
        map.put("offline", deviceDetailRepo.countDistinctByStatus("offline"));
        map.put("allow", deviceDetailRepo.countDistinctByStatus("allow"));
        map.put("forbidden", deviceDetailRepo.countDistinctByStatus("forbidden"));
        return map;
    }

    public void sendMessageToDevice(String deviceTopic, String Message) {
    }

    public void sendMessageToDevices(String deviceGroup, String Message) {
    }

    public Page<DeviceDetailProj> getAllDevices(int pageNum, int pageSize) {
        return deviceDetailRepo.findAllDistinct(new PageRequest(pageNum - 1, pageSize));
    }

    public List<String> getDeviceTopics(String deviceId){
        return deviceDetailRepo.findAllByDeviceId(deviceId)
                .stream()
                .map(DeviceDetail::getDeviceTopic)
                .collect(Collectors.toList());
    }


    public void changeDeviceTopic(String pre, String now){

    }

    public void removeDeviceTopics(String deviceId){
        List<DeviceDetail> deviceDetails = deviceDetailRepo.findAllByDeviceId(deviceId);
        if(!deviceDetails.isEmpty()){
            DeviceDetail deviceDetail = deviceDetails.get(0);
            DeviceDetail save = new DeviceDetail();
            BeanUtils.copyProperties(deviceDetail, save);
            save.setDeviceTopic("");

            deviceDetailRepo.deleteByDeviceId(deviceDetail.getDeviceId());

            System.out.println(JSONObject.toJSON(save));

            deviceDetailRepo.save(save);
        }
    }

    public void deleteDevice(String deviceId){
        deviceDetailRepo.deleteByDeviceId(deviceId);
    }

}
