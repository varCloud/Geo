package com.example.rexv666480.verificadores.Adapters;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rexv666480.verificadores.Entidades.Ruta;
import com.example.rexv666480.verificadores.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rexv666480 on 13/12/2017.
 */

public class AdapterRutas extends RecyclerView.Adapter<AdapterRutas.PersonViewHolder> {

    private List<Ruta> rutas;

    public AdapterRutas(List<Ruta> rutas) {
        this.rutas = rutas;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        ImageView iconUbicacion;
        TextView descripcionRuta;
        ImageView iconSiguiente;

        public PersonViewHolder(View itemView) {
            super(itemView);
            iconUbicacion = (ImageView)itemView.findViewById(R.id.iconUbicacion);
            descripcionRuta = (TextView)itemView.findViewById(R.id.descripcionRuta);
            //iconSiguiente = (ImageView)itemView.findViewById(R.id.iconSig);
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_rutas, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {

        //holder.iconUbicacion.Im(rutas.get(position).getId());
        holder.descripcionRuta.setText(rutas.get(position).getDescripcionRuta());
        //holder.iconSiguiente.setImageResource(rutas.get(position).photoId);

    }

    @Override
    public int getItemCount() {
        return this.rutas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
