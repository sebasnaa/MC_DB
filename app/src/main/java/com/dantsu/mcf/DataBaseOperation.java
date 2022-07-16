package com.dantsu.mcf;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.LinkedList;

public class DataBaseOperation extends SQLiteOpenHelper {

    public static  final String TABLA_CLIENTES = "TABLA_CLIENTES";
    public static  final String TABLA_PEDIDOS = "TABLA_PEDIDOS";
    public static  final String TABLA_DESCUENTOS = "TABLA_DESCUENTOS";
    public static final String COLUMNA_NOMBRE_CLIENTE = "NOMBRE_CLIENTE";
    public static final String COLUMNA_DIRECCION_CLIENTE = "DIRECCION_CLIENTE";
    public static final String COLUMNA_TELEFONO_CLIENTE = "TELEFONO_CLIENTE";
    public static final String COLUMNA_TIPO_PEDIDO = "TIPO_PEDIDO";
    public static final String COLUMNA_PRECIO_PEDIDO = "PRECIO_PEDIDO";
    public static final String COLUMNA_PRECIO_DESCUENTO = "PRECIO_DESCUENTO";
    public static final String COLUMNA_FECHA_PEDIDO = "FECHA_PEDIDO";
    public static final String COLUMNA_METODO_PAGO = "METODO_PAGO";

    public DataBaseOperation(@Nullable Context context) {
        super(context, "clientes.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createTableStatement = "CREATE TABLE " + TABLA_CLIENTES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMNA_NOMBRE_CLIENTE + " TEXT, " + COLUMNA_DIRECCION_CLIENTE + " TEXT, " + COLUMNA_TELEFONO_CLIENTE + " INT)";


        String createTablePedidos = "CREATE TABLE " + TABLA_PEDIDOS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMNA_TELEFONO_CLIENTE + " INT, "  + COLUMNA_TIPO_PEDIDO + " TEXT, " + COLUMNA_PRECIO_PEDIDO + " REAL ," + COLUMNA_METODO_PAGO + " TEXT ," +
                COLUMNA_FECHA_PEDIDO + " DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')) )";

        String createTableDescuentos = "CREATE TABLE " + TABLA_DESCUENTOS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMNA_PRECIO_DESCUENTO + " REAL ," + COLUMNA_METODO_PAGO + " TEXT ," +
                COLUMNA_FECHA_PEDIDO + " DATETIME NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')) )";

        db.execSQL(createTableStatement);
        db.execSQL(createTablePedidos);
        db.execSQL(createTableDescuentos);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onCreate(db);

    }


    public void eliminarTabla(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLA_CLIENTES);
    }


    public boolean agregarCliente(ModeloCliente modeloCliente){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contenido = new ContentValues();

        if(!existeCliente(modeloCliente.getTelefonoString())){
            contenido.put(COLUMNA_NOMBRE_CLIENTE,modeloCliente.getNombre());
            contenido.put(COLUMNA_DIRECCION_CLIENTE,modeloCliente.getDireccion());
            contenido.put(COLUMNA_TELEFONO_CLIENTE,modeloCliente.getTelefono());

            long insert = db.insert(TABLA_CLIENTES, null, contenido);
            if(insert == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean agregarPedido(ModeloPedido modeloPedido){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contenido = new ContentValues();


        contenido.put(COLUMNA_TELEFONO_CLIENTE,modeloPedido.getTelefono());
        contenido.put(COLUMNA_TIPO_PEDIDO,modeloPedido.getTipo());
        contenido.put(COLUMNA_PRECIO_PEDIDO,modeloPedido.getPrecio());
        contenido.put(COLUMNA_METODO_PAGO,modeloPedido.getMetodoPago());



        long insert = db.insert(TABLA_PEDIDOS, null, contenido);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean agregarDescuentoPedido(String metodoPago,double importeDescuento){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contenido = new ContentValues();

        contenido.put(COLUMNA_PRECIO_DESCUENTO,importeDescuento);
        contenido.put(COLUMNA_METODO_PAGO,metodoPago);

        long insert = db.insert(TABLA_DESCUENTOS,null,contenido);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean existeCliente(String telefono){

        String p_tel = telefono+"";
        String[] parametros = new String[1];
        parametros[0] = telefono;
        String query = "SELECT * FROM TABLA_CLIENTES  WHERE TELEFONO_CLIENTE = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,parametros);

        if(!cursor.moveToFirst()){
            cursor.close();
            return false;
        }
        int contador = cursor.getInt(0);
        cursor.close();
        return contador > 0;
    }

    public LinkedList<ModeloCliente> getClientes(){
        LinkedList<ModeloCliente> listaRes = new LinkedList<>();

        String query = "SELECT * FROM TABLA_CLIENTES";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int clienteId = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String direccion = cursor.getString(2);
                int telefono = cursor.getInt(3);
                ModeloCliente aux = new ModeloCliente(clienteId,nombre,direccion,telefono);
                listaRes.add(aux);
                cursor.moveToNext();
            }
        }
    return listaRes;
    }

    public LinkedList<ModeloPedido> getPedidos(){
        LinkedList<ModeloPedido> listaRes = new LinkedList<>();

        String query = "SELECT * FROM TABLA_PEDIDOS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){

                int clienteId = cursor.getInt(0);
                int telefono = cursor.getInt(1);
                String tipo = cursor.getString(2);
                double precio = cursor.getDouble(3);
                String metodoPago = cursor.getString(4);
                String fecha = cursor.getString(5);
                ModeloPedido aux = new ModeloPedido(clienteId,telefono,tipo,precio,metodoPago,fecha);
                aux.setFecha(fecha);

                listaRes.add(aux);
                cursor.moveToNext();
            }
        }
        return listaRes;
    }

    public ModeloCliente getCliente(String tel){
        //ModeloCliente res = new ModeloCliente();
        ModeloCliente res = null;
        String p_tel = tel+"";
        String[] parametros = new String[1];
        parametros[0] = tel;
        String query = "SELECT * FROM TABLA_CLIENTES  WHERE TELEFONO_CLIENTE = ? ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query,parametros);

        if(cursor.moveToFirst()){

            int clienteId = cursor.getInt(0);
            String nombre = cursor.getString(1);
            String direccion = cursor.getString(2);
            int telefono = cursor.getInt(3);
            res = new ModeloCliente(clienteId,nombre,direccion,telefono);

        }
        cursor.close();
        db.close();
        return res;
    }


    public boolean eliminarCliente(String tel){
        ModeloCliente res = new ModeloCliente();
        String p_tel = tel+"";
        String[] parametros = new String[1];
        parametros[0] = tel;
        String query = "DELETE FROM TABLA_CLIENTES  WHERE TELEFONO_CLIENTE = ? ";
        SQLiteDatabase db = this.getWritableDatabase();

        int borradoExitoso = db.delete(TABLA_CLIENTES,"TELEFONO_CLIENTE=?", parametros);

        db.close();
        return borradoExitoso > 0;
    }



    public double[] getBalanceCompleto(){
        SQLiteDatabase db = this.getReadableDatabase();
        //SELECT COUNT(*) FROM

        /*String[] parametros = new String[1];
        parametros[0] = "Glovo";
        long taskCount = DatabaseUtils.longForQuery(db, "SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE " + COLUMNA_TIPO_PEDIDO + "=?",
                parametros);*/



        //"select sum(amount) from transaction_table where category = Salary ;", null
        double res[] = new double[2];

        //String query = "SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS;
        Cursor c;
        double totalVentas;
        c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS,null);
        if(c.moveToFirst())
            totalVentas = c.getInt(0);
        else
            totalVentas = -1;
        c.close();

        double contadorRegistros = DatabaseUtils.queryNumEntries(db, TABLA_PEDIDOS);

        res[0] = contadorRegistros;
        res[1] = totalVentas;

        return res;

    }

    public double[] getBalanceCompletoMod(String fecha,int tipo){
        SQLiteDatabase db = this.getReadableDatabase();
        double res[] = new double[2];


        double totalVentas;
        String[] parametros = new String[2];
        String[] parametrosMod = new String[4];

        String parametrosDescuentos[] = new String[3];

        String fechaArray[] = fecha.split("-");
        String year = fechaArray[0];
        String mes = fechaArray[1];
        String dia = fechaArray[2];
        int yearInt = Integer.parseInt(year);
        int mesInt = Integer.parseInt(mes)-1;
        int diaInt = Integer.parseInt(dia);

        Calendar fechajavaLow = Calendar.getInstance();
        Calendar fechajavaTop = Calendar.getInstance();
        fechajavaLow.set(yearInt,mesInt,diaInt,3,0,0);
        fechajavaTop.set(yearInt,mesInt,diaInt,3,0,0);
        fechajavaTop.add(Calendar.DATE,1);


        String fechaLow = addZero(fechajavaLow.get(Calendar.YEAR),fechajavaLow.get(Calendar.MONTH)+1,fechajavaLow.get(Calendar.DATE))+ " 10:00:00";
        String fechaTop = addZero(fechajavaTop.get(Calendar.YEAR),fechajavaTop.get(Calendar.MONTH)+1,fechajavaTop.get(Calendar.DATE))+" 03:00:00";

        parametros[0] = fechaLow;
        parametros[1] = fechaTop;



        Cursor c = db.rawQuery("",null);
        Cursor cursor = db.rawQuery("",null);

        switch (tipo){
            case 0:

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE "+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? ",parametros);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE " +COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? ",parametros);

                break;

            case 1:
                //Restaurante
                parametrosMod[0] = parametros[0];
                parametrosMod[1] = parametros[1];
                parametrosMod[2] = "R";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? )",parametrosMod);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? )",parametrosMod);

                break;

            case 2:
                //Glovo
                parametrosMod[0] = parametros[0];
                parametrosMod[1] = parametros[1];
                parametrosMod[2] = "G";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? )",parametrosMod);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? )",parametrosMod);


                break;

            case 3:
                //Restaurante Efectivo
                parametrosMod[0] = parametros[0];
                parametrosMod[1] = parametros[1];
                parametrosMod[2] = "R";
                parametrosMod[3] = "Efectivo";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)",parametrosMod);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)"   ,parametrosMod);

                break;

            case 4:
                //Restaurante Tarjeta
                parametrosMod[0] = parametros[0];
                parametrosMod[1] = parametros[1];
                parametrosMod[2] = "R";
                parametrosMod[3] = "Tarjeta";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)",parametrosMod);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)"   ,parametrosMod);

                break;

            case 5:
                //Glovo Efectivo
                parametrosMod[0] = parametros[0];
                parametrosMod[1] = parametros[1];
                parametrosMod[2] = "G";
                parametrosMod[3] = "Efectivo";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)",parametrosMod);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)"   ,parametrosMod);
                break;

            case 6:
                //Glovo Tarjeta
                parametrosMod[0] = parametros[0];
                parametrosMod[1] = parametros[1];
                parametrosMod[2] = "G";
                parametrosMod[3] = "Tarjeta";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)",parametrosMod);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)"   ,parametrosMod);
                break;

            case 7:
                //Glovo App
                parametrosMod[0] = parametros[0];
                parametrosMod[1] = parametros[1];
                parametrosMod[2] = "G";
                parametrosMod[3] = "App";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_PEDIDO+") FROM "+TABLA_PEDIDOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)",parametrosMod);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_PEDIDOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_TIPO_PEDIDO + " =? AND " + COLUMNA_METODO_PAGO + "=?)"   ,parametrosMod);
                break;

            case 8:
                //Descuentos efectivo
                parametrosDescuentos[0] = parametros[0];
                parametrosDescuentos[1] = parametros[1];
                parametrosDescuentos[2] = "Efectivo";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_DESCUENTO+") FROM "+TABLA_DESCUENTOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND " + COLUMNA_METODO_PAGO + "=?)",parametrosDescuentos);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_DESCUENTOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_METODO_PAGO + "=?)"   ,parametrosDescuentos);


                break;

            case 9:
                //Descuentos tarjeta
                parametrosDescuentos[0] = parametros[0];
                parametrosDescuentos[1] = parametros[1];
                parametrosDescuentos[2] = "Tarjeta";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_DESCUENTO+") FROM "+TABLA_DESCUENTOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_METODO_PAGO + "=?)",parametrosDescuentos);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_DESCUENTOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_METODO_PAGO + "=?)"   ,parametrosDescuentos);


                break;

            case 10:
                //Descuentos App
                parametrosDescuentos[0] = parametros[0];
                parametrosDescuentos[1] = parametros[1];
                parametrosDescuentos[2] = "App";

                c = db.rawQuery("SELECT SUM("+COLUMNA_PRECIO_DESCUENTO+") FROM "+TABLA_DESCUENTOS +" WHERE ("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_METODO_PAGO + "=?)",parametrosDescuentos);
                cursor= db.rawQuery("SELECT COUNT (*) FROM " + TABLA_DESCUENTOS + " WHERE("+COLUMNA_FECHA_PEDIDO+" > ? " + " AND " + COLUMNA_FECHA_PEDIDO + " < ? AND "+ COLUMNA_METODO_PAGO + "=?)"   ,parametrosDescuentos);


                break;
        }

        if(c.moveToFirst())
            totalVentas = c.getDouble(0);
        else
            totalVentas = -1;
        c.close();



        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();


        res[0] = count;
        res[1] = totalVentas;

        return res;

    }





    private String addZero(int year,int mes,int dia){
        String res = "";
        res = year+"-";
        if(mes<10){
            res+="0"+mes+"-";
        }else{
            res+=""+mes+"-";
        }
        if(dia<10){
            res+="0"+dia;
        }else{
            res+=""+dia;
        }


        return res;

    }


}
