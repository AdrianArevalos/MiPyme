package inge2.mipyme;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_usuario, et_contraseña;
    private Button btn_sesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_usuario = (EditText)findViewById(R.id.usuario);
        et_contraseña = (EditText)findViewById(R.id.contraseña);
        btn_sesion = (Button)findViewById(R.id.btn_sesion);
        btn_sesion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_sesion){
            if(controlar_login()){
                Intent intent = new Intent(this, ActividadCliente.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Credenciales incorrectos", Toast.LENGTH_SHORT).show();
            }
            et_usuario.setText("");
            et_contraseña.setText("");
        }
    }

    private boolean controlar_login() {
        Persistencia persistencia = new Persistencia(this, "Administrador", null,1);
        SQLiteDatabase db = persistencia.getWritableDatabase();

        String usuario = et_usuario.getText().toString();
        String contraseña = et_contraseña.getText().toString();

        Cursor fila = db.rawQuery("select usuario, contraseña from Usuario " +
                "where usuario = '"+usuario+ "' and contraseña = '"+contraseña+"'",null);
        if(fila.moveToFirst()){
            db.close();
            return true;
        }else{
            db.close();
            return false;
        }
    }
}
