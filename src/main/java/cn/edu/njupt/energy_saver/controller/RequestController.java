package cn.edu.njupt.energy_saver.controller;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.service.DeviceRequestService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                                                   defaultValue = "unauthorized") String status) {
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

    @PostMapping("/change")
    public String changeStatus(UserControl userControl,
                               @RequestParam("id") Integer id,
                               @RequestParam("status") String status) {
        deviceRequestService.changeStatus(id, status);
        return "success";
    }

}
