package com.example.rexv666480.verificadores.Utilerias;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by rexv666480 on 19/12/2017.
 */

public class Loading {
    private ProgressDialog progressDialog;

    public Loading(Context c) {
        if(progressDialog == null)
            progressDialog = new ProgressDialog(c);

    }

    public  void ShowLoading( String  mensaje)
    {
        progressDialog.setMessage(mensaje); // Setting Message
        progressDialog.setTitle("GEO"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    public  void CerrarLoading()
    {
        progressDialog.dismiss();
    }
}
