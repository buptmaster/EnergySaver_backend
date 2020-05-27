package cn.edu.njupt.energy_saver.config;

import cn.edu.njupt.energy_saver.repo.DeviceDetailRepo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GlobalConfig implements InitializingBean, ServletContextAware {

    @Autowired
    DeviceDetailRepo deviceDetailRepo;

    public static Map<String, Boolean> deviceStatus = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        List<String> ids = deviceDetailRepo.getAllDeviceId();
        deviceStatus = ids.stream().collect(Collectors.toMap(v -> v, v -> false));
        System.out.println(JSONObject.toJSON(deviceStatus));
    }

}
