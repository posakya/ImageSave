package com.kandktech.ezivizi.model_class;

public class User_detail
{
    private String image;

    private String user_id;

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [image = "+image+", user_id = "+user_id+"]";
    }
}

