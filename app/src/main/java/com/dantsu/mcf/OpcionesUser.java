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


import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import  com.dantsu.mcf.R;
import com.dantsu.mcf.async.AsyncEscPosPrint;
import com.dantsu.mcf.async.AsyncEscPosPrinter;
import com.dantsu.mcf.async.AsyncUsbEscPosPrint;

public class OpcionesUser extends AppCompatActivity {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    Button btn_edicion_user,btn_cerrar_sesion_user;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_opciones_user);

        btn_edicion_user = findViewById(R.id.btn_edicion_user);
        btn_cerrar_sesion_user = findViewById(R.id.btn_cerrar_sesion_user);

        btn_edicion_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesUser.this, MainActivity.class);
                //intent.putExtra("superUserStatus",Loggin.getSuperUserStatus());
                startActivity(intent);
            }
        });

        btn_cerrar_sesion_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpcionesUser.this, Loggin.class);
                //intent.putExtra("superUserStatus",Loggin.getSuperUserStatus());
                startActivity(intent);
            }
        });



    }




}
