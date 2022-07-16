package com.dantsu.mcf;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//import com.example.mcf.R;
import  com.dantsu.mcf.R;

public class Balances extends AppCompatActivity {



    DataBaseOperation db = new DataBaseOperation(Balances.this);

    Button btn_completo_balance,btn_inicio_balance;


    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_balance);

        btn_completo_balance = findViewById(R.id.btn_completo_balance);
        btn_inicio_balance = findViewById(R.id.btn_inicio_balance);

        textView2 = findViewById(R.id.textView2);


        btn_completo_balance.setEnabled(false);

        btn_completo_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();

            }
        });




        btn_inicio_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Balances.this,OpcionesAdmin.class);
                startActivity(intent);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();

            }
        });


        textView2.addTextChangedListener(textWatcher);


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

        if(textView2.getText().toString().equals("Fecha")){
            btn_completo_balance.setEnabled(false);
        }else{
            btn_completo_balance.setEnabled(true);
        }

    }


    private double[] getDatosRestauranteEfectivo(){
        return db.getBalanceCompletoMod(textView2.getText().toString(),3);

  /*      double datosBalance[] = new double[2];
        String fecha = textView2.getText().toString();
        datosBalance = db.getBalanceCompletoMod(fecha,3);


        return datosBalance;*/

    }

    private double[] getDatosRestauranteTarjeta(){
        return db.getBalanceCompletoMod(textView2.getText().toString(),4);


      /*  double datosBalance[] = new double[2];
        String fecha = textView2.getText().toString();
        datosBalance = db.getBalanceCompletoMod(fecha,4);

        return datosBalance;*/
    }

    private double[] getDatosGlovoTarjeta(){
        return db.getBalanceCompletoMod(textView2.getText().toString(),6);

       /* double datosBalance[] = new double[2];
        String fecha = textView2.getText().toString();
        datosBalance = db.getBalanceCompletoMod(fecha,6);



        return datosBalance;*/
    }

    private double[] getDatosGlovoEfectivo(){
        return db.getBalanceCompletoMod(textView2.getText().toString(),5);

        /*double datosBalance[] = new double[2];
        String fecha = textView2.getText().toString();
        datosBalance = db.getBalanceCompletoMod(fecha,5);



        return datosBalance;*/
    }

    private double[] getDatosGlovoApp(){
        return db.getBalanceCompletoMod(textView2.getText().toString(),7);


        /*double datosBalance[] = new double[2];
        String fecha = textView2.getText().toString();
        datosBalance = db.getBalanceCompletoMod(fecha,7);


        return datosBalance;*/
    }

    private double[] getDatosDescuentosEfectivo(){
        /*double datosBalance[] = new double[2];
        String fecha = textView2.getText().toString();
        datosBalance = db.getBalanceCompletoMod(fecha,8);


        return datosBalance;*/
        return db.getBalanceCompletoMod(textView2.getText().toString(),8);

    }

    private double[] getDatosDescuentosTarjeta(){

        return db.getBalanceCompletoMod(textView2.getText().toString(),9);
    }

    private double[] getDatosDescuentosApp(){

        return db.getBalanceCompletoMod(textView2.getText().toString(),10);
    }


    private void showDatePickerDialog() {
        /*DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");*/


        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                //final String selectedDate = day + " / " + (month+1) + " / " + year;
                final String fechaSeleccionada = year+"-"+(month+1)+"-"+day;
                textView2.setText(fechaSeleccionada);

            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void init() {






        TableLayout stk = (TableLayout) findViewById(R.id.table_balances);
        TableRow tbrow0 = new TableRow(this);
        TableRow blank = new TableRow(this);

        int count = stk.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = stk.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        tbrow0.setBackgroundColor(Color.parseColor("#2D572C"));


        /*TextView tv0 = new TextView(this);
        tv0.setText("           ");
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setPadding(0,0,25,0);
        tbrow0.addView(tv0);
*/

        TextView tv1 = new TextView(this);
        tv1.setText("           ");
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setPadding(0,0,25,0);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("Tarjeta");
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setPadding(0,0,25,0);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("Efectivo");
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setPadding(0,0,25,0);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText("App");
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setPadding(0,0,25,0);
        tbrow0.addView(tv4);

        stk.addView(tbrow0);

        DataBaseOperation db = new DataBaseOperation(Balances.this);
        int colorA = Color.parseColor("#9E9E9E");
        int colorB = Color.parseColor("#FFFFFF");

        int colorLetraBlanco = Color.parseColor("#FFFFFF");
        int colorLetraNegro = Color.parseColor("#000000");

        int colorC,colorLetra;


        //creacion de las filas
        TableRow RNV = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText("Restaurante/Nº ventas");
        t1v.setGravity(Gravity.LEFT);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);


        double datosTarjetaRestaurante[] = getDatosRestauranteTarjeta();
        double datosEfectivoRestaurante[] = getDatosRestauranteEfectivo();

        //Tarjeta
        t1v = new TextView(this);
        t1v.setText(""+(int)datosTarjetaRestaurante[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //Efectivo
        t1v = new TextView(this);
        t1v.setText(""+(int)datosEfectivoRestaurante[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //APP
       /* t1v = new TextView(this);
        t1v.setText("0");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        */

        stk.addView(RNV);


        TableRow RI = new TableRow(this);
        t1v = new TextView(this);
        t1v.setText("Restaurante/Importe");
        t1v.setGravity(Gravity.LEFT);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //Tarjeta importe
        t1v = new TextView(this);
        t1v.setText(""+datosTarjetaRestaurante[1]+" €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);



        //Efectivo importe
        t1v = new TextView(this);
        t1v.setText(""+datosEfectivoRestaurante[1] + " €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //APP importe
        /*t1v = new TextView(this);
        t1v.setText("2");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);*/


        stk.addView(RI);

        t1v = new TextView(this);
        t1v.setText("");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        blank.addView(t1v);
        stk.addView(blank);

        //Glovo aprtado

        double datosTarjetaGlovo[] = getDatosGlovoTarjeta();
        double datosEfectivoGlovo[] = getDatosGlovoEfectivo();
        double datosAppGlovo[] = getDatosGlovoApp();
        double datosDescuentosEfectivo[] = getDatosDescuentosEfectivo();
        double datosDescuentosTarjeta[] = getDatosDescuentosTarjeta();
        double datosDescuentoApp[] = getDatosDescuentosApp();

        RNV = new TableRow(this);


        t1v = new TextView(this);
        t1v.setText("Glovo/Nº ventas");
        t1v.setGravity(Gravity.LEFT);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //Tarjeta
        t1v = new TextView(this);
        t1v.setText(""+(int)datosTarjetaGlovo[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //Efectivo
        t1v = new TextView(this);
        t1v.setText(""+(int)datosEfectivoGlovo[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //APP
        t1v = new TextView(this);
        t1v.setText(""+(int)datosAppGlovo[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);

        stk.addView(RNV);


        RI = new TableRow(this);
        t1v = new TextView(this);
        t1v.setText("Glovo/Importe");
        t1v.setGravity(Gravity.LEFT);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //Tarjeta importe
        t1v = new TextView(this);
        t1v.setText(datosTarjetaGlovo[1]+" €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //Efectivo importe
        t1v = new TextView(this);
        t1v.setText(datosEfectivoGlovo[1] + " €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //APP importe
        t1v = new TextView(this);
        t1v.setText(datosAppGlovo[1]+" €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);

        stk.addView(RI);

        blank = new TableRow(this);
        t1v = new TextView(this);
        t1v.setText("");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        blank.addView(t1v);
        stk.addView(blank);


        //
        //Descuentos
        //
        RNV = new TableRow(this);


        t1v = new TextView(this);
        t1v.setText("Descuentos/Nº descuentos");
        t1v.setGravity(Gravity.LEFT);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //Tarjeta
        t1v = new TextView(this);
        t1v.setText(""+(int)datosDescuentosTarjeta[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //Efectivo
        t1v = new TextView(this);
        t1v.setText(""+(int)datosDescuentosEfectivo[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);
        //App
        t1v = new TextView(this);
        t1v.setText(""+(int)datosDescuentoApp[0]);
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RNV.addView(t1v);



        stk.addView(RNV);


        RI = new TableRow(this);
        t1v = new TextView(this);
        t1v.setText("Descuentos/Importe");
        t1v.setGravity(Gravity.LEFT);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //Tarjeta importe
        t1v = new TextView(this);
        t1v.setText(datosDescuentosTarjeta[1]+" €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //Efectivo importe
        t1v = new TextView(this);
        t1v.setText(datosDescuentosEfectivo[1]+" €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);
        //App importe
        t1v = new TextView(this);
        t1v.setText(datosDescuentoApp[1]+" €");
        t1v.setGravity(Gravity.CENTER);
        t1v.setPadding(0,0,25,0);
        RI.addView(t1v);

        stk.addView(RI);



    }
















}
