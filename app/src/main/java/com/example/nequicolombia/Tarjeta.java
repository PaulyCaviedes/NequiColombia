package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class Tarjeta extends AppCompatActivity {
    private TextView fecha;
    private TextView nombre;
    private TextView codigo;
    private TextView nequi;
    private TextView cvv;
    private Registro registro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta);
        getSupportActionBar().hide();

        // Inicializar los TextView
        fecha = findViewById(R.id.tvfecha);
        nombre = findViewById(R.id.nombre);
        codigo = findViewById(R.id.codigo);
        nequi = findViewById(R.id.nequi);
        cvv = findViewById(R.id.cvv);

        // Crear una instancia de la clase Registro
        registro = new Registro(this, "database", null, 1);

        // Obtener el nombre de la base de datos y establecerlo en el TextView 'nombre'
        String nombreBaseDatos = registro.obtenerNombreBaseDatos();
        nombre.setText(nombreBaseDatos);

        // Mostrar el nombre de la persona en el TextView 'nombre'
        mostrarNombrePersona();

        // Generar números aleatorios para el número de tarjeta y el CVV
        generarNumerosAleatorios();
    }

    // Método para mostrar el nombre de la persona en el TextView 'nombre'
    private void mostrarNombrePersona() {
        String nombrePersona = getIntent().getStringExtra("nombrePersona");
        nombre.setText(nombrePersona);
    }

    // Método para generar números aleatorios para el número de tarjeta y el CVV
    private void generarNumerosAleatorios() {
        // Generar un número de tarjeta aleatorio de 16 dígitos y separarlo en grupos de 4 dígitos
        String tarjetaNumero = generarNumerosAleatorios(16);
        String tarjetaSeparada = separarNumeros(tarjetaNumero, 4);
        codigo.setText(tarjetaSeparada);

        // Generar un número de CVV aleatorio de 3 dígitos
        String cvvNumero = generarNumerosAleatorios(3);
        cvv.setText(cvvNumero);
    }

    // Método para generar una cadena de números aleatorios de la longitud especificada
    private String generarNumerosAleatorios(int longitud) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int numero = random.nextInt(10);
            sb.append(numero);
        }
        return sb.toString();
    }

    // Método para separar una cadena de números en grupos de la longitud especificada
    private String separarNumeros(String numero, int separacion) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numero.length(); i += separacion) {
            int endIndex = Math.min(i + separacion, numero.length());
            String grupo = numero.substring(i, endIndex);
            sb.append(grupo);
            if (endIndex < numero.length()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Método para finalizar la actividad cuando se hace clic en la flecha de retroceso
    public void flecha(View view) {
        finish();
    }
}
