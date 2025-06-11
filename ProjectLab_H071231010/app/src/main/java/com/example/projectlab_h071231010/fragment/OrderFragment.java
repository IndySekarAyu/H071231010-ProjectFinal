package com.example.projectlab_h071231010.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.projectlab_h071231010.R;
import com.example.projectlab_h071231010.adapter.OrderAdapter;
import com.example.projectlab_h071231010.api.ApiConfig;
import com.example.projectlab_h071231010.api.ApiService;
import com.example.projectlab_h071231010.model.PizzaModel;
import com.example.projectlab_h071231010.model.PizzaResponse;
import com.example.projectlab_h071231010.sqlite.DbConfig;
import com.example.projectlab_h071231010.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment
        implements OrderAdapter.OnQuantityChangeListener {

    private LinearLayout layoutEmpty, layoutContent;
    private Button btnBrowseMenu, btnClearAll;
    private RecyclerView recyclerView;
    private TextView tvItemCount, tvEstimatedTime;
    private OrderAdapter adapter;
    private DbConfig dbConfig;
    private ApiService service;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutEmpty     = view.findViewById(R.id.layout_empty_pesanan);
        layoutContent   = view.findViewById(R.id.layout_content_pesanan);
        btnBrowseMenu   = view.findViewById(R.id.btn_browse_menu_pesanan);
        btnClearAll     = view.findViewById(R.id.btn_clear_all_pesanan);
        recyclerView    = view.findViewById(R.id.recycler_view_pesanan);
        tvItemCount     = view.findViewById(R.id.tv_item_count_pesanan);
        tvEstimatedTime = view.findViewById(R.id.tv_estimated_time);

        fragmentManager = requireActivity().getSupportFragmentManager();
        dbConfig        = new DbConfig(requireContext());
        service         = ApiConfig.getClient().create(ApiService.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new OrderAdapter(
                fragmentManager,
                new ArrayList<>(),
                requireContext(),
                this  // <-- daftarkan listener
        );
        recyclerView.setAdapter(adapter);

        btnBrowseMenu.setOnClickListener(v ->
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, new HomeFragment())
                        .addToBackStack(null)
                        .commit()
        );

        btnClearAll.setOnClickListener(v -> {
            dbConfig.deleteAllOrders();
            adapter.getPizzaList().clear();
            adapter.notifyDataSetChanged();
            showEmpty();
        });

        loadOrder();
    }

    private void loadOrder() {
        List<PizzaModel> localOrders = dbConfig.getAllOrder();
        if (localOrders.isEmpty()) {
            showEmpty();
            return;
        }

        Map<String, Integer> qtyMap = new HashMap<>();
        for (PizzaModel p : localOrders) {
            qtyMap.put(p.getId(), p.getQuantity());
        }

        if (Utils.isNetworkAvailable(requireContext())) {
            service.getPizza().enqueue(new Callback<PizzaResponse>() {
                @Override
                public void onResponse(@NonNull Call<PizzaResponse> call,
                                       @NonNull Response<PizzaResponse> response) {
                    List<PizzaModel> displayList = new ArrayList<>();
                    if (response.isSuccessful() && response.body() != null) {
                        for (PizzaModel apiPizza : response.body().getPizza()) {
                            Integer q = qtyMap.get(apiPizza.getId());
                            if (q != null) {
                                apiPizza.setQuantity(q);
                                displayList.add(apiPizza);
                            }
                        }
                    } else {
                        displayList = localOrders;
                    }
                    updateList(displayList);
                }

                @Override
                public void onFailure(@NonNull Call<PizzaResponse> call,
                                      @NonNull Throwable t) {
                    updateList(localOrders);
                }
            });
        } else {
            updateList(localOrders);
        }
    }

    private void updateList(List<PizzaModel> list) {
        if (list == null || list.isEmpty()) {
            showEmpty();
        } else {
            adapter.setPizzaList(list);
            adapter.notifyDataSetChanged();
            recalcAndShow(list);
        }
    }

    private void recalcAndShow(List<PizzaModel> list) {
        int itemCount = list.size();
        int totalQty = 0;
        for (PizzaModel p : list) totalQty += p.getQuantity();
        showContent(itemCount, totalQty);
    }

    private void showContent(int itemCount, int totalQty) {
        layoutEmpty.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);
        btnClearAll.setVisibility(View.VISIBLE);

        tvItemCount.setText(itemCount + " Item dalam keranjang");
        int minTime = totalQty * 5;
        int maxTime = totalQty * 7;
        tvEstimatedTime.setText("⏱ " + minTime + " - " + maxTime + " menit");
    }

    private void showEmpty() {
        layoutEmpty.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);
        btnClearAll.setVisibility(View.GONE);
    }

    @Override
    public void onQuantityChanged(List<PizzaModel> updatedList) {
        recalcAndShow(updatedList);
    }
}
