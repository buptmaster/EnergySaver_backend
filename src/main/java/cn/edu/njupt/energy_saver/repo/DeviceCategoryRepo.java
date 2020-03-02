package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.DeviceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceCategoryRepo extends JpaRepository<DeviceCategory, String> {

}