package com.example.rexv666480.verificadores.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rexv666480.verificadores.Entidades.Ruta;
import com.example.rexv666480.verificadores.Entidades.Visita;
import com.example.rexv666480.verificadores.R;

import java.util.List;

/**
 * Created by rexv666480 on 05/01/2018.
 */

public class AdapterVisitas extends ArrayAdapter<Visita> {

    public AdapterVisitas(Context context, List<Visita> visitas) {
        super(context, 0, visitas);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Visita visita = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_rutas, parent, false);
        }
        // Lookup view for data population
        TextView tvDescRuta = (TextView) convertView.findViewById(R.id.descripcionRuta);
        ImageView imgUbicacion = (ImageView) convertView.findViewById(R.id.iconUbicacion);
        ImageView imgSig = (ImageView) convertView.findViewById(R.id.iconSig);
        // Populate the data into the template view using the data object
        tvDescRuta.setText(visita.getDescripcionOrigen() +" -- "+ visita.getDescripcionDestino());
        // Return the completed view to render on screen
        return convertView;
    }
}
