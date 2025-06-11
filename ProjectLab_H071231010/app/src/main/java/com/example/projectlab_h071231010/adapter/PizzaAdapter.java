package com.example.projectlab_h071231010.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectlab_h071231010.DetailActivity;
import com.example.projectlab_h071231010.R;
import com.example.projectlab_h071231010.model.PizzaModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.ViewHolder> {

    private List<PizzaModel> pizzaList;
    private Context context;

    public PizzaAdapter(List<PizzaModel> pizzaList, Context context) {
        this.pizzaList = pizzaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PizzaModel pizzaModel = pizzaList.get(position);
        holder.bind(pizzaModel);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("pizzaModel", pizzaModel);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList != null ? pizzaList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMenu;
        private TextView tvNamaMenu, tvDeskripsi, tvHarga;
        private ImageButton btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            tvNamaMenu = itemView.findViewById(R.id.tvNamaMenu);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvHarga = itemView.findViewById(R.id.tvHarga);
        }

        public void bind(PizzaModel pizza) {
            Picasso.get().load(pizza.getLinkImagen()).into(imgMenu);
            tvNamaMenu.setText(pizza.getNombre());
            tvDeskripsi.setText(pizza.getDescripcion());
            tvHarga.setText("Rp " + pizza.getPrecio());
        }
    }
}
