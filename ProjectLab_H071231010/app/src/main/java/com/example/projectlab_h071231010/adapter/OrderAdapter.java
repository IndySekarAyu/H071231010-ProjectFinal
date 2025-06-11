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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectlab_h071231010.DetailActivity;
import com.example.projectlab_h071231010.R;
import com.example.projectlab_h071231010.model.PizzaModel;
import com.example.projectlab_h071231010.sqlite.DbConfig;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public interface OnQuantityChangeListener {
        void onQuantityChanged(List<PizzaModel> updatedList);
    }

    private final FragmentManager fragmentManager;
    private List<PizzaModel> pizzaList;
    private final Context context;
    private final DbConfig dbConfig;
    private final OnQuantityChangeListener qtyListener;

    public OrderAdapter(FragmentManager fragmentManager,
                        List<PizzaModel> pizzaList,
                        Context context,
                        OnQuantityChangeListener qtyListener) {
        this.fragmentManager = fragmentManager;
        this.pizzaList = pizzaList;
        this.context = context;
        this.dbConfig = new DbConfig(context);
        this.qtyListener = qtyListener;
    }

    public List<PizzaModel> getPizzaList() {
        return pizzaList;
    }

    public void setPizzaList(List<PizzaModel> pizzaList) {
        this.pizzaList = pizzaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pesanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PizzaModel pizza = pizzaList.get(position);
        holder.bind(pizza);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("pizzaModel", pizza);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            dbConfig.deleteOrder(pizza.getId());
            pizzaList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pizzaList.size());
            qtyListener.onQuantityChanged(pizzaList);
        });

        holder.btnPlus.setOnClickListener(v -> {
            int qty = pizza.getQuantity() + 1;
            pizza.setQuantity(qty);
            dbConfig.updateQuantity(pizza.getId(), qty);
            holder.tvQuantity.setText(String.valueOf(qty));
            holder.tvTotalHarga.setText("Rp " + pizza.getTotalPrice());
            qtyListener.onQuantityChanged(pizzaList);
        });

        holder.btnMinus.setOnClickListener(v -> {
            int qty = pizza.getQuantity();
            if (qty > 1) {
                qty--;
                pizza.setQuantity(qty);
                dbConfig.updateQuantity(pizza.getId(), qty);
                holder.tvQuantity.setText(String.valueOf(qty));
                holder.tvTotalHarga.setText("Rp " + pizza.getTotalPrice());
                qtyListener.onQuantityChanged(pizzaList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pizzaList != null ? pizzaList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPizza;
        final TextView tvNamaPizza, tvHargaSatuan;
        final ImageButton btnDelete;
        final MaterialButton btnPlus, btnMinus;
        final TextView tvQuantity, tvTotalHarga;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPizza      = itemView.findViewById(R.id.img_pizza_pesanan);
            tvNamaPizza   = itemView.findViewById(R.id.tv_nama_pizza_pesanan);
            tvHargaSatuan = itemView.findViewById(R.id.tv_harga_satuan_pesanan);
            btnDelete     = itemView.findViewById(R.id.btn_hapus_pesanan);
            btnMinus      = itemView.findViewById(R.id.btn_minus_pesanan);
            btnPlus       = itemView.findViewById(R.id.btn_plus_pesanan);
            tvQuantity    = itemView.findViewById(R.id.tv_quantity_pesanan);
            tvTotalHarga  = itemView.findViewById(R.id.tv_total_harga_pesanan);
        }

        void bind(PizzaModel pizza) {
            Picasso.get()
                    .load(pizza.getLinkImagen())
                    .into(imgPizza);
            tvNamaPizza.setText(pizza.getNombre());
            tvHargaSatuan.setText("Rp " + pizza.getPrecio() + " / pcs");
            tvQuantity.setText(String.valueOf(pizza.getQuantity()));
            tvTotalHarga.setText("Rp " + pizza.getTotalPrice());
        }
    }
}
