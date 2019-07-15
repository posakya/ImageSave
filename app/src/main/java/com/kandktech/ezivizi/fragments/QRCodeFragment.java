package com.kandktech.ezivizi.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.welcome_screen.WelcomeScreenActivity;

public class QRCodeFragment extends Fragment {

    View view;
    ImageView qrImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_qrcode, container, false);
        qrImg = view.findViewById(R.id.qrImg);

        try {
            Glide.with(getActivity()).load("/storage/emulated/0/Pictures/.ezvz/"+"_image_"+WelcomeScreenActivity.deviceId+".jpg").into(qrImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
