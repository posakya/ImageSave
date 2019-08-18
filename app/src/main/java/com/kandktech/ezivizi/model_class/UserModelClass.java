package com.kandktech.ezivizi.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserModelClass {

    @SerializedName("data")
    @Expose
    private List<User_detail> data;

    public List<User_detail> getData() {
        return data;
    }

    public void setData(List<User_detail> data) {
        this.data = data;
    }

    private String message;

    private String status;



    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [data = "+data+", message = "+message+", status = "+status+"]";
    }
}
