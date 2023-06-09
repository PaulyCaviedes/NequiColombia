package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Establecer el contenido de la vista
        getSupportActionBar().hide(); // Ocultar la barra de acción

        TimerTask trabajo = new TimerTask() { // Crear una nueva tarea del temporizador
            @Override
            public void run() { // Método que se ejecuta cuando se realiza la tarea
                Intent intent = new Intent(MainActivity.this, MainActivity2.class); // Crear una nueva instancia de la clase Intent con la actividad de destino
                startActivity(intent); // Iniciar la actividad MainActivity2
                finish(); //darle finalidad a la actividad
            }
        };
        Timer tiempo = new Timer(); // Crear una nueva instancia de la clase Timer
        tiempo.schedule(trabajo, 2000); // Programar la ejecución de la tarea después de 2000 milisegundos (2 segundos)

    }
}
