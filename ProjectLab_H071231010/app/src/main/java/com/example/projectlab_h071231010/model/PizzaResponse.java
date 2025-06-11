package com.example.projectlab_h071231010.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PizzaResponse {
    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("productos")
    private List<PizzaModel> productos;

    public String getPage() {
        return mensaje;
    }

    public List<PizzaModel> getResult() {
        return productos;
    }

    public List<PizzaModel> getPizza() {
        return productos;
    }
}
