package com.example.projectlab_h071231010.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.projectlab_h071231010.model.PizzaModel;

import java.util.ArrayList;
import java.util.List;

public class DbConfig extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database-pizza.db";
    private static final int DATABASE_VERSION = 2; // bumped for quantity column

    public static final String TABLE_NAME = "pizza";
    public static final String COLUMN_ID = "pizza_id";
    public static final String COLUMN_NAME = "nombre";
    public static final String COLUMN_DESC = "descripcion";
    public static final String COLUMN_IMAGE = "linkImagen";
    public static final String COLUMN_PRICE = "precio";
    public static final String COLUMN_IVA = "tasaIva";
    public static final String COLUMN_QTY = "quantity"; // new

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                    + COLUMN_ID + " TEXT PRIMARY KEY, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_DESC + " TEXT, "
                    + COLUMN_IMAGE + " TEXT, "
                    + COLUMN_PRICE + " INTEGER, "
                    + COLUMN_IVA + " TEXT, "
                    + COLUMN_QTY + " INTEGER DEFAULT 1"
                    + ");";

    public DbConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME +
                    " ADD COLUMN " + COLUMN_QTY + " INTEGER DEFAULT 1");
        }
    }

    public boolean insertOrder(PizzaModel pizzaModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(pizzaModel);
        long row = db.insertWithOnConflict(
                TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
        db.close();
        return row != -1;
    }

    public void deleteOrder(String pizzaId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{pizzaId});
        db.close();
    }

    public void deleteAllOrders() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public boolean isOrder(String pizzaId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_ID + " = ?",
                new String[]{pizzaId},
                null, null, null
        );
        boolean exists = (c != null && c.getCount() > 0);
        if (c != null) c.close();
        db.close();
        return exists;
    }

    public List<PizzaModel> getAllOrder() {
        List<PizzaModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                PizzaModel pizza = new PizzaModel();
                pizza.setId(c.getString(c.getColumnIndexOrThrow(COLUMN_ID)));
                pizza.setNombre(c.getString(c.getColumnIndexOrThrow(COLUMN_NAME)));
                pizza.setDescripcion(c.getString(c.getColumnIndexOrThrow(COLUMN_DESC)));
                pizza.setLinkImagen(c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE)));
                pizza.setPrecio(c.getInt(c.getColumnIndexOrThrow(COLUMN_PRICE)));
                pizza.setTasaIva(c.getString(c.getColumnIndexOrThrow(COLUMN_IVA)));
                pizza.setQuantity(c.getInt(c.getColumnIndexOrThrow(COLUMN_QTY)));
                list.add(pizza);
            } while (c.moveToNext());
            c.close();
        }
        db.close();
        return list;
    }

    public void updateQuantity(String id, int newQty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_QTY, newQty);
        db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{id});
        db.close();
    }

    private ContentValues toContentValues(PizzaModel pizzaModel) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, pizzaModel.getId());
        values.put(COLUMN_NAME, pizzaModel.getNombre());
        values.put(COLUMN_DESC, pizzaModel.getDescripcion());
        values.put(COLUMN_IMAGE, pizzaModel.getLinkImagen());
        values.put(COLUMN_PRICE, pizzaModel.getPrecio());
        values.put(COLUMN_IVA, pizzaModel.getTasaIva());
        values.put(COLUMN_QTY, pizzaModel.getQuantity());
        return values;
    }
}
