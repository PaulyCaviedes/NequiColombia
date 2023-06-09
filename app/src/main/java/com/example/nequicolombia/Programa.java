package com.example.nequicolombia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Programa extends AppCompatActivity {

    private TextView tv;
    private static final int ENVIOS = 1;

    private ImageView cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programa); // Establecer el contenido de la vista
        getSupportActionBar().hide(); // Ocultar la barra de acción

        tv = findViewById(R.id.tv2); // Obtener la referencia al TextView con el id "tv2"
        tv.setText("Disponible"); // Establecer el texto "Disponible" en el TextView

        Intent intent = getIntent(); // Obtener el intent que inició esta actividad
        ArrayList<String> saludo = intent.getStringArrayListExtra("danom"); // Obtener el ArrayList de strings con la clave "danom" del intent
        tv = findViewById(R.id.tvsaludo); // Obtener la referencia al TextView con el id "tvsaludo"
        StringBuilder stringBuilder = new StringBuilder(); // Crear un StringBuilder para construir el texto a mostrar
        if (saludo != null) { // Verificar si el ArrayList no es nulo
            for (String fila : saludo) { // Recorrer cada elemento del ArrayList
                stringBuilder.append(fila).append("\n"); // Agregar cada elemento al StringBuilder
            }
        }
        tv.setText(stringBuilder.toString()); // Establecer el texto construido en el TextView

        String cell2 = getIntent().getStringExtra("cell"); // Obtener una cadena de texto con la clave "cell" del intent
        actusal(cell2); // Llamar al método "actusal" con el valor obtenido

        cerrar = findViewById(R.id.cesion);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarConfirmacionCerrarSesion();
            }
        });


    }

    public void tarjeta(View v) {
        String nombrePersona = obtenerNombrePersona(); // Llamar al método "obtenerNombrePersona" para obtener el nombre de la persona
        Intent intent = new Intent(this, Tarjeta.class); // Crear una nueva instancia de la clase Intent con la actividad de destino (Tarjeta)
        intent.putExtra("nombrePersona", nombrePersona); // Pasar el nombre de la persona como dato extra en el intent
        startActivity(intent); // Iniciar la actividad Tarjeta
    }

    private String obtenerNombrePersona() {
        String cellremi = getIntent().getStringExtra("cell"); // Obtener una cadena de texto con la clave "cell" del intent
        Registro registro = new Registro(this, "database", null, 1); // Crear una instancia de la clase Registro
        return registro.obtenerNombrePersona(cellremi); // Llamar al método "obtenerNombrePersona" de la instancia de Registro y retornar el resultado
    }

    public void historial(View v) {
        String remite2 = getIntent().getStringExtra("cell"); // Obtener una cadena de texto con la clave "cell" del intent
        Intent intent = new Intent(this, Historial.class); // Crear una nueva instancia de la clase Intent con la actividad de destino (Historial)
        intent.putExtra("remite2", remite2); // Pasar el valor de "remite2" como dato extra en el intent
        startActivity(intent); // Iniciar la actividad Historial
    }



    public void enviar(View v) {
        String cell2 = getIntent().getStringExtra("cell"); // Obtener una cadena de texto con la clave "cell" del intent
        Intent intent = new Intent(this, Enviar.class); // Crear una nueva instancia de la clase Intent con la actividad de destino (Enviar)
        intent.putExtra("cell2", cell2); // Pasar el valor de "cell2" como dato extra en el intent
        startActivityForResult(intent, ENVIOS); // Iniciar la actividad Enviar con el código de solicitud ENVIOS
    }

    @Override
    public void onBackPressed() {
        mostrarConfirmacionCerrarSesion(); // Llamar al método "mostrarConfirmacionCerrarSesion" cuando se presione el botón de retroceso
    }

    private void mostrarConfirmacionCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Crear un nuevo AlertDialog.Builder con el contexto actual
        builder.setTitle("Confirmación") // Establecer el título del cuadro de diálogo
                .setMessage("¿Seguro que quieres cerrar sesión?") // Establecer el mensaje del cuadro de diálogo
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() { // Establecer el botón positivo del cuadro de diálogo con un click listener
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarSesion(); // Llamar al método "cerrarSesion" cuando se haga clic en el botón positivo
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() { // Establecer el botón negativo del cuadro de diálogo con un click listener
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Descartar el cuadro de diálogo cuando se haga clic en el botón negativo
                    }
                })
                .show(); // Mostrar el cuadro de diálogo
    }

    private void cerrarSesion() {
        mostrarConfirmacionCerrarSesion();
        Intent intent = new Intent(Programa.this, Entrar.class); // Crear una nueva instancia de la clase Intent con la actividad de destino (Entrar)
        startActivity(intent); // Iniciar la actividad Entrar
        finish(); // Finalizar la actividad actual
    }

    private void actusal(String cellremi) {
        Registro registro = new Registro(this, "database", null, 1); // Crear una instancia de la clase Registro
        String sal = String.valueOf(registro.consultarDinero(cellremi)); // Obtener el saldo como una cadena de texto
        tv = findViewById(R.id.tvsaldo); // Obtener la referencia al TextView con el id "tvsaldo"
        tv.setText("$ " + sal); // Establecer el texto del saldo en el TextView
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENVIOS && resultCode == RESULT_OK && data != null) { // Verificar si el código de solicitud y el resultado son correctos y si el intent no es nulo
            boolean actualizarSaldo = data.getBooleanExtra("actua", false); // Obtener un booleano extra con la clave "actua" del intent
            if (actualizarSaldo) { // Verificar si se debe actualizar el saldo
                String cell2 = getIntent().getStringExtra("cell"); // Obtener una cadena de texto con la clave "cell" del intent
                actusal(cell2); // Llamar al método "actusal" con el valor obtenido
            }
        }
    }
}
