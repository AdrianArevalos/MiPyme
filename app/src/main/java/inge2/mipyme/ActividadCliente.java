package inge2.mipyme;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import objetos.Cliente;

public class ActividadCliente extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private TextView tv_direccion, tv_telefono;
    private List<Cliente> clientes;
    private Button btn_siguiente;
    private int cedula_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        spinner = (Spinner)findViewById(R.id.spinner);
        tv_direccion = (TextView)findViewById(R.id.direccion);
        tv_telefono = (TextView)findViewById(R.id.telefono);
        btn_siguiente = (Button)findViewById(R.id.btn_sigte);
        btn_siguiente.setOnClickListener(this);

        clientes = cargar_lista_clientes();
        List<String> clientes1 = new ArrayList<String>();
        for (int i = 0; i < clientes.size(); i++){
            clientes1.add(clientes.get(i).getCedula() + ", " +clientes.get(i).getNombre() + " " + clientes.get(i).getApellido());
        }

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, clientes1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActividadCliente.this, String.valueOf(spinner.getSelectedItem()), Toast.LENGTH_SHORT).show();
                tv_direccion.setText(clientes.get(spinner.getSelectedItemPosition()).getDireccion());
                tv_telefono.setText(clientes.get(spinner.getSelectedItemPosition()).getTelefono());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_sigte){
            Intent intent = new Intent(this, ActividadProducto.class);
            cedula_cliente = clientes.get(spinner.getSelectedItemPosition()).getCedula();
            intent.putExtra("CedulaCliente",cedula_cliente);
            startActivity(intent);
        }
    }

    private List<Cliente> cargar_lista_clientes() {
        List<Cliente> clientes = new ArrayList<Cliente>();
        Persistencia persistencia = new Persistencia(this,"Administrador",null,1);
        SQLiteDatabase db = persistencia.getReadableDatabase();

        Cursor fila = db.rawQuery("select * from Cliente",null);
        if(fila.moveToFirst()){
            do{
                int cedula = fila.getInt(0);
                String nombre = fila.getString(1);
                String apellido = fila.getString(2);
                String direccion = fila.getString(3);
                String telefono = fila.getString(4);
                clientes.add(new Cliente(cedula,nombre,apellido,direccion,telefono));
            }while(fila.moveToNext());
        }
        return clientes;
    }

}
