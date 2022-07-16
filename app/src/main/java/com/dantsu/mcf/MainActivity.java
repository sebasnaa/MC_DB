package com.dantsu.mcf;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.mcf.R;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import  com.dantsu.mcf.R;
import com.dantsu.mcf.async.AsyncEscPosPrint;
import com.dantsu.mcf.async.AsyncEscPosPrinter;
import com.dantsu.mcf.async.AsyncUsbEscPosPrint;

import java.util.Date;

public class MainActivity extends AppCompatActivity {



    Button btn_agregar,btn_buscar,btn_eliminar,btn_mostrar_clientes,btn_marcar_pedido,btn_home_user,btn_home_admin;
    EditText et_nombre,et_direccion,et_telefono;



    private static final String TAG = "Main";
    private boolean superUserStatus = false;

    private boolean getUserStatus() {
        Intent intent = getIntent();
        boolean status = intent.getBooleanExtra("superUserStatus",false);
        return  status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        superUserStatus = getUserStatus();

        superUserStatus = Loggin.credencialesUsuario;

        btn_marcar_pedido = findViewById(R.id.btn_marcar_pedido);

        btn_agregar = findViewById(R.id.btn_agregar);
        btn_buscar = findViewById(R.id.btn_buscar);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        btn_home_user = findViewById(R.id.btn_home_user);
        btn_home_admin = findViewById(R.id.btn_home_admin);
        et_nombre = findViewById(R.id.NombreCliente);
        et_direccion = findViewById(R.id.DireccionCliente);
        et_telefono = findViewById(R.id.TelefonoCliente);


        if(!superUserStatus){
            //Toast.makeText(getApplicationContext(), "Junior", Toast.LENGTH_SHORT).show();
            btn_eliminar.setVisibility(View.GONE);
            btn_home_admin.setVisibility(View.GONE);
        }

        if(superUserStatus){
            btn_home_user.setVisibility(View.GONE);
        }




        btn_marcar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarcarPedido.class);
                intent.putExtra("nombreCliente",et_nombre.getText().toString());
                intent.putExtra("direccionCliente",et_direccion.getText().toString());
                intent.putExtra("telefonoCliente",et_telefono.getText().toString());
                //intent.putExtra("superUserStatus",superUserStatus);

                startActivity(intent);
            }
        });

        btn_home_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpcionesUser.class);
                startActivity(intent);
            }
        });

        btn_home_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpcionesAdmin.class);
                startActivity(intent);
            }
        });


        btn_agregar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ModeloCliente modeloCliente = null;

                String mensajeError = "Faltan Datos";
                try{

                    //Datos correctos
                    if(et_nombre.getText().toString().length() > 0 && et_direccion.getText().toString().length() > 0 && et_telefono.getText().toString().length() > 0  ){
                        String nombre = et_nombre.getText().toString();
                        String dir = et_direccion.getText().toString();
                        int tel = Integer.parseInt(et_telefono.getText().toString());
                        if(Integer.toString(tel).length() < 9 && Integer.toString(tel).length() > 0){
                            mensajeError+=", Teléfono Incorrecto";
                        }else{

                            modeloCliente= new ModeloCliente(-1,nombre,dir,tel);
                            DataBaseOperation dataBaseOperation = new DataBaseOperation(MainActivity.this);
                            boolean exitoso = dataBaseOperation.agregarCliente(modeloCliente);
                            String mensaje ="Cliente existente";
                            if(exitoso){
                                mensaje = "Cliente guardado";
                                //limpiar();
                            }
                            Toast.makeText(MainActivity.this,mensaje ,Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(et_nombre.getText().toString().length() < 1 || et_direccion.getText().toString().length() < 1 || et_telefono.getText().toString().length() < 1 ){
                        Toast.makeText(MainActivity.this,"Faltan datos" ,Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e){
                    Toast.makeText(MainActivity.this,"Error creando cliente "+ e.getMessage() ,Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_buscar.setOnClickListener(new View.OnClickListener() {

            ModeloCliente modeloCliente;
            public void onClick(View v) {

                DataBaseOperation dataBaseOperation = new DataBaseOperation(MainActivity.this);
                String telefono = et_telefono.getText().toString();
                modeloCliente = dataBaseOperation.getCliente(telefono);
                if(modeloCliente != null){

//                    Toast.makeText(MainActivity.this,modeloCliente.toString() ,Toast.LENGTH_SHORT).show();
                    et_nombre.setText(modeloCliente.getNombre());
                    et_direccion.setText(modeloCliente.getDireccion());
                    String aux = ""+modeloCliente.getTelefono();
                    et_telefono.setText(aux);
                }else if(modeloCliente == null){
                    Toast.makeText(MainActivity.this,"No existe, compruebe teléfono" ,Toast.LENGTH_SHORT).show();
                }


            }
        });


        btn_eliminar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                DataBaseOperation dataBaseOperation = new DataBaseOperation(MainActivity.this);
                String telefono = et_telefono.getText().toString();
                boolean salida = dataBaseOperation.eliminarCliente(telefono);
                Toast.makeText(MainActivity.this,"Cliente eliminado" ,Toast.LENGTH_SHORT).show();
                if(salida){
                    limpiar();
                }




            }
        });



        et_nombre.addTextChangedListener(textWatcher);
        et_direccion.addTextChangedListener(textWatcher);
        et_telefono.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();





    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBackPressed() {
        //muere el boton back
        closeKeyBoard();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void closeKeyBoard(){
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

    public void limpiar(){
        et_nombre.setText("");
        et_telefono.setText("");
        et_direccion.setText("");
        et_telefono.setText("");
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private  void checkFieldsForEmptyValues(){
        DataBaseOperation dataBaseOperation = new DataBaseOperation(MainActivity.this);
        Button btn = (Button) findViewById(R.id.btn_marcar_pedido);

        String nombre = et_nombre.getText().toString();
        String dirr = et_direccion.getText().toString();
        String telefono = et_telefono.getText().toString();






        if (nombre.length() > 0 && dirr.length() > 0 && telefono.length() > 0) {
            ModeloCliente modeloCliente = dataBaseOperation.getCliente(telefono);
            if(modeloCliente != null){
                btn.setEnabled(true);
            }else{
                btn.setEnabled(false);
            }
        } else {
            btn.setEnabled(false);
        }
    }

}