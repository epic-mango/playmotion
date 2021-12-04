package com.aldeanos.playmotion.ui.explorar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aldeanos.playmotion.R;
import com.aldeanos.playmotion.databinding.CardCancionBinding;
import com.aldeanos.playmotion.databinding.FragmentLoginBinding;
import com.aldeanos.playmotion.model.Cancion;

import java.util.ArrayList;

public class CancionesAdapter extends RecyclerView.Adapter<CancionesViewHolder> {

    ArrayList<Cancion> listaCanciones;
    CancionesViewHolder.CancionesListener listener;

    public CancionesAdapter(ArrayList<Cancion> listaCanciones, CancionesViewHolder.CancionesListener listener) {
        this.listaCanciones = listaCanciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CancionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CancionesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cancion,parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CancionesViewHolder holder, int position) {
        holder.txtNombre = holder.itemView.findViewById(R.id.txtNombre);
        holder.txtArtista = holder.itemView.findViewById(R.id.txtArtista);
        holder.txtAlbum = holder.itemView.findViewById(R.id.txtAlbum);

        holder.txtNombre.setText(listaCanciones.get(position).getNombre());
        holder.txtArtista.setText(listaCanciones.get(position).getArtista());
        holder.txtAlbum.setText(listaCanciones.get(position).getAlbum());
    }

    @Override
    public int getItemCount() {
        return listaCanciones.size();
    }
}

class CancionesViewHolder extends RecyclerView.ViewHolder {

    TextView txtNombre, txtArtista, txtAlbum;

    public CancionesViewHolder(@NonNull View itemView, CancionesListener listener) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClic(getAdapterPosition());
            }
        });
    }

    interface CancionesListener {
        void onClic(int position);
    }
}