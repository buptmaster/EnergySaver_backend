package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceCategory;
import cn.edu.njupt.energy_saver.repo.DeviceCategoryRepo;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    DeviceCategoryRepo deviceCategoryRepo;

    @Autowired
    DeviceService deviceService;

    public List<DeviceCategory> getAll(){
        return deviceCategoryRepo.findAll();
    }

    public List<String> getAllCategoriesByKey(String key){
        List<DeviceCategory> ds = deviceCategoryRepo.findAll();
        List<String> res = new ArrayList<>();

        ds.forEach(d -> {
            String categoryName = d.getCategoryName();
            String cs = d.getChildren();
            List<JSONObject> children = JSONObject.parseArray(cs, JSONObject.class);
            dfs(children, "/" + categoryName + "/", res);
        });

        System.out.println(JSONObject.toJSON(res));

        return res.stream()
                .filter(r -> r.contains(key))
                .collect(Collectors.toList());
    }

    private void dfs(List<JSONObject> children, String path, List<String> res) {
        if (children.size() == 0){
            res.add(path);
            return;
        }
        for (JSONObject c : children) {
            if(StringUtils.isNotBlank(c.getString("deviceId"))){
                res.add(path);
                continue;
            }
            dfs(c.getJSONArray("children").toJavaList(JSONObject.class),
                    path + c.getString("name") + "/",
                    res);
        }
    }
}
