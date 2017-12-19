package com.example.rexv666480.verificadores.Utilerias;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by rexv666480 on 19/12/2017.
 */

public class Alert {

    public static class Alerta {


        public static  void showLocationDialog(Activity activity, String mensaje) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("GEO");
            builder.setMessage(mensaje);

            String positiveText = "OK";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // positive button logic
                        }
                    });

            String negativeText = "Cancelar";
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // negative button logic
                        }
                    });

            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();
        }
    }
}
