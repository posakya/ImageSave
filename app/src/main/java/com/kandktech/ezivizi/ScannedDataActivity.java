package com.kandktech.ezivizi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class ScannedDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_data);
        TextView textView3 = findViewById(R.id.textView3);
        String password = String.valueOf(R.string.app_name);
        String decryptedMsg = "";
        try {
            decryptedMsg = AESCrypt.decrypt(password, getIntent().getExtras().getString("qrData"));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        textView3.setText(decryptedMsg);
    }
}
