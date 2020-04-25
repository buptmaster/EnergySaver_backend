package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserControlRepo extends JpaRepository<UserControl, String> {

    UserControl findByUserName(String userName);

    UserControl findByAuthId(String authId);

    Integer deleteByUserName(String userName);
}
