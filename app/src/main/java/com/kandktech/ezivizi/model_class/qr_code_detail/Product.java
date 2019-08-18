package com.kandktech.ezivizi.model_class.qr_code_detail;

public class Product {
    private String image;

    private String address;

    private String paid_status;

    private String po_box_no;

    private String created_at;

    private String layout;

    private String updated_at;

    private String phone;

    private String user_id;

    private String company_name;

    private String name;

    private String weblink;

    private String id;

    private String position;

    private String color_code_second;

    private String email;

    private String color_code;

    private String fax_no;

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getPaid_status ()
    {
        return paid_status;
    }

    public void setPaid_status (String paid_status)
    {
        this.paid_status = paid_status;
    }

    public String getPo_box_no ()
    {
        return po_box_no;
    }

    public void setPo_box_no (String po_box_no)
    {
        this.po_box_no = po_box_no;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getLayout ()
    {
        return layout;
    }

    public void setLayout (String layout)
    {
        this.layout = layout;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getCompany_name ()
    {
        return company_name;
    }

    public void setCompany_name (String company_name)
    {
        this.company_name = company_name;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getWeblink ()
    {
        return weblink;
    }

    public void setWeblink (String weblink)
    {
        this.weblink = weblink;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPosition ()
    {
        return position;
    }

    public void setPosition (String position)
    {
        this.position = position;
    }

    public String getColor_code_second ()
    {
        return color_code_second;
    }

    public void setColor_code_second (String color_code_second)
    {
        this.color_code_second = color_code_second;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getColor_code ()
    {
        return color_code;
    }

    public void setColor_code (String color_code)
    {
        this.color_code = color_code;
    }

    public String getFax_no ()
    {
        return fax_no;
    }

    public void setFax_no (String fax_no)
    {
        this.fax_no = fax_no;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [image = "+image+", address = "+address+", paid_status = "+paid_status+", po_box_no = "+po_box_no+", created_at = "+created_at+", layout = "+layout+", updated_at = "+updated_at+", phone = "+phone+", user_id = "+user_id+", company_name = "+company_name+", name = "+name+", weblink = "+weblink+", id = "+id+", position = "+position+", color_code_second = "+color_code_second+", email = "+email+", color_code = "+color_code+", fax_no = "+fax_no+"]";
    }
}
