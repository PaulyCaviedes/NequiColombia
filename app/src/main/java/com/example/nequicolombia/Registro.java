package com.example.nequicolombia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Registro extends SQLiteOpenHelper {
    private String nombreBaseDatos;

    // Constructor de la clase Registro
    public Registro(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        nombreBaseDatos = name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Método onCreate: Se ejecuta cuando la base de datos es creada
        // Aquí se crean las tablas de la base de datos

        // Crear tabla 'articulos' con las siguientes columnas:

        db.execSQL("create table articulos(Celular text primary key, Nombre text, Correo text, Pin text, Confirmar text, Saldo text)");

        // Crear tabla 'historial' con las siguientes columnas:
        db.execSQL("create table historial(id integer primary key AUTOINCREMENT, fecha text, nombre_envia text, nombrerecibe text, monto text, celular_envia text, celular_recibe text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Método onUpgrade: Se ejecuta cuando se realiza una actualización de la base de datos
        // Aquí se pueden realizar acciones como modificar la estructura de las tablas existentes
        // en caso de una actualización de la aplicación
    }

    // Método para actualizar el saldo de un registro en la tabla 'articulos'
    public void actualizarDinero(String celular, float nuevoSaldo) {
        SQLiteDatabase db = getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los nuevos valores
        ContentValues valores = new ContentValues();
        valores.put("Saldo", String.valueOf(nuevoSaldo));

        // Definir la cláusula WHERE para seleccionar el registro a actualizar
        String whereClause = "Celular=?";
        String[] whereArgs = {celular};

        // Ejecutar la actualización utilizando el método update de SQLiteDatabase
        db.update("articulos", valores, whereClause, whereArgs);

        db.close();
    }

    // Método para consultar el saldo de un registro en la tabla 'articulos'
    public float consultarDinero(String celular) {
        SQLiteDatabase db = getReadableDatabase();

        // Definir las columnas a seleccionar
        String[] columnas = {"Saldo"};

        // Definir la cláusula WHERE para seleccionar el registro deseado
        String selection = "Celular=?";
        String[] selectionArgs = {celular};

        // Ejecutar la consulta utilizando el método query de SQLiteDatabase
        Cursor cursor = db.query("articulos", columnas, selection, selectionArgs, null, null, null);

        float saldo = 0;

        // Verificar si el cursor contiene algún resultado y obtener el saldo del registro
        if (cursor.moveToFirst()) {
            saldo = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow("Saldo")));
        }

        cursor.close();
        db.close();

        return saldo;
    }

    // Método para verificar si un número de celular está registrado en la tabla 'articulos'
    public boolean esNumeroRegistrado(String celular) {
        SQLiteDatabase db = getReadableDatabase();

        // Definir las columnas a seleccionar
        String[] columnas = {"Celular"};

        // Definir la cláusula WHERE para seleccionar el registro deseado
        String selection = "Celular=?";
        String[] selectionArgs = {celular};

        // Ejecutar la consulta utilizando el método query de SQLiteDatabase
        Cursor cursor = db.query("articulos", columnas, selection, selectionArgs, null, null, null);

        // Verificar si el cursor contiene algún resultado
        boolean registrado = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return registrado;
    }

    // Método para obtener el nombre de una persona a partir de su número de celular en la tabla 'articulos'
    public String obtenerNombrePersona(String celular) {
        SQLiteDatabase db = getReadableDatabase();

        // Definir las columnas a seleccionar
        String[] columnas = {"Nombre"};

        // Definir la cláusula WHERE para seleccionar el registro deseado
        String selection = "Celular=?";
        String[] selectionArgs = {celular};

        // Ejecutar la consulta utilizando el método query de SQLiteDatabase
        Cursor cursor = db.query("articulos", columnas, selection, selectionArgs, null, null, null);

        String nombrePersona = "";

        // Verificar si el cursor contiene algún resultado y obtener el nombre del registro
        if (cursor.moveToFirst()) {
            nombrePersona = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));
        }

        cursor.close();
        db.close();

        return nombrePersona;
    }

    // Método para obtener el nombre de la base de datos
    public String obtenerNombreBaseDatos() {
        return nombreBaseDatos;
    }
}
