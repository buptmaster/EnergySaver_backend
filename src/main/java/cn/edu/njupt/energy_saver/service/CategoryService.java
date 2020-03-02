package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceCategory;
import cn.edu.njupt.energy_saver.repo.DeviceCategoryRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    DeviceCategoryRepo deviceCategoryRepo;

    @Autowired
    DeviceService deviceService;

    public List<DeviceCategory> getAll(){
        return deviceCategoryRepo.findAll();
    }

    public void deleteDeviceCategory(String categoryName) {

    }

    public void changeDeviceCategory(Integer prority, String pre, String now){

    }

    public void addDeviceToCategory(String deviceId, String categoryName){}
}
