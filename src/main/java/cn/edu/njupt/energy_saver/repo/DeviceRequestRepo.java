package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.DeviceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRequestRepo extends JpaRepository<DeviceRequest, Integer> {

    List<DeviceRequest> findAllByStatus(String status);

    DeviceRequest findById(Integer id);
}
