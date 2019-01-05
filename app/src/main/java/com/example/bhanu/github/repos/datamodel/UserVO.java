package com.example.bhanu.github.repos.datamodel;

import java.io.Serializable;

public class UserVO implements Serializable {

    private String name;
    private String login;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
