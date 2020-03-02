package cn.edu.njupt.energy_saver.dataobject;

import javax.persistence.*;

/**
 * 动态配置表
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "kkey")})
public class DynamicConfig {

    @Id
    @Column(name = "kkey")
    private String key;

    private String value;

    private String category;

}
