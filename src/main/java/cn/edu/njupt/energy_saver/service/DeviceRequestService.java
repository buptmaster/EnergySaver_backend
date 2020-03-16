package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceRequest;
import cn.edu.njupt.energy_saver.repo.DeviceRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DeviceRequestService {

    @Autowired
    DeviceRequestRepo deviceRequestRepo;

    public List<DeviceRequest> getAllByStatus(String status){
        return deviceRequestRepo.findAllByStatus(status);
    }
}
