package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.DeviceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeviceCategoryRepo extends JpaRepository<DeviceCategory, String> {

    DeviceCategory findByCategoryNameAndTag(String categoryName, String tag);
}