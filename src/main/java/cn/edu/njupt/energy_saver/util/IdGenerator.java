package cn.edu.njupt.energy_saver.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class IdGenerator {
    public static String newId() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
}