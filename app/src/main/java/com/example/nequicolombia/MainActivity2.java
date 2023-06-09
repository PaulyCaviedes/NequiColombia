package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // Establecer el contenido de la vista
        getSupportActionBar().hide(); // Ocultar la barra de acción

        TimerTask trabajo = new TimerTask() { // Crear una nueva tarea del temporizador
            @Override
            public void run() { // Método que se ejecuta cuando se realiza la tarea
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class); // Crear una nueva instancia de la clase Intent con la actividad de destino
                startActivity(intent); // Iniciar la actividad MainActivity3
                finish(); // Finalizar la actividad actual (MainActivity2)
            }
        };
        Timer tiempo = new Timer(); // Crear una nueva instancia de la clase Timer
        tiempo.schedule(trabajo, 4000); // Programar la ejecución de la tarea después de 4000 milisegundos (4 segundos)
    }
}
