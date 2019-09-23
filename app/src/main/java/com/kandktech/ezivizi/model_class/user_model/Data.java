package com.kandktech.ezivizi.model_class.user_model;

public class Data
{
    private String image;

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [image = "+image+"]";
    }

}

