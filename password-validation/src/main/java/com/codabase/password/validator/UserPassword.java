package com.codabase.password.validator;

import java.util.ArrayList;
import java.util.List;


public class UserPassword {


    private String password;


    private List<String> failureList;

    public List<String> getFailureList() {
      if(failureList==null)
          failureList = new ArrayList<>();
      return failureList;
    }

    public UserPassword(String password)
    {
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFailureList(List<String> failureList) {
        this.failureList = failureList;
    }
}