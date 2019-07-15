package com.kandktech.ezivizi.model_class;



public class SavedUserDetailModelClass {
    String name;
    String email;
    String phone;
    String web;
    String address;
    String position;
    String device_id;
    String user_logo;
    String colorCode;
    String used_layout;

    public SavedUserDetailModelClass() {
    }

    @Override
    public String toString() {
        return "SavedUserDetailModelClass{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", web='" + web + '\'' +
                ", address='" + address + '\'' +
                ", position='" + position + '\'' +
                ", device_id='" + device_id + '\'' +
                ", user_logo='" + user_logo + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", used_layout='" + used_layout + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getUsed_layout() {
        return used_layout;
    }

    public void setUsed_layout(String used_layout) {
        this.used_layout = used_layout;
    }

    public SavedUserDetailModelClass(String name, String email, String phone, String web, String address, String position, String device_id, String user_logo, String colorCode, String used_layout) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.web = web;
        this.address = address;
        this.position = position;
        this.device_id = device_id;
        this.user_logo = user_logo;
        this.colorCode = colorCode;
        this.used_layout = used_layout;
    }
}
