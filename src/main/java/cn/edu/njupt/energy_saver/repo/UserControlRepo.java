package cn.edu.njupt.energy_saver.repo;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserControlRepo extends JpaRepository<UserControl, String> {

    UserControl findByUserName(String userName);

    UserControl findByAuthId(String authId);

    @Transactional
    Integer deleteByUserName(String userName);
}
