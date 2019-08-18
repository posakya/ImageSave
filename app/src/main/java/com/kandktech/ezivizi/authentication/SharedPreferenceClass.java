package com.kandktech.ezivizi.authentication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceClass {

    Context context;
    SharedPreferences sp;

    public SharedPreferenceClass(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("UserDetail",Context.MODE_PRIVATE);
    }

    public void saveData(String u_id,String name,String email,String pic){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("u_id",u_id);
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("pic",pic);
        editor.apply();
    }

    public void savePhone(String phone){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("phone",phone);
        editor.apply();
    }

    public String getPhone(){
        return sp.getString("phone","");
    }



    public void saveDob(String dob){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("dob",dob);
        editor.apply();
    }

    public String getDob(){
        return sp.getString("dob","");
    }


    public void saveGender(String gender){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("gender",gender);
        editor.apply();
    }

    public String getGender(){
        return sp.getString("gender","");
    }

    public void saveAddress(String address){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("address",address);
        editor.apply();
    }

    public String getAddress(){
        return sp.getString("address","");
    }


    public void saveScanData(String scan){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("scan",scan);
        editor.apply();
    }

    public String getScanData(){
        return sp.getString("scan","");
    }


    public void saveCartData(String cart){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cart",cart);
        editor.apply();
    }

    public String getCartData(){
        return sp.getString("cart","");
    }

    public void savePlusData(String plus){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("plus",plus);
        editor.apply();
    }

    public String getPlusData(){
        return sp.getString("plus","");
    }

    public void saveSubmitData(String submit){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("submit",submit);
        editor.apply();
    }

    public String getSubmitData(){
        return sp.getString("submit","");
    }

    public void saveCheckData(String check){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("check",check);
        editor.apply();
    }

    public String getCheckData(){
        return sp.getString("check","");
    }

    public void saveTotalData(String total){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("total",total);
        editor.apply();
    }

    public String getTotalData(){
        return sp.getString("total","");
    }


    public void isLooggedIn(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLoogedIn",true);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return sp.getBoolean("isLoogedIn",false);
    }

    public String getName(){
        return sp.getString("name","");
    }

    public String getEmail(){
        return sp.getString("email","");
    }

    public String getPic(){
        return sp.getString("pic","");
    }

    public String getUid(){
        return sp.getString("u_id","");
    }

    public void setCheckOut(String checkOut){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("isCheckOut",checkOut);
        editor.apply();
    }

    public String getCheckOut(){
        return sp.getString("isCheckOut","");
    }


}
