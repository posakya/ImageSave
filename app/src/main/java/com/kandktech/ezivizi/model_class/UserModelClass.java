package com.kandktech.ezivizi.model_class;

public class UserModelClass {
    private User_detail user_detail;

    public User_detail getUser_detail ()
    {
        return user_detail;
    }

    public void setUser_detail (User_detail user_detail)
    {
        this.user_detail = user_detail;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user_detail = "+user_detail+"]";
    }
}
