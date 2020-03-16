package cn.edu.njupt.energy_saver.controller;

import cn.edu.njupt.energy_saver.dataobject.DeviceRequest;
import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.service.DeviceRequestService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    DeviceRequestService deviceRequestService;

    @GetMapping
    public List<JSONObject> getAllRequests(UserControl userControl,
                                           @RequestParam(
                                       name = "status",
                                       required = false,
                                       defaultValue = "unauthorized")String status){
        return deviceRequestService.getAllByStatus(status)
                .stream()
                .map(dr -> {
                    String date = dr.getCreateTime().toString();
                    JSONObject j = JSONObject.parseObject(JSONObject.toJSONString(dr));
                    j.remove("createTime");
                    j.put("createTime", date);
                    return j;
                }).collect(Collectors.toList());

    }

}
