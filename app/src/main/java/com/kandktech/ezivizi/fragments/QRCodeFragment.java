package com.kandktech.ezivizi.fragments;



import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.welcome_screen.WelcomeScreenActivity;

import java.io.File;
import java.lang.reflect.Method;

import static android.content.Context.MODE_PRIVATE;

public class QRCodeFragment extends Fragment {

    View view;
    ImageView qrImg;
    FloatingActionButton shareImg;
    SharedPreferences  sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        qrImg = view.findViewById(R.id.qrImg);
        shareImg = view.findViewById(R.id.shareImg);
        sharedPreferences = getActivity().getSharedPreferences("qrImage",MODE_PRIVATE);
        try {

            Glide.with(getActivity()).load("/storage/emulated/0/Pictures/.ezvz/"+"_image_"+sharedPreferences.getLong("imgName",0)+".jpg").into(qrImg);

        } catch (Exception e) {
            e.printStackTrace();
        }


        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDefaultShareIntent();
            }
        });

        return view;
    }

    private Intent getDefaultShareIntent(){

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        File f=new File("/storage/emulated/0/Pictures/.ezvz/"+"_image_"+sharedPreferences.getLong("imgName",0)+".jpg");
        Uri screenshotUri = Uri.fromFile(f);

        //Create an intent to send any type of image
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_TEXT,"Hey please check this application " + "https://play.google.com/store/apps/details?id="+"com.app.half.waiter");
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        return sharingIntent;
    }

}
