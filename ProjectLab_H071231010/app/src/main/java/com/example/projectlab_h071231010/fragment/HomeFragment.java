package com.example.projectlab_h071231010.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectlab_h071231010.R;
import com.example.projectlab_h071231010.adapter.PizzaAdapter;
import com.example.projectlab_h071231010.api.ApiConfig;
import com.example.projectlab_h071231010.api.ApiService;
import com.example.projectlab_h071231010.model.PizzaModel;
import com.example.projectlab_h071231010.model.PizzaResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View layoutError;
    private TextView tvErrorMessage;
    private Button btnRetry;
    private ImageButton btnRefresh;
    private SearchView searchView;

    private PizzaAdapter pizzaAdapter;
    private List<PizzaModel> pizzaList = new ArrayList<>();

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        progressBar = view.findViewById(R.id.progressBar);
        layoutError = view.findViewById(R.id.layoutError);
        tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        btnRetry = view.findViewById(R.id.btnRetry);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        searchView = view.findViewById(R.id.searchView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pizzaAdapter = new PizzaAdapter(pizzaList, getContext());
        recyclerView.setAdapter(pizzaAdapter);

        // API service
        apiService = ApiConfig.getClient().create(ApiService.class);

        // Listener tombol retry dan refresh
        btnRetry.setOnClickListener(v -> loadPizza());
        btnRefresh.setOnClickListener(v -> loadPizza());

        // Setup search filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // Load data
        loadPizza();
    }

    private void loadPizza() {
        progressBar.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);

        apiService.getPizza().enqueue(new Callback<PizzaResponse>() {
            @Override
            public void onResponse(Call<PizzaResponse> call, Response<PizzaResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getResult() != null && !response.body().getResult().isEmpty()) {
                    pizzaList.clear();
                    pizzaList.addAll(response.body().getResult());
                    pizzaAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    showLoadError("Gagal memuat data makanan");
                }
            }

            @Override
            public void onFailure(Call<PizzaResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showLoadError("Periksa koneksi internet Anda");
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoadError(String message) {
        tvErrorMessage.setText(message);
        layoutError.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
    }

    private void filter(String keyword) {
        List<PizzaModel> filtered = new ArrayList<>();
        for (PizzaModel item : pizzaList) {
            if (item.getNombre().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(item);
            }
        }
        if (filtered.isEmpty()) {
            tvErrorMessage.setText("Tidak ada menu yang cocok");
            layoutError.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutError.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            pizzaAdapter = new PizzaAdapter(filtered, getContext());
            recyclerView.setAdapter(pizzaAdapter);
        }
    }
}