package cn.edu.njupt.energy_saver.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(name = "user_control", uniqueConstraints = @UniqueConstraint(columnNames = "authId"))
@Entity
public class UserControl {

    @Id
    private String userName;

    @JsonIgnore
    private String password;

    private String role;

    private String authId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }
}
