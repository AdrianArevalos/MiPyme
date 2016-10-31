package inge2.mipyme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import objetos.*;


/**
 * Created by mundo on 21/10/2016.
 */

public class Persistencia extends SQLiteOpenHelper {

    public Persistencia(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version){
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Usuario(usuario text, contraseña text)");

        db.execSQL("create table Cliente(cedula integer primary key, nombre text, apellido text, direccion text, telefono text)");

        db.execSQL("create table Producto(id_producto integer primary key, descripcion text, precio_unitario integer)");

        db.execSQL("create table Pedido(id_pedido integer primary key, cedula_cliente integer, monto_total integer, " +
                "foreign key(cedula_cliente) references Cliente(cedula))");

        db.execSQL("create table DetallePedido(id_pedido integer, id_producto integer, cantidad integer, " +
                "primary key(id_pedido, id_producto), foreign key(id_pedido) references Pedido(id_pedido)" +
                "foreign key(id_producto) references Producto(id_producto))");

        cargar_usuarios(db);
        cargar_clientes(db);
        cargar_productos(db);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Usuario");
        db.execSQL("create table Usuario(usuario text, contraseña text)");

    }

    private void cargar_productos(SQLiteDatabase db) {
        List<Producto> productos = new ArrayList<Producto>();
        Producto pro1 = new Producto(1,"Coca Cola 500cc", 5000);
        Producto pro2 = new Producto(2,"Yerba Kurupi 500g", 10000);
        Producto pro3 = new Producto(3,"Arroz Primicia 1Kg", 6000);
        productos.add(pro1);
        productos.add(pro2);
        productos.add(pro3);
        productos.add(new Producto(4,"Pepsi 2L",10000));
        productos.add(new Producto(5,"Coca Cola 2L",10000));
        productos.add(new Producto(6,"Nescafe 500g", 20000));
        productos.add(new Producto(7,"Pringles 200g", 12000));

        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            for(int i = 0; i < productos.size(); i++){
                values.put("id_producto",productos.get(i).getId_producto());
                values.put("descripcion", productos.get(i).getDescripcion());
                values.put("precio_unitario", productos.get(i).getPrecio_unitario());
                db.insert("Producto",null,values);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    private void cargar_clientes(SQLiteDatabase db) {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        Cliente cli1 = new Cliente(1234567,"Juan","Perez","Azara 2300 c/ Iturbe","021442244");
        Cliente cli2 = new Cliente(7654321,"Maria","Ramos","Colon 320 c/ Azara","021123456");
        Cliente cli3 = new Cliente(3214567,"Luis","Lopez","Mcal Lopez 1234","0981234567");
        clientes.add(cli1);
        clientes.add(cli2);
        clientes.add(cli3);
        clientes.add(new Cliente(4364746,"Bryan","Arevalos","Mcal Lopez 999 c/ Del Carmen","0981234345"));
        clientes.add(new Cliente(7168577,"Andres","Vega","Iturbe 122 c/ Herrera","0985984081"));

        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            for(int i = 0; i < clientes.size(); i++){
                values.put("cedula", clientes.get(i).getCedula());
                values.put("nombre", clientes.get(i).getNombre());
                values.put("apellido", clientes.get(i).getApellido());
                values.put("direccion", clientes.get(i).getDireccion());
                values.put("telefono", clientes.get(i).getTelefono());
                db.insert("Cliente",null,values);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    private void cargar_usuarios(SQLiteDatabase db) {
        Usuario usu = new Usuario("admin","admin");
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put("usuario", usu.getUsuario());
            values.put("contraseña", usu.getContraseña());
            db.insert("Usuario",null,values);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

}
