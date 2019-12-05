package com.transo.hualiantou.token;

/**
 * 开发中建议使用实体类型或BO，DTO数据对象
 */
public class JWTSubject {

    private String username;

    private String email;

    public JWTSubject() {
    }

    public String getUsername() {
        return username;
    }

    public JWTSubject(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "JWTSubject{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
