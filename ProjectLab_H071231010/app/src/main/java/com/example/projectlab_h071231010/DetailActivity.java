package com.example.projectlab_h071231010;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.projectlab_h071231010.model.PizzaModel;
import com.example.projectlab_h071231010.sqlite.DbConfig;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {

    private ImageView imgDetailMenu;
    private TextView tvDetailNamaMenu, tvDetailHarga, tvDetailDeskripsi;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fabOrder;
    private DbConfig dbConfig;
    private PizzaModel pizzaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        imgDetailMenu = findViewById(R.id.imgDetailMenu);
        tvDetailNamaMenu = findViewById(R.id.tvDetailNamaMenu);
        tvDetailHarga = findViewById(R.id.tvDetailHarga);
        tvDetailDeskripsi = findViewById(R.id.tvDetailDeskripsi);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        fabOrder = findViewById(R.id.fabOrder);

        // Initialize database helper
        dbConfig = new DbConfig(this);

        // Toolbar setup
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        pizzaModel = getIntent().getParcelableExtra("pizzaModel");
        if (pizzaModel != null) {
            showPizzaDetails(pizzaModel);
            updateFabState();
            fabOrder.setOnClickListener(v -> toggleOrder());
        } else {
            Toast.makeText(this, "Data menu tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showPizzaDetails(PizzaModel pizzaModel) {
        collapsingToolbar.setTitle(pizzaModel.getNombre());
        Picasso.get().load(pizzaModel.getLinkImagen()).into(imgDetailMenu);
        tvDetailNamaMenu.setText(pizzaModel.getNombre());
        tvDetailHarga.setText("Rp " + pizzaModel.getPrecio());
        tvDetailDeskripsi.setText(pizzaModel.getDescripcion());
    }

    private void toggleOrder() {
        String id = pizzaModel.getId();
        boolean exists = dbConfig.isOrder(id);
        if (exists) {
            dbConfig.deleteOrder(id);
            Toast.makeText(this, "Pesanan dihapus dari favorit", Toast.LENGTH_SHORT).show();
        } else {
            boolean inserted = dbConfig.insertOrder(pizzaModel);
            if (inserted) {
                Toast.makeText(this, "Pesanan disimpan ke favorit", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal menyimpan pesanan", Toast.LENGTH_SHORT).show();
            }
        }
        updateFabState();
    }

    private void updateFabState() {
        boolean exists = dbConfig.isOrder(pizzaModel.getId());
        if (exists) {
            fabOrder.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.colorPrimary)
            ));
            fabOrder.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, android.R.color.white)
            ));
        } else {
            fabOrder.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.gray_inactive)
            ));
            fabOrder.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)
            ));
        }
    }
}
