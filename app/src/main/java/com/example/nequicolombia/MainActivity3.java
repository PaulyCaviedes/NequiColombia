package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3); // Establecer el contenido de la vista
        getSupportActionBar().hide(); // Ocultar la barra de acción
    }

    public void ayuda(View v) {
        Intent intent = new Intent(this, Ayuda.class); // Crear una nueva instancia de la clase Intent con la actividad de destino (Ayuda)
        startActivity(intent); // Iniciar la actividad Ayuda
    }

    public void crear(View v) {
        Intent intent = new Intent(this, Crear.class); // Crear una nueva instancia de la clase Intent con la actividad de destino (Crear)
        startActivity(intent); // Iniciar la actividad Crear
    }

    public void entrar(View v) {
        Intent intent = new Intent(this, Entrar.class); // Crear una nueva instancia de la clase Intent con la actividad de destino (Entrar)
        startActivity(intent); // Iniciar la actividad Entrar
    }
}
