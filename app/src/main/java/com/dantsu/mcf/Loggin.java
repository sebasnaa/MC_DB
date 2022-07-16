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
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.dantsu.mcf.async.AsyncEscPosPrint;
import com.dantsu.mcf.async.AsyncEscPosPrinter;
import com.dantsu.mcf.async.AsyncUsbEscPosPrint;


public class Loggin extends AppCompatActivity {

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static int superUserStatus = -1;
    public static boolean credencialesUsuario = false;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageView pegon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_loggin);

        usernameEditText = findViewById(R.id.activity_main_usernameEditText);
        passwordEditText = findViewById(R.id.activity_main_passwordEditText);
        loginButton = findViewById(R.id.activity_main_loginButton);

//        pegon = findViewById(R.id.gifInicio);

        String id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

// sebas 6492b67f86e8ef3a


        establecerConexonUsb();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (usernameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {
                    String toastMessage = "Usuario: " + usernameEditText.getText().toString() + ", Contraseña: " + passwordEditText.getText().toString();
                    //Por comodidad no creo base de datos para los usuarios, comrpuebo los campos y asigno 1 si es admin en caso cotontrario algo diferente a 1
                    //mcadmin    admin2022     a a
                    //mcjunior   2022

    //usernameEditText.setText(id);/

                    //&& id.equals("6492b67f86e8ef3a")

                    if(usernameEditText.getText().toString().trim().equals("a") && passwordEditText.getText().toString().trim().equals("a") ){

                        setSuperUserStatus(1);

                       // Intent intent = new Intent(Loggin.this, OpcionesAdmin.class);
                        Intent intent = new Intent(Loggin.this, OpcionesAdmin.class);
                        credencialesUsuario = true;
                        //intent.putExtra("superUserStatus",true);

                        startActivity(intent);

                    }else if(usernameEditText.getText().toString().trim().equals("j") && passwordEditText.getText().toString().trim().equals("j")){
                        Toast.makeText(getApplicationContext(), "Bunny logged", Toast.LENGTH_SHORT).show();
                        setSuperUserStatus(404);
                        credencialesUsuario = false;
                        Intent intent = new Intent(Loggin.this, OpcionesUser.class);
                        //intent.putExtra("superUserStatus",false);
                        startActivity(intent);

                    }

//                    if(!id.equals("6492b67f86e8ef3a")){
//                        pegon.setVisibility(View.VISIBLE);
//                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//
//                        new CountDownTimer(5000,1000){
//
//                            @Override
//                            public void onTick(long millisUntilFinished) {
//
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                pegon.setVisibility(View.INVISIBLE);
//
//                            }
//                        }.start();
//                    }

                } else {
                    String toastMessage = "Usuario o contraseña vacios";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public static boolean getSuperUserStatus(){

        if(Loggin.superUserStatus == 1){
            return true;
        }

        return false;

    }

    private void setSuperUserStatus(int status){
        Loggin.superUserStatus = status;
    }



    private void establecerConexonUsb(){


        UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(this);
        UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);

        if (usbConnection == null || usbManager == null) {
            new AlertDialog.Builder(this)
                    .setTitle("USB Connection")
                    .setMessage("No USB printer found.")
                    .show();
            return;
        }

        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                this,
                0,
                new Intent(Loggin.ACTION_USB_PERMISSION),
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(Loggin.ACTION_USB_PERMISSION);
        registerReceiver(this.usbReceiver, filter);
        usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);



    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Loggin.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context,
                                    new AsyncEscPosPrint.OnPrintFinished() {
                                        @Override
                                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Algo falló !");
                                        }

                                        @Override
                                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Conexión correcta !");
                                        }
                                    }
                            )
                                    .execute(getAsyncEscPosPrinter(new UsbConnection(usbManager, usbDevice)));
                        }
                        //puesto sebas
                        unregisterReceiver(usbReceiver);
                    }
                }
            }
        }
    };

    /**
     * Asynchronous printing
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 80f, 32);
        return printer.addTextToPrint(
                "[L]\n" +

                        "[C]Conexión correcta\n" +
                        "[L]\n" +
                        "[L]\n" +
                        "[L]\n" +
                        "[L]\n"

        );



    }



}
