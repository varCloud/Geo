package com.example.rexv666480.verificadores.Utilerias;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by victor on 19/02/18.
 */

public class NotificacionToast {

    private int duration;
    private Context context;

    public NotificacionToast(Context context) {
        duration = Toast.LENGTH_SHORT;
        this.context = context;
    }

    public void Show(String mensaje)
    {
        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }

    public void ErrorWebService(String mensaje)
    {
        Toast toast = Toast.makeText(context, "Espere un momento y vuelva a intentarlo "+mensaje, duration);
        toast.show();
    }
}
