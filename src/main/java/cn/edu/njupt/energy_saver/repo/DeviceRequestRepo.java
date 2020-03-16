package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.DeviceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRequestRepo extends JpaRepository<DeviceRequest, String> {

    List<DeviceRequest> findAllByStatus(String status);
}
