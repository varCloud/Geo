package com.example.rexv666480.verificadores.Utilerias;

import android.content.Context;
import android.location.LocationManager;
import android.sax.RootElement;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by rexv666480 on 21/02/2018.
 */

public class Permisos {

    private  Context context;
    final LocationManager manager;
    private  View view;
    public Permisos(Context context , View view) {
        this.context = context;
        manager = (LocationManager)context.getSystemService    (Context.LOCATION_SERVICE );
        this.view = view;
    }

    public boolean GPSActivado()
    {
        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            return  true;
        }
        else
        {
            Snackbar.make(view, "Por favor active el GPS", Snackbar.LENGTH_LONG).show();
            return  false;
        }

    }


}
