package cn.edu.njupt.energy_saver.task;

import cn.edu.njupt.energy_saver.config.GlobalConfig;
import cn.edu.njupt.energy_saver.dataobject.DeviceDetail;
import cn.edu.njupt.energy_saver.dataobject.DeviceStrategy;
import cn.edu.njupt.energy_saver.repo.DeviceDetailRepo;
import cn.edu.njupt.energy_saver.repo.DeviceStrategyRepo;
import cn.edu.njupt.energy_saver.service.MessageService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


@EnableScheduling
@Component
public class TimerTask {

    private static final String PATTERN = "yyyy-MM-dd hh:mm";

    @Autowired
    DeviceDetailRepo deviceDetailRepo;

    @Autowired
    DeviceStrategyRepo deviceStrategyRepo;

    @Autowired
    MessageService messageService;

    @Scheduled(cron = "0/2 * * * * ?")
    private void configureTask() {

        Map<String, String> orders = new HashMap<>();

        List<DeviceStrategy> deviceStrategies = deviceStrategyRepo.findAll();
        deviceStrategies.forEach(d -> {
            String time = d.getTime();
            if (!time.contains("weekStart")) {
                byDay(orders, time, d.getDeviceGroup());

            } else {
                byWeek(orders, time, d.getDeviceGroup());
            }

        });

        orders.forEach((key, value) -> messageService.setOrderMessageByGroup("status:" + value, key));
    }

    //时间可以设置长一点
//    @Scheduled(cron = "0/5 * * * * ?")
    private void wipeStatus(){
        if (GlobalConfig.deviceStatus == null) return;
        List<String> suspectOffline = GlobalConfig.deviceStatus.entrySet().stream().filter(e -> !e.getValue()).map(Map.Entry::getKey).collect(Collectors.toList());
        GlobalConfig.deviceStatus = deviceDetailRepo.getAllDeviceId().stream().collect(Collectors.toMap(v -> v, v -> false));

        suspectOffline.forEach(s -> {
            List<DeviceDetail> des = deviceDetailRepo.findAllByDeviceId(s);
            des.forEach(d -> d.setStatus("offline"));
            deviceDetailRepo.save(des);
        });

    }

    private void byWeek(Map<String, String> orders, String time, String deviceGroup) {
        JSONObject jsonObject = JSONObject.parseObject(time);

        Date now = new Date();

        JSONArray weekDay = jsonObject.getJSONArray("time");
        int weekStart = Integer.parseInt(jsonObject.getString("weekStart").split("-")[1]);
        int weekEnd = Integer.valueOf(jsonObject.getString("weekEnd").split("-")[1]);
        int year = Integer.parseInt(jsonObject.getString("weekStart").split("-")[0]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        calendar.set(Calendar.WEEK_OF_YEAR, weekStart);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        if(now.before(calendar.getTime())) return;

        calendar.set(Calendar.WEEK_OF_YEAR, weekEnd);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        if(now.after(calendar.getTime())) {
            return;
        }

        calendar.clear();

        int today = calendar.get(Calendar.DAY_OF_WEEK);

        String currentTimes = weekDay.getString((today - 2) % 7);
        if(StringUtils.isBlank(currentTimes)) return;

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = DateUtils.parseDate(currentTimes.split("~")[0]);
            endTime = DateUtils.parseDate(currentTimes.split("~")[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!now.before(startTime) && !now.after(endTime)){
            orders.put(deviceGroup, "allow");
        }

    }

    private void byDay(Map<String, String> orders, String time, String deviceGroup) {
        Date now = new Date();

        String currentStatus = orders.get(deviceGroup);

        if (StringUtils.isNotBlank(currentStatus) && currentStatus.equals("allow")) return;

        JSONArray jsonArray = JSONObject.parseArray(time);

        jsonArray.forEach(j -> {
            String t = j.toString();
            String start = t.split("~")[0];
            String end = t.split("~")[1];

            Date dateStart = null;
            Date dateEnd = null;
            try {
                dateStart = DateUtils.parseDate(start, PATTERN);
                dateEnd = DateUtils.parseDate(end, PATTERN);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (now.before(dateStart)) {
                return;
            } else if (now.after(dateEnd)) {
                orders.put(deviceGroup, "forbidden");
            } else {
                orders.put(deviceGroup, "allow");
            }
        });


    }
}
