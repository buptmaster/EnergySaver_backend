package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.exception.CustomError;
import cn.edu.njupt.energy_saver.exception.LocalRuntimeException;
import cn.edu.njupt.energy_saver.repo.UserControlRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
