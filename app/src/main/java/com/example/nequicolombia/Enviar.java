package com.example.nequicolombia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Enviar extends AppCompatActivity {

    private EditText monto, numeroenviar;
    private Registro registro;
    private Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar); // Establecer el contenido de la vista
        getSupportActionBar().hide(); // Ocultar la barra de acción

        monto = findViewById(R.id.edtNumberDecimal); // Obtener referencia del EditText
        numeroenviar = findViewById(R.id.etText); // Obtener referencia del EditText
        registro = new Registro(this, "database", null, 1); // Crear instancia de la clase Registro
        enviar = findViewById(R.id.button); // Obtener referencia del Button
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos(); // Validar campos antes de mostrar la confirmación del diálogo
            }
        });
    }

    private void validarCampos() {
        String plata = monto.getText().toString(); // Obtener el monto ingresado
        String celularenviar = numeroenviar.getText().toString(); // Obtener el número de celular al que se va a enviar el dinero

        if (plata.isEmpty() && celularenviar.isEmpty()) {
            Toast.makeText(this, "Ingresa el monto y el número a enviar", Toast.LENGTH_SHORT).show();
        } else if (plata.isEmpty()) {
            Toast.makeText(this, "Ingresa el monto", Toast.LENGTH_SHORT).show();
        } else if (celularenviar.isEmpty()) {
            Toast.makeText(this, "Ingresa el número a enviar", Toast.LENGTH_SHORT).show();
        } else {
            mostrarConfirmacionDialogo();
        }
    }


    private void mostrarConfirmacionDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Crear un objeto AlertDialog.Builder
        builder.setTitle("Confirmación") // Establecer el título del diálogo
                .setMessage("¿Seguro de realizar la transferencia?") // Establecer el mensaje del diálogo
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        validarTransferencia(); // Llamar al método para validar la transferencia
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción a realizar si se selecciona "No"
                    }
                })
                .show(); // Mostrar el cuadro de diálogo
    }

    private void validarTransferencia() {
        String celular = getIntent().getStringExtra("cell2"); // Obtener el número de celular de la actividad anterior
        String plata = monto.getText().toString(); // Obtener el monto ingresado
        String celularenviar = numeroenviar.getText().toString(); // Obtener el número de celular al que se va a enviar el dinero

        if (!registro.esNumeroRegistrado(celular)) { // Verificar si el número de celular actual está registrado
            Toast.makeText(this, "Número incorrecto, por favor verifique su número", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!registro.esNumeroRegistrado(celularenviar)) { // Verificar si el número de celular al que se va a enviar el dinero está registrado
            Toast.makeText(this, "El número a enviar no se encuentra registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        float total = Float.parseFloat(plata); // Convertir el monto a un valor numérico
        float saldomio = registro.consultarDinero(celular); // Obtener el saldo actual del usuario actual
        float recibir = registro.consultarDinero(celularenviar); // Obtener el saldo actual del usuario al que se va a enviar el dinero

        if (total >= 1000) { // Verificar si el monto a transferir es mayor o igual a 1000
            if (saldomio >= total) { // Verificar si el saldo del usuario actual es suficiente para la transferencia
                float nuevosaldomio = saldomio - total; // Calcular el nuevo saldo del usuario actual
                float nuevorecibir = recibir + total; // Calcular el nuevo saldo del usuario al que se va a enviar el dinero

                registro.actualizarDinero(celular, nuevosaldomio); // Actualizar el saldo del usuario actual en la base de datos
                registro.actualizarDinero(celularenviar, nuevorecibir); // Actualizar el saldo del usuario al que se envió el dinero en la base de datos

                Toast.makeText(this, "La transferencia fue exitosa", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de éxito

                Intent intent = new Intent();
                intent.putExtra("actua", true); // Pasar un valor booleano a la actividad anterior
                setResult(RESULT_OK, intent); // Establecer el resultado de la actividad actual como "RESULT_OK"
                finish(); // Finalizar la actividad actual

                SQLiteDatabase db = registro.getReadableDatabase(); // Obtener una instancia de la base de datos
                String desti = registro.obtenerNombrePersona(celularenviar); // Obtener el nombre del destinatario
                String remi = registro.obtenerNombrePersona(celular); // Obtener el nombre del remitente
                Calendar calendar = Calendar.getInstance(); // Obtener la fecha y hora actual
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); // Formato de la fecha
                String fecha = dateFormat.format(calendar.getTime()); // Obtener la fecha y hora formateadas
                db = registro.getWritableDatabase(); // Obtener una instancia de la base de datos para escritura
                ContentValues values = new ContentValues(); // Objeto para almacenar los valores a insertar
                values.put("fecha", fecha); // Insertar la fecha y hora
                values.put("nombre_envia", remi); // Insertar el nombre del remitente
                values.put("nombrerecibe", desti); // Insertar el nombre del destinatario
                values.put("monto", plata); // Insertar el monto transferido
                values.put("celular_envia", celular); // Insertar el número de celular del remitente
                values.put("celular_recibe", celularenviar); // Insertar el número de celular del destinatario
                db.insert("historial", null, values); // Insertar los valores en la tabla "historial" de la base de datos
                db.close(); // Cerrar la base de datos

            } else {
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de saldo insuficiente
            }
        } else {
            Toast.makeText(this, "El monto mínimo de transferencia es de 1000", Toast.LENGTH_SHORT).show(); // Mostrar un mensaje de monto mínimo no alcanzado
        }
    }

    public void flecha1(View view) {
        finish(); }// Finalizar la actividad actual

}
