package com.convict.po;

/**
 * @Author Convict
 * @Date 2018/12/25 9:31
 */
public class Admin {

    private String account;// 账号
    private String password;// 密码
    private String nickname;// 昵称
    private String userPhoto;// 头像

    public Admin() {
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                '}';
    }
}
