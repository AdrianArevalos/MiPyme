package inge2.mipyme;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import objetos.Listado;
import objetos.Producto;

public class ActividadProducto extends AppCompatActivity implements View.OnClickListener {

    private Spinner p_spinner;
    private Spinner p_cantidad;
    private Button p_registrar;
    private Button p_siguiente;
    private List<Producto> productos;
    private TextView p_precio_unitario;
    private TextView p_total;
    private List cantidades;
    private List<Listado> cliente_productos = new ArrayList<Listado>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        p_spinner = (Spinner)findViewById(R.id.p_spinner);
        p_precio_unitario = (TextView)findViewById(R.id.precio_unitario);
        p_cantidad = (Spinner)findViewById(R.id.cantidad);
        p_total = (TextView)findViewById(R.id.total);
        p_registrar = (Button)findViewById(R.id.p_registrar);
        p_registrar.setOnClickListener(this);
        p_siguiente = (Button)findViewById(R.id.p_siguiente);
        p_siguiente.setOnClickListener(this);

        productos = cargar_lista_productos();
        List<String> productos1 = new ArrayList<String>();
        for(int i = 0; i < productos.size(); i++){
            productos1.add(productos.get(i).getDescripcion());
        }

        //Spinner para los productos//
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, productos1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        p_spinner.setAdapter(arrayAdapter1);

        cantidades = carlgar_lista_numeros();
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, cantidades);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        p_cantidad.setAdapter(arrayAdapter2);

        p_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActividadProducto.this, String.valueOf(p_spinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
                p_precio_unitario.setText(String.valueOf(productos.get(p_spinner.getSelectedItemPosition()).getPrecio_unitario())+" Gs");
                int precio = productos.get(p_spinner.getSelectedItemPosition()).getPrecio_unitario();
                int cantidad = (int) cantidades.get(p_cantidad.getSelectedItemPosition());
                p_total.setText(String.valueOf(precio*cantidad)+" Gs");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Spinner para los productos//

        //Spinner para la cantidad//
        p_cantidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int precio = productos.get(p_spinner.getSelectedItemPosition()).getPrecio_unitario();
                int cantidad = (int) cantidades.get(p_cantidad.getSelectedItemPosition());
                p_total.setText(String.valueOf(precio*cantidad)+" Gs");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List carlgar_lista_numeros() {
        List numeros = new ArrayList();
        for(int i = 1; i <= 50; i++){
            numeros.add(i);
        }
        return numeros;
    }

    private List<Producto> cargar_lista_productos() {
        List<Producto> productos = new ArrayList<Producto>();
        Persistencia persistencia = new Persistencia(this,"Administrador",null,1);
        SQLiteDatabase db = persistencia.getReadableDatabase();

        Cursor fila = db.rawQuery("select * from Producto",null);
        if(fila.moveToFirst()){
            do{
                int id_produtco = fila.getInt(0);
                String descripcion = fila.getString(1);
                int precio_unitario = fila.getInt(2);
                productos.add(new Producto(id_produtco,descripcion,precio_unitario));
            }while(fila.moveToNext());
        }
        return productos;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.p_registrar:

                int id = productos.get(p_spinner.getSelectedItemPosition()).getId_producto();
                int cantidad = (int) cantidades.get(p_cantidad.getSelectedItemPosition());
                String descripcion = productos.get(p_spinner.getSelectedItemPosition()).getDescripcion();
                int precio_unitario = productos.get(p_spinner.getSelectedItemPosition()).getPrecio_unitario();

                if(controlarProductoRepetido(id)){
                    for(int i = 0; i < cliente_productos.size(); i++){
                        if(cliente_productos.get(i).getId_producto() == id){
                            cliente_productos.get(i).setCantidad(cantidad);
                        }
                    }
                }else{
                    cliente_productos.add(new Listado(id, cantidad, descripcion, precio_unitario));
                }
                Toast.makeText(this, String.valueOf("Se registro el Producto: "+ id + ", "+cantidad),Toast.LENGTH_SHORT).show();
                break;

            case R.id.p_siguiente:
                if(cliente_productos.size() == 0){
                    Toast.makeText(ActividadProducto.this,"No se registro ningun producto", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(this, ActividadListarProductos.class);

                    Bundle bundle = getIntent().getExtras();
                    int cedula_cliente = bundle.getInt("CedulaCliente");
                    intent.putParcelableArrayListExtra("Listado", (ArrayList<? extends Parcelable>) cliente_productos);
                    intent.putExtra("CedulaCliente",cedula_cliente);

                    startActivity(intent);
                }
                break;

            default:
                break;

        }
    }

    private boolean controlarProductoRepetido(int id) {
        for(int i = 0; i < cliente_productos.size(); i++){
            if(cliente_productos.get(i).getId_producto() == id){
                return true;
            }
        }
        return false;
    }
}
