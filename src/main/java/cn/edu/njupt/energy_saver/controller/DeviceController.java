package cn.edu.njupt.energy_saver.controller;

import cn.edu.njupt.energy_saver.dataobject.DeviceCategory;
import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.dataobject.projection.DeviceDetailProj;
import cn.edu.njupt.energy_saver.service.CategoryService;
import cn.edu.njupt.energy_saver.service.DeviceService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/counts")
    public Map<String, Integer> getDeviceCounts(UserControl userControl){
        return deviceService.getDeviceCount();
    }

    @GetMapping("/all")
    public JSONObject getAllDevice(UserControl userControl,
                                   @RequestParam("pageNum")int pageNum,
                                   @RequestParam("pageSize")int pageSize){

        JSONObject jsonObject = new JSONObject();
        Page<DeviceDetailProj> deviceDetailProjs = deviceService.getAllDevices(pageNum, pageSize);
        List<List<String>> topics = new ArrayList<>();
        deviceDetailProjs
                .getContent()
                .forEach(deviceDetailProj -> topics.add(deviceService.getDeviceTopics(deviceDetailProj.getDeviceId())));

        jsonObject.put("pages", deviceDetailProjs);
        jsonObject.put("topics", topics);
        return jsonObject;
    }

    @PostMapping("/removeTopics")
    public String removeTopics(UserControl userControl,
                               @RequestParam("deviceId")String deviceId){
        deviceService.removeDeviceTopics(deviceId);
        return "success";
    }

    @PostMapping("/delete")
    public String deleteDevice(UserControl userControl,
                               @RequestParam("deviceId")String deviceId){
        deviceService.deleteDevice(deviceId);
        return "success";
    }

    @GetMapping("/category")
    public List<JSONObject> getAllCategory(){
        List<JSONObject> res = new ArrayList<>();
        categoryService.getAll()
                .forEach(c -> {
                    JSONObject j = new JSONObject();
                    j.put("tag", c.getTag());
                    j.put("categoryName", c.getCategoryName());
                    j.put("children", JSONObject.parse(c.getChildren()));
                    res.add(j);
                });

        return res;
    }

}
