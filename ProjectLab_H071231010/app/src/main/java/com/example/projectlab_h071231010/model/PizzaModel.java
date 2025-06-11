package com.example.projectlab_h071231010.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PizzaModel implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("linkImagen")
    private String linkImagen;
    @SerializedName("precio")
    private int precio;
    @SerializedName("tasaIva")
    private String tasaIva;
    private int quantity = 1;

    public PizzaModel() {}

    protected PizzaModel(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        linkImagen = in.readString();
        precio = in.readInt();
        tasaIva = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<PizzaModel> CREATOR = new Creator<PizzaModel>() {
        @Override
        public PizzaModel createFromParcel(Parcel in) {
            return new PizzaModel(in);
        }

        @Override
        public PizzaModel[] newArray(int size) {
            return new PizzaModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeString(linkImagen);
        parcel.writeInt(precio);
        parcel.writeString(tasaIva);
        parcel.writeInt(quantity);
    }

    // --- Getter & Setter ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getLinkImagen() { return linkImagen; }
    public void setLinkImagen(String linkImagen) { this.linkImagen = linkImagen; }

    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }

    public String getTasaIva() { return tasaIva; }
    public void setTasaIva(String tasaIva) { this.tasaIva = tasaIva; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getTotalPrice() {
        return precio * quantity;
    }
}