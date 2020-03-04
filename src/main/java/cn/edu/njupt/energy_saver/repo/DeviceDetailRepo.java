package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.DeviceDetail;
import cn.edu.njupt.energy_saver.dataobject.DeviceStrategy;
import cn.edu.njupt.energy_saver.dataobject.projection.DeviceDetailProj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DeviceDetailRepo extends JpaRepository<DeviceDetail, Integer> {

    @Query(value = "select d from DeviceDetail d where d.deviceName like ?1% ")
    List<DeviceDetail> findDeviceGroup(String name);

    List<DeviceDetail> findAllByDeviceNameContains(String deviceName);

    List<DeviceDetail> findAllByDeviceId(String deviceId);

    DeviceDetail findByDeviceIdAndDeviceTopic(String deviceId, String deviceTopic);

    Integer deleteByDeviceName(String deviceName);

    @Modifying
    @Transactional
    Integer deleteByDeviceId(String deviceId);

    //从0开始就好了
    @Query("select distinct d.deviceName as deviceName, d.deviceId as deviceId, d.status as status from DeviceDetail d")
    Page<DeviceDetailProj> findAllDistinct(Pageable pageable);

    @Query("select count(distinct  d.deviceId) from DeviceDetail d where d.status = ?1")
    Integer countDistinctByStatus(String status);
}