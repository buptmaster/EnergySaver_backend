package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.exception.CustomError;
import cn.edu.njupt.energy_saver.exception.LocalRuntimeException;
import cn.edu.njupt.energy_saver.repo.UserControlRepo;
import cn.edu.njupt.energy_saver.util.IdGenerator;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserControlRepo userControlRepo;

    public UserControl login(String username, String password){
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password))
            throw new LocalRuntimeException(CustomError.WROING_PASSWORD);

        UserControl userControl = userControlRepo.findByUserName(username);
        if(userControl == null) throw new LocalRuntimeException(CustomError.NOT_FOUND);
        if(!userControl.getPassword().equals(password)) throw new LocalRuntimeException(CustomError.WROING_PASSWORD);
        return userControl;
    }

    public UserControl findUserByAuth(String auth){
        return userControlRepo.findByAuthId(auth);
    }

    public List<UserControl> getAllUsers(){
        return userControlRepo.findAll();
    }

    public void addUser(JSONObject jsonObject) {
        UserControl userControl = new UserControl();
        userControl.setAuthId(IdGenerator.newId());
        userControl.setUserName(jsonObject.getString("username"));
        userControl.setPassword(jsonObject.getString("password"));
        userControl.setRole(jsonObject.getString("role"));

        userControlRepo.save(userControl);
    }

    public void deleteUser(String username, UserControl userControl) {
        UserControl needDeleted = userControlRepo.findByUserName(username);
        if (userControl.getRole().equals("NORMAL")) return;
        userControlRepo.deleteByUserName(username);
    }
}
