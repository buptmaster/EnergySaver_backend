package cn.edu.njupt.energy_saver.dataobject;

import javax.persistence.*;

/**
 * 设备组的表
 * 有类名和对应的类
 */
@Table(name = "device_category")
@Entity
public class DeviceCategory {

    /**
     * [{tag:xxx, categoryName:xxx, child:[ {name: xxx, children:[] },  ] },{...}]
     */


    private String tag;

    @Id
    private String categoryName;

    @Column(columnDefinition = "text")
    private String children;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }
}
