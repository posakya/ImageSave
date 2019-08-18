package com.kandktech.ezivizi.progressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.validation.Validation;


public class ShowProgress {

    Context context;
    ProgressDialog pDialog;

    public ShowProgress(Context context) {
        this.context = context;
    }

    /*
        show progress dialog
     */
    public void showProgress(){

        pDialog = ProgressDialog.show(new ContextThemeWrapper(context, R.style.NewDialog),"", Validation.pleaseWait,true);

        pDialog.show();

    }

    /*
        hide progress dialog

     */

    public void hideProgress(){
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
