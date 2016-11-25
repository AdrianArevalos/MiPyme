package inge2.mipyme;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objetos.Listado;

public class ActividadListarProductos extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private TextView tv_total;
    private Button btn_finalizar;
    private int monto_total;
    private int cedula_cliente;
    private int id_pedido = 1;
    List<Listado> listado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_productos);

        Bundle bundle = getIntent().getExtras();
        listado = bundle.getParcelableArrayList("Listado");
        cedula_cliente = bundle.getInt("CedulaCliente");

        btn_finalizar = (Button)findViewById(R.id.btn_finalizar);
        btn_finalizar.setOnClickListener(this);


        listView = (ListView)findViewById(R.id.list_view);
        ArrayList<HashMap<String,String>> listado_productos = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        for(int i = 0; i < listado.size(); i++){
            map = new HashMap<String, String>();
            map.put("Id", String.valueOf(listado.get(i).getId_producto()));
            map.put("Descripcion", listado.get(i).getDescripcion());
            map.put("Cantidad", String.valueOf(listado.get(i).getCantidad()));
            int monto = listado.get(i).getPrecio_unitario()*listado.get(i).getCantidad();
            monto_total += monto;
            map.put("Monto", String.valueOf(monto));
            listado_productos.add(map);
        }

        tv_total = (TextView)findViewById(R.id.text_total);
        tv_total.setText(String.valueOf(monto_total)+" Gs");

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listado_productos, R.layout.activity_vista_item,
                new String[]{"Id", "Descripcion", "Cantidad", "Monto"},
                new int[]{R.id.text_id, R.id.text_descripcion, R.id.text_cantidad, R.id.text_monto});
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_finalizar){
            if(altaPedido(id_pedido)){
                actualizar_cantidad_producto();
                Toast.makeText(this,"Transaccion Finalizada",Toast.LENGTH_SHORT).show();
                id_pedido++;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                finish();
            }else{
                Toast.makeText(this,"No se completo la transaccion",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean altaPedido(int id_pedido) {
        Persistencia persistencia = new Persistencia(this, "Administrador", null, 1);
        SQLiteDatabase bd = persistencia.getWritableDatabase();
        try{
            ContentValues registro = new ContentValues();
            registro.put("id_pedido",id_pedido);
            registro.put("cedula_cliente",cedula_cliente);
            registro.put("monto_total",monto_total);
            bd.insert("Pedido",null,registro);
            bd.close();
            altaDetallePedido(id_pedido);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void altaDetallePedido(int id_pedido) {
        Persistencia persistencia = new Persistencia(this, "Administrador", null, 1);
        SQLiteDatabase bd = persistencia.getWritableDatabase();

        try{
            ContentValues registro = new ContentValues();
            for(int i = 0; i < listado.size(); i++){
                registro.put("id_pedido",id_pedido);
                registro.put("id_producto", listado.get(i).getId_producto());
                registro.put("cantidad", listado.get(i).getCantidad());
                bd.insert("DetallePedido",null,registro);
            }
        }catch (Exception e){

        }
        bd.close();
    }

    private void actualizar_cantidad_producto(){
        Persistencia persistencia = new Persistencia(this, "Administrador", null, 1);
        SQLiteDatabase bd = persistencia.getWritableDatabase();
        try{
            //traer de la bd la cantidad y hacer la diferencia con la cantidad de productos que se compro
            for(int i = 0; i < listado.size(); i++){
                Cursor fila = bd.rawQuery("select cantidad from Producto where id_producto = " + listado.get(i).getId_producto(), null);
                if(fila.moveToFirst()){
                    ContentValues registro = new ContentValues();
                    registro.put("id_producto", listado.get(i).getId_producto());
                    registro.put("descripcion", listado.get(i).getDescripcion());
                    registro.put("precio_unitario", listado.get(i).getPrecio_unitario());
                    registro.put("cantidad", (fila.getInt(0) - listado.get(i).getCantidad()) );

                    bd.update("Producto", registro, "id_producto = " + listado.get(i).getId_producto(), null);
                }
            }
            bd.close();

        }catch (Exception e){

        }
    }
}