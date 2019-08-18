package com.kandktech.ezivizi.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.kandktech.ezivizi.FirstPageActivity;
import com.kandktech.ezivizi.fragments.ListFragment;
import com.kandktech.ezivizi.fragments.QRCodeFragment;
import com.kandktech.ezivizi.fragments.QRScanFragment;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                QRScanFragment qrScanFragment = new QRScanFragment();
                FirstPageActivity.qrScan = true;
                return qrScanFragment;

            case 1:
                ListFragment listFragment = new ListFragment();
                FirstPageActivity.qrScan  = false;
                return listFragment;

            case 2:
                QRCodeFragment qrCodeFragment = new QRCodeFragment();
                FirstPageActivity.qrScan  = false;
                return qrCodeFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                FirstPageActivity.qrScan  = true;
                return "Scan";

            case 1:
                FirstPageActivity.qrScan  = false;
                return "Visiting Cards";

            case 2:
                FirstPageActivity.qrScan  = false;
                return "QRCode";

            default:
                return null;
        }
    }
}
