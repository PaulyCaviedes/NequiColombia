package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Clave extends AppCompatActivity {

    private EditText et;
    boolean claveValida = false;
    private Registro registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clave);
        getSupportActionBar().hide();

        String cell = getIntent().getStringExtra("Celular");  // Obtiene el valor de la clave "Celular" de la intent

        et = findViewById(R.id.etclave);
        registro = new Registro(this, "database", null, 1);  // Inicializa el objeto Registro

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et.getText().toString();  // Obtiene el texto ingresado en el campo de texto

                claveValida = validPin(password);  // Verifica si la contraseña es válida

                if (claveValida) {
                    Intent programaIntent = new Intent(Clave.this, Splash.class);  // Crea un intent para la actividad Splash

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(programaIntent);  // Inicia la actividad Splash

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SQLiteDatabase db = registro.getReadableDatabase();  // Obtiene una instancia de la base de datos en modo lectura
                                    String qwery = "SELECT Nombre, Saldo FROM articulos WHERE Celular =" + cell;  // Consulta SQL para obtener el nombre y el saldo de los artículos correspondientes al celular
                                    Cursor cursor = db.rawQuery(qwery, null);  // Ejecuta la consulta en la base de datos
                                    ArrayList<String> danom = new ArrayList<>();  // Crea una lista para almacenar los nombres obtenidos

                                    while (cursor.moveToNext()) {
                                        String nom = cursor.getString(cursor.getColumnIndexOrThrow("Nombre"));  // Obtiene el nombre del cursor
                                        danom.add("Hola,\n" + nom);  // Agrega el nombre a la lista

                                    }
                                    Intent programaIntent = new Intent(Clave.this, Programa.class);  // Crea un intent para la actividad Programa
                                    programaIntent.putStringArrayListExtra("danom", danom);  // Agrega la lista de nombres como extra en el intent
                                    programaIntent.putExtra("cell", cell);  // Agrega el número de celular como extra en el intent
                                    startActivity(programaIntent);  // Inicia la actividad Programa
                                    finish();
                                    cursor.close();  // Cierra el cursor
                                }
                            }, 1000);  // Retrasa la ejecución durante 1000 milisegundos (1 segundo)
                        }
                    }, 1000);  // Retrasa la ejecución durante 1000 milisegundos (1 segundo)
                } else {
                    Toast.makeText(Clave.this, "Clave incorrecta", Toast.LENGTH_SHORT).show();  // Muestra un mensaje indicando que la clave es incorrecta
                }
            }
        });
    }

    public void atras(View view) {
        finish();  // Cierra la actividad actual y vuelve a la actividad anterior
    }

    private boolean validPin(String Pin) {
        SQLiteDatabase db = registro.getReadableDatabase();  // Obtiene una instancia de la base de datos en modo lectura
        String[] projection = {"Pin"};  // Define las columnas a seleccionar en la consulta
        String selection = "Pin = ?";  // Define la cláusula WHERE de la consulta
        String[] selectionArgs = {Pin};  // Define los argumentos de la cláusula WHERE
        Cursor cursor = db.query("articulos", projection, selection, selectionArgs, null, null, null);  // Ejecuta la consulta en la base de datos
        if (cursor.moveToFirst()) {
            claveValida = true;  // Si hay al menos una fila en el cursor, la clave es válida
        }
        cursor.close();  // Cierra el cursor
        db.close();  // Cierra la base de datos
        return claveValida;  // Devuelve el estado de validez de la clave
    }
}
