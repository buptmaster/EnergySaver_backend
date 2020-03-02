package cn.edu.njupt.energy_saver.controller;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public UserControl login(@RequestParam("username")String userName,
                             @RequestParam("password")String password){

        return userService.login(userName, password);
    }
}
