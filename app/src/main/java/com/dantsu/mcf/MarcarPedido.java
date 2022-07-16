package com.dantsu.mcf;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.dantsu.mcf.async.AsyncEscPosPrint;
import com.dantsu.mcf.async.AsyncEscPosPrinter;
import com.dantsu.mcf.async.AsyncUsbEscPosPrint;
//import com.example.mcf.R;
import  com.dantsu.mcf.R;

import java.util.ArrayList;
import java.util.Date;

public class MarcarPedido extends AppCompatActivity {


    private double descuentoGlovo;
    private ArrayList<String> datosTicket = new ArrayList<>();


    Button botonMain,btn_confirmar_pedido;
    TextView et_nombre,et_direccion,et_telefono,et_cambio_calculado,descuentoField;
    EditText et_importe_pedido,et_entrega_dinero,importeField,entregaField,et_descuento_aplicado;
    RadioButton r_btn_tarjeta,r_btn_efectivo;
    Switch sw_glovo,sw_pagado,sw_descuento;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_marcar_pedido);

        //Botones
        botonMain = findViewById(R.id.buttonToMainPedido);
        btn_confirmar_pedido = findViewById(R.id.btn_confirmar_pedido);

        //Etiquetas
        et_nombre = findViewById(R.id.nombreClientePedido);
        et_direccion = findViewById(R.id.direccionClientePedido);
        et_telefono = findViewById(R.id.telefonoClientePedido);
        et_cambio_calculado = findViewById(R.id.et_cambio_calculado);

        //EditText
        et_importe_pedido = findViewById(R.id.et_importe_pedido);
        et_entrega_dinero = findViewById(R.id.et_entrega_dinero);
        et_descuento_aplicado = findViewById(R.id.et_descuento_aplicado);
        importeField = findViewById(R.id.importeField);
        entregaField = findViewById(R.id.entregaField);
        descuentoField = findViewById(R.id.descuentoField);

        descuentoField.setVisibility(View.INVISIBLE);
        et_descuento_aplicado.setVisibility(View.INVISIBLE);

        //RadioButton
        r_btn_tarjeta = findViewById(R.id.radio_btn_tarjeta);
        r_btn_efectivo = findViewById(R.id.radio_btn_efectivo);

        //switch
        sw_glovo = findViewById(R.id.sw_glovo);
        sw_pagado = findViewById(R.id.sw_pagado);
        sw_descuento = findViewById(R.id.sw_descuento);
        sw_pagado.setVisibility(View.INVISIBLE);
        sw_descuento.setVisibility(View.INVISIBLE);


        setDatosPedidoCliente();
        et_cambio_calculado.setKeyListener(null);
        entregaField.setKeyListener(null);
        importeField.setKeyListener(null);


        Intent intent = getIntent();
        boolean superUser = intent.getBooleanExtra("superUserStatus",false);

        sw_glovo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sw_pagado.setVisibility(View.VISIBLE);
                    sw_descuento.setVisibility(View.VISIBLE);
                }else{
                    sw_pagado.setVisibility(View.INVISIBLE);
                    sw_descuento.setVisibility(View.INVISIBLE);

                }
            }
        });

        sw_descuento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    descuentoField.setVisibility(View.VISIBLE);
                    et_descuento_aplicado.setVisibility(View.VISIBLE);
                }else{
                    descuentoField.setVisibility(View.INVISIBLE);
                    et_descuento_aplicado.setVisibility(View.INVISIBLE);
                }
            }
        });


        botonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentMod = new Intent(MarcarPedido.this,MainActivity.class);
                //intentMod.putExtra("superUserStatus",superUser);
                startActivity(intentMod);
            }
        });




        btn_confirmar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int telefono = Integer.parseInt(et_telefono.getText().toString());
                String tipo = getTipoPedido();
                double importe = Double.parseDouble(et_importe_pedido.getText().toString());
                double entregaCliente = Double.parseDouble(et_entrega_dinero.getText().toString());
                double devolver = entregaCliente -  importe;
                String metodoPago = getMetodoPago();

                ModeloPedido moPedido = new ModeloPedido(-1,telefono,tipo,importe,metodoPago,"");


                String tipoPedido = tipo.equals("G") ? "Glovo" : "Restaurante";

                String datosPedido = "Metodo pago -> " + metodoPago + "\n" +
                        "Importe -> " + importe +
                        " € \n" + "Tipo -> " + tipoPedido;



                datosTicket.add((String) et_direccion.getText());
                datosTicket.add(""+telefono);
                datosTicket.add((String) et_nombre.getText());
                datosTicket.add(""+importe);
                datosTicket.add(""+entregaCliente);
                datosTicket.add(""+devolver);
                datosTicket.add(""+tipoPedido);
                datosTicket.add(""+metodoPago);


                AlertDialog.Builder builder = new AlertDialog.Builder(MarcarPedido.this);
                builder.setMessage(datosPedido);
                builder.setTitle(getResources().getString(R.string.confirmacion_pedido));
                builder.setCancelable(false);
                builder.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.confirmar), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onClick(DialogInterface dialog, int which) {


                        DataBaseOperation dataBaseOperation = new DataBaseOperation(MarcarPedido.this);
                        boolean exitoso = dataBaseOperation.agregarPedido(moPedido);
                        boolean exitosoDescuento = true;
                        if(sw_glovo.isChecked() && sw_descuento.isChecked() && et_descuento_aplicado.getText().toString().length() > 0){
                            double importeDescuento = Double.parseDouble(et_descuento_aplicado.getText().toString());
                            exitosoDescuento = dataBaseOperation.agregarDescuentoPedido(getMetodoPago(),importeDescuento);
                        }


                        if(exitoso && exitosoDescuento){
                            Toast.makeText(MarcarPedido.this, "Pedido Agregado",
                                    Toast.LENGTH_LONG).show();


                            printUsb();


                            Intent intent = new Intent(MarcarPedido.this,MainActivity.class);
                            //intent.putExtra("superUserStatus",superUser);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MarcarPedido.this, "Error,no agregado",
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        et_importe_pedido.addTextChangedListener(textWatcher);
        et_entrega_dinero.addTextChangedListener(textWatcher);
        et_descuento_aplicado.addTextChangedListener(textWatcher);
        checkFieldsForEmptyValues();

    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MarcarPedido.ACTION_USB_PERMISSION.equals(action)) {
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
                                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                        }

                                        @Override
                                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
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


    public void printUsb() {
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
                new Intent(MarcarPedido.ACTION_USB_PERMISSION),
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(MarcarPedido.ACTION_USB_PERMISSION);
        registerReceiver(this.usbReceiver, filter);
        usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);

    }

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
                        "[R]<u><font size='big'>PEDIDO N°045</font></u>\n" +
                        "[L]\n" +
                        "[R]<u type='double'>" + format.format(new Date()) + "</u>\n" +
                        "[C]\n" +
                        "[R]================================\n" +
                        "[L]\n" +
                        "[L]<b>Dirección: </b>\n" +
                        "[L]  + "+this.datosTicket.get(0)+"\n" +
                        "[L]\n" +
                        "[L]<b>Teléfono: </b>\n" +
                        "[L]  + " + this.datosTicket.get(1) +"\n" +
                        "[L]\n" +
                        "[L]<b>Nombre: </b>\n" +
                        "[L]  + " + this.datosTicket.get(2) +"\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[C]--------------------------------\n" +
                        "[L]<b>T. pedido -> </b>  " + datosTicket.get(6) +"\n" +
                        "[L]<b>Método pago -> </b>  " + datosTicket.get(7) +"\n" +
                        "[C]--------------------------------\n" +
                        "[C]--------------------------------\n" +
                        "[R] Importe -> [R]"+ this.datosTicket.get(3) +" €\n" +
                        "[L]\n" +
                        "[R] Cliente entrega -> [R]"+ this.datosTicket.get(4) +" €\n" +
                        "[L]\n" +
                        "[R] Devolver -> [R]"+ this.datosTicket.get(5) +" €\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]\n" +
                        "[L]\n" +
                        "[L]\n"

        );



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBackPressed() {
        closeKeyBoard();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void closeKeyBoard(){
        View view = this.getCurrentFocus();

        if (view != null) {

            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }


    public ModeloCliente setDatosPedidoCliente(){
        ModeloCliente cliente = new ModeloCliente();
        Intent intent = getIntent();
        String telefono = intent.getStringExtra("telefonoCliente");
        DataBaseOperation db = new DataBaseOperation(MarcarPedido.this);
        ModeloCliente aux = db.getCliente(telefono);

        et_nombre.setText(aux.getNombre());
        et_direccion.setText(aux.getDireccion());
        et_telefono.setText(telefono);
        return cliente;
    }

    private String getMetodoPago(){


        if(sw_pagado.isChecked()){
            return "App";
        }

        if(r_btn_tarjeta.isChecked()){
            return r_btn_tarjeta.getText().toString();
        }



        return r_btn_efectivo.getText().toString();

    }


    private String getTipoPedido(){
        if(sw_glovo.isChecked()){
            return "G";
        }else{
            return "R";
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            if(!sw_descuento.isChecked()){
                descuentoGlovo = 0;

            }
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
        Button btn = (Button) findViewById(R.id.btn_confirmar_pedido);

        if(et_importe_pedido.getText().toString().length() > 0 &&  et_entrega_dinero.getText().toString().length() > 0){
            Double precio = Double.parseDouble(et_importe_pedido.getText().toString());
            Double entregaDinero = Double.parseDouble(et_entrega_dinero.getText().toString());
            setDescuento();
            if (precio > 0 && (precio <= entregaDinero+descuentoGlovo)) {
                double cambio = entregaDinero+descuentoGlovo-precio;
                et_cambio_calculado.setText(""+cambio);
                btn.setEnabled(true);
            } else {
                et_cambio_calculado.setText("");
                btn.setEnabled(false);
            }
        }else{
            btn.setEnabled(false);
        }

    }


    private void setDescuento(){

        descuentoGlovo = 0;

        if( et_descuento_aplicado.getVisibility() == View.VISIBLE && et_descuento_aplicado.getText().toString().length() > 0){
            descuentoGlovo = Double.parseDouble(et_descuento_aplicado.getText().toString());
        }
    }










}
