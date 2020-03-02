package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceStrategy;
import cn.edu.njupt.energy_saver.repo.DeviceStrategyRepo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyService {

    @Autowired
    DeviceStrategyRepo deviceStrategyRepo;

    public void addStrategy(){

    }

    public void addStrategies(List<DeviceStrategy> deviceStrategies){
        deviceStrategyRepo.save(deviceStrategies);
    }

    public List<DeviceStrategy> getAllStrategy(String order, String name){
        return order.equals("asc")
                ? deviceStrategyRepo.findAllByStrategyNameOrderByPriorityAsc(name)
                : deviceStrategyRepo.findAllByStrategyNameOrderByPriorityDesc(name);
    }

    public List<String> getAllNames(){
        return deviceStrategyRepo.getStrategyName();
    }

}
