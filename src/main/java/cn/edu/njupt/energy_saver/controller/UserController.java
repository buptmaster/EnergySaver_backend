package cn.edu.njupt.energy_saver.controller;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.service.DeviceRequestService;
import cn.edu.njupt.energy_saver.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    DeviceRequestService deviceRequestService;

    @PostMapping("/login")
    public UserControl login(@RequestParam("username")String userName,
                             @RequestParam("password")String password){

        return userService.login(userName, password);
    }

    @PostMapping("/commit")
    public String commitRequest(UserControl userControl, @RequestBody JSONObject jsonObject) {
        jsonObject.put("username", userControl.getUserName());
        jsonObject.put("status", "unauthorized");
        deviceRequestService.addRequest(jsonObject);
        System.out.println(jsonObject);
        return "success";
    }
}
