package cn.edu.njupt.energy_saver.service;

import cn.edu.njupt.energy_saver.dataobject.DeviceDetail;
import cn.edu.njupt.energy_saver.dataobject.projection.DeviceDetailProj;
import cn.edu.njupt.energy_saver.repo.DeviceDetailRepo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    DeviceDetailRepo deviceDetailRepo;

    private void dfs(List<JSONObject> children, String path, List<String> res) {
        System.out.println(path);
        if (children.size() == 0) return;
        for (JSONObject c : children) {
            String deviceId = c.getString("deviceId");
            if (!StringUtils.isBlank(deviceId))
                res.add(path + c.getString("name") + "," + deviceId);
            dfs(c.getJSONArray("children").toJavaList(JSONObject.class),
                    path + c.getString("name") + "/",
                    res);
        }
    }

    public void diffDevice(List<JSONObject> js) {
        List<String> res = new ArrayList<>();
        js.forEach(j -> {
            String path = "/" + j.getString("categoryName") + "/";

            List<JSONObject> cs = j.getJSONArray("children").toJavaList(JSONObject.class);
            dfs(cs, path, res);
            System.out.println(JSONObject.toJSON(res));

            res.forEach(r -> {
                String devicePath = r.split(",")[0];
                String deviceId = r.split(",")[1];

                int sep = devicePath.lastIndexOf("/");

                String deviceName = devicePath.substring(sep + 1);
                String deviceTopic = devicePath.substring(0, sep);
                String status;

                List<DeviceDetail> ds = deviceDetailRepo.findAllByDeviceId(deviceId);
                if (ds.size() == 0) return;
                else status = ds.get(0).getStatus();

                boolean flag = true;
                List<String> ts = getDeviceTopics(deviceId);

                if (ts.size() == 0) {
                    DeviceDetail deviceDetail = new DeviceDetail();
                    deviceDetail.setDeviceTopic(devicePath);
                    deviceDetail.setDeviceId(deviceId);
                    deviceDetail.setDeviceName(deviceName);
                    deviceDetail.setStatus(status);
                    deviceDetailRepo.save(deviceDetail);
                }

                for (String t : ts) {
                    String root = t.split("/")[0];
                    String root2 = devicePath.split("/")[0];
                    if (root.equals(root2)) {
                        changeDeviceTopic(t, deviceTopic, deviceId);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    DeviceDetail deviceDetail = new DeviceDetail();
                    deviceDetail.setDeviceTopic(devicePath);
                    deviceDetail.setDeviceId(deviceId);
                    deviceDetail.setDeviceName(deviceName);
                    deviceDetailRepo.save(deviceDetail);
                }
            });
        });
    }

    public Map<String, Integer> getDeviceCount() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("offline", deviceDetailRepo.countDistinctByStatus("offline"));
        map.put("allow", deviceDetailRepo.countDistinctByStatus("allow"));
        map.put("forbidden", deviceDetailRepo.countDistinctByStatus("forbidden"));
        return map;
    }

    public void sendMessageToDevice(String deviceTopic, String Message) {
    }

    public void sendMessageToDevices(String deviceGroup, String Message) {
    }

    public Page<DeviceDetailProj> getAllDevices(int pageNum, int pageSize) {
        return deviceDetailRepo.findAllDistinct(new PageRequest(pageNum - 1, pageSize));
    }

    public List<String> getDeviceTopics(String deviceId) {
        return deviceDetailRepo.findAllByDeviceId(deviceId)
                .stream()
                .map(DeviceDetail::getDeviceTopic)
                .collect(Collectors.toList());
    }

    public List<DeviceDetail> searchDevice(String name) {
        return deviceDetailRepo.findAllByDeviceNameContains(name);
    }


    public void changeDeviceTopic(String preTopic, String nowTopic, String deviceId) {
        DeviceDetail d = deviceDetailRepo.findByDeviceIdAndDeviceTopic(deviceId, preTopic);
        if (d == null) return;
        d.setDeviceTopic(nowTopic);
        deviceDetailRepo.save(d);
    }

    public void removeDeviceTopics(String deviceId) {
        List<DeviceDetail> deviceDetails = deviceDetailRepo.findAllByDeviceId(deviceId);
        if (!deviceDetails.isEmpty()) {
            DeviceDetail deviceDetail = deviceDetails.get(0);
            DeviceDetail save = new DeviceDetail();
            BeanUtils.copyProperties(deviceDetail, save);
            save.setDeviceTopic("");

            deviceDetailRepo.deleteByDeviceId(deviceDetail.getDeviceId());

            System.out.println(JSONObject.toJSON(save));

            deviceDetailRepo.save(save);
        }
    }

    public void deleteDevice(String deviceId) {
        deviceDetailRepo.deleteByDeviceId(deviceId);
    }

}
