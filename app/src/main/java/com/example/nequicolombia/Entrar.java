package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class
Entrar extends AppCompatActivity {

    private Registro registro; // Declaración de variable miembro para acceder a la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar); // Establecer el contenido de la vista
        getSupportActionBar().hide(); // Ocultar la barra de acción

        registro = new Registro(this, "database", null, 1); // Crear instancia de la clase Registro
    }

    public void ingresar(View v) {
        EditText editTextNumero = findViewById(R.id.ed1); // Obtener referencia del EditText
        String Celular = editTextNumero.getText().toString(); // Obtener el número ingresado

        if (existeNumero(Celular)) { // Verificar si el número existe en la base de datos
            Intent intent = new Intent(this, Clave.class); // Crear un Intent para iniciar la actividad Clave
            intent.putExtra("Celular", Celular); // Pasar el número de celular como dato extra
            startActivity(intent); // Iniciar la actividad Clave
            finish();
        } else {
            Toast.makeText(this, "El número ingresado no está registrado.", Toast.LENGTH_SHORT).show(); // Mostrar mensaje de error
        }
    }

    public void atras(View view) {
        finish(); // Finalizar la actividad actual y regresar a la actividad anterior
    }

    private boolean existeNumero(String Celular) {
        SQLiteDatabase db = registro.getWritableDatabase(); // Obtener instancia de la base de datos
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM articulos WHERE celular = ?", new String[]{Celular}); // Consulta para verificar si el número existe
        cursor.moveToFirst(); // Mover el cursor al primer resultado
        int count = cursor.getInt(0); // Obtener el valor de la columna COUNT(*)
        cursor.close(); // Cerrar el cursor
        return count > 0; // Devolver true si el count es mayor a cero (el número existe), de lo contrario, devolver false
    }
}
