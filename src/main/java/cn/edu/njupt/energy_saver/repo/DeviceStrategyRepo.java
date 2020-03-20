package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.DeviceStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DeviceStrategyRepo extends JpaRepository<DeviceStrategy, String> {

    List<DeviceStrategy> findAllByStrategyNameOrderByPriorityAsc(String strategyName);

    List<DeviceStrategy> findAllByStrategyNameOrderByPriorityDesc(String strategyName);

    @Transactional
    Integer deleteByStrategyName(String strategyName);

    @Transactional
    Integer deleteByDeviceGroup(String deviceGroup);

    List<DeviceStrategy> findAllByPriority(int priority);

    @Query("select distinct s.strategyName from DeviceStrategy s")
    List<String> getStrategyName();

}
