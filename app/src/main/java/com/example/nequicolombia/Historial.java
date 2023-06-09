package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Historial extends AppCompatActivity {

    private ListView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial); // Establecer el contenido de la vista
        getSupportActionBar().hide(); // Ocultar la barra de acción

        history = findViewById(R.id.history); // Obtener referencia del ListView

        historys(); // Llamar al método para obtener el historial de transacciones
    }

    private void historys() {
        String remite2 = getIntent().getStringExtra("remite2"); // Obtener el número de celular de la actividad anterior
        Registro registro = new Registro(this, "database", null, 1); // Crear instancia de la clase Registro
        String consulta = "SELECT * FROM historial WHERE celular_envia = ? OR celular_recibe = ?" + " ORDER BY fecha DESC "; // Consulta SQL para obtener el historial de transacciones
        SQLiteDatabase db = registro.getReadableDatabase(); // Obtener una instancia de la base de datos para lectura
        Cursor cursor = db.rawQuery(consulta, new String[]{remite2, remite2}); // Ejecutar la consulta y obtener el cursor
        List<Object> mihistoria = new ArrayList<>(); // Lista para almacenar las transacciones del historial

        if (cursor.moveToFirst()) { // Verificar si hay registros en el cursor
            do {
                String celuemi = cursor.getString(cursor.getColumnIndexOrThrow("celular_envia")); // Obtener el número de celular del remitente
                String celurece = cursor.getString(cursor.getColumnIndexOrThrow("celular_recibe")); // Obtener el número de celular del destinatario
                String nombreenvia = cursor.getString(cursor.getColumnIndexOrThrow("nombre_envia")); // Obtener el nombre del remitente
                String nombrerecibe = cursor.getString(cursor.getColumnIndexOrThrow("nombrerecibe")); // Obtener el nombre del destinatario
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")); // Obtener la fecha de la transacción
                double monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto")); // Obtener el monto de la transacción

                String nombresa;
                String celdos;

                int color;
                int gravity;

                if (celuemi.equals(remite2)) { // Verificar si el número de celular actual coincide con el número de celular del remitente
                    nombresa = nombrerecibe; // Establecer el nombre del destinatario como el "nombresa"
                    celdos = celurece; // Establecer el número de celular del destinatario como el "celdos"
                    color = Color.RED; // Establecer el color rojo
                    gravity = Gravity.END; // Alinear a la derecha
                } else if (celurece.equals(remite2)) { // Verificar si el número de celular actual coincide con el número de celular del destinatario
                    nombresa = nombreenvia; // Establecer el nombre del remitente como el "nombresa"
                    celdos = celuemi; // Establecer el número de celular del remitente como el "celdos"
                    color = Color.parseColor("#05CD4B"); // Establecer el color verde
                    gravity = Gravity.START; // Alinear a la izquierda
                } else {
                    continue; // Saltar al siguiente registro si no se cumple ninguna condición
                }

                String transaccion = "\n" + fecha + "\n" + nombresa + "\n" + monto + "\n" + celdos + "\n"; // Crear una cadena con la información de la transacción
                mihistoria.add(new Object[]{transaccion, color, gravity}); // Agregar la información de la transacción a la lista "mihistoria"
            } while (cursor.moveToNext()); // Mover al siguiente registro del cursor
        }

        if (mihistoria.isEmpty()) { // Verificar si la lista está vacía
            Toast.makeText(this, "No se encontraron transacciones", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje indicando que no hay transacciones
        } else {
            ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, mihistoria) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent); // Obtener la vista del elemento de la lista

                    Object[] item = (Object[]) getItem(position); // Obtener el elemento de la lista en la posición actual
                    String transaccion = (String) item[0]; // Obtener la información de la transacción
                    int color = (int) item[1]; // Obtener el color del texto
                    int gravity = (int) item[2]; // Obtener la gravedad del texto

                    TextView textView = view.findViewById(android.R.id.text1); // Obtener el TextView de la vista
                    textView.setText(transaccion); // Establecer el texto de la transacción
                    textView.setTextColor(color); // Establecer el color del texto
                    textView.setGravity(gravity); // Establecer la gravedad del texto

                    return view; // Devolver la vista modificada
                }
            };

            history.setAdapter(adapter); // Establecer el adaptador en el ListView para mostrar el historial
        }

        cursor.close(); // Cerrar el cursor
    }

    public void flecha2(View view) {
        finish();
    }// Finalizar la actividad actual
}
