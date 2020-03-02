package cn.edu.njupt.energy_saver.controller;

import cn.edu.njupt.energy_saver.dataobject.DeviceStrategy;
import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.service.StrategyService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    StrategyService strategyService;

    @PostMapping("/add")
    public void addStrategy(UserControl userControl, @RequestBody JSONObject jsonObject){
        System.out.println(jsonObject);
        List<DeviceStrategy> deviceStrategies = new ArrayList<>();
        String strategyName = jsonObject.getString("strategyName");
        Integer priority = jsonObject.getInteger("priority");

        JSONArray child = jsonObject.getJSONArray("child");
        child.forEach(c -> {
            JSONObject j = ((JSONObject) c);

            DeviceStrategy deviceStrategy = new DeviceStrategy  ();
            deviceStrategy.setDeviceGroup(j.getString("deviceGroup"));
            deviceStrategy.setTime(j.getJSONArray("time").toJSONString());
            deviceStrategy.setStrategyName(strategyName);
            deviceStrategy.setPriority(priority);
            deviceStrategies.add(deviceStrategy);


        });
        System.out.println(JSONObject.toJSON(deviceStrategies));
        strategyService.addStrategies(deviceStrategies);
    }

    @PostMapping("/addPerWeek")
    public void addStrategiesPerWeek(UserControl userControl, @RequestBody JSONObject jsonObject){

    }

    @GetMapping("/all")
    public List<JSONObject> getAllStrategies(@RequestParam("order") String order){

        List<JSONObject> js = new ArrayList<>();


        strategyService.getAllNames()
                .forEach(name -> {
                    JSONObject bigs = new JSONObject();
                    List<JSONObject> childs = new ArrayList<>();
                    strategyService
                            .getAllStrategy(order, name)
                            .forEach(s -> {
                                JSONObject smallj = new JSONObject();
                                List<String> times = JSON.parseArray(s.getTime()).toJavaList(String.class);
                                smallj.put("priority", s.getPriority());
                                smallj.put("deviceGroup", s.getDeviceGroup());
                                smallj.put("time", times);
                                childs.add(smallj);
                            });
                    bigs.put("strategyName", name);
                    bigs.put("children", childs);
                    js.add(bigs);
                });

        return js;
    }

}
