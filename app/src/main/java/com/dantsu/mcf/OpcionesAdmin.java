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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

//import com.example.mcf.R;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import  com.dantsu.mcf.R;
import com.dantsu.mcf.async.AsyncEscPosPrint;
import com.dantsu.mcf.async.AsyncEscPosPrinter;
import com.dantsu.mcf.async.AsyncUsbEscPosPrint;

public class OpcionesAdmin extends AppCompatActivity {

    Button btn_edicion,btn_mostra_clientes,btn_mostrar_pedidos,btn_cerrar_sesion,btn_balance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_opciones_admin);


        btn_edicion = findViewById(R.id.btn_edicion_opcion);
        btn_mostra_clientes = findViewById(R.id.btn_mostrar_clientes_opcion);
        btn_mostrar_pedidos = findViewById(R.id.btn_mostrar_pedidos_opcion);
        btn_cerrar_sesion = findViewById(R.id.btn_cerrar_sesion);
        btn_balance = findViewById(R.id.btn_balance);


        Intent intent = getIntent();
        boolean superUser = intent.getBooleanExtra("superUserStatus",false);




        btn_edicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesAdmin.this, MainActivity.class);
                //intent.putExtra("superUserStatus",superUser);
                startActivity(intent);
            }
        });

        btn_mostra_clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesAdmin.this, mostrarClientes.class);
                startActivity(intent);
            }
        });

        btn_mostrar_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesAdmin.this, MostrarPedidos.class);
                startActivity(intent);
            }
        });

        btn_cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesAdmin.this, Loggin.class);
                startActivity(intent);
            }
        });

        btn_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesAdmin.this, Balances.class);
                startActivity(intent);
            }
        });



    }





}
