package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceRequest;
import cn.edu.njupt.energy_saver.dataobject.DeviceStrategy;
import cn.edu.njupt.energy_saver.repo.DeviceRequestRepo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class DeviceRequestService {

    @Autowired
    DeviceRequestRepo deviceRequestRepo;

    @Autowired
    StrategyService strategyService;

    public List<DeviceRequest> getAllByStatus(String status){
        return deviceRequestRepo.findAllByStatus(status);
    }

    public void changeStatus(Integer id, String status) {
        DeviceRequest deviceRequest = deviceRequestRepo.findById(id);
        deviceRequest.setStatus(status);
        deviceRequestRepo.save(deviceRequest);

        if (status.equals("granted")) {
            DeviceStrategy deviceStrategy = new DeviceStrategy();
            Integer need = strategyService.getTopPriority() < 999 ? 999 : strategyService.getTopPriority() + 1;
            deviceStrategy.setPriority(need);
            deviceStrategy.setDeviceGroup(deviceRequest.getDeviceGroup());
            deviceStrategy.setStrategyName("批准用户策略" + need + "级");
//            deviceStrategy.setTime(deviceRequest.getTime());
            deviceStrategy.setTime(JSONObject.toJSONString(Collections.singletonList(deviceRequest.getTime())));

            strategyService.addStrategies(Collections.singletonList(deviceStrategy));
        }

    }

    public void addRequest(JSONObject jsonObject) {
        DeviceRequest deviceRequest = jsonObject.toJavaObject(DeviceRequest.class);
        deviceRequestRepo.save(deviceRequest);
    }
}
