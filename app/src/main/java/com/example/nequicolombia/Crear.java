package com.example.nequicolombia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Crear extends AppCompatActivity {

    private EditText et1, et2, et3, et4, et5;
    private int saldo = 1000000;
    private SQLiteOpenHelper helper = new Registro(this,"database", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        getSupportActionBar().hide();

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);

        helper = new Registro(this, "database", null, 1);  // Inicializa el objeto Registro para trabajar con la base de datos
    }

    public void registrar(View v) {
        validacion();  // Realiza la validación de los campos de entrada

        String Nom = et1.getText().toString();  // Obtiene el valor del campo Nombre
        String Cel = et2.getText().toString();  // Obtiene el valor del campo Celular
        String Cor = et3.getText().toString();  // Obtiene el valor del campo Correo
        String pin = et4.getText().toString();  // Obtiene el valor del campo Pin
        String con = et5.getText().toString();  // Obtiene el valor del campo Confirmar

        if (Nom.isEmpty() || Cel.isEmpty() || Cor.isEmpty() || pin.isEmpty() || con.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_LONG).show();  // Muestra un mensaje indicando que todos los campos deben ser completados
        } else {
            if (isValidEmail(Cor)) {  // Verifica si el correo es válido
                if (pin.equals(con)) {  // Verifica si el Pin y la Confirmación coinciden
                    if (Cel.length() == 10) {  // Verifica si el número de celular tiene 10 dígitos
                        if (!isNumeroRegistrado(Cel)) {  // Verifica si el número de celular ya está registrado
                            if (!isCorreoRegistrado(Cor)) {  // Verifica si el correo electrónico ya está registrado
                                ContentValues registrarse = new ContentValues();  // Crea un objeto ContentValues para almacenar los valores a insertar
                                registrarse.put("Nombre", Nom);  // Agrega el valor del nombre
                                registrarse.put("Celular", Cel);  // Agrega el valor del celular
                                registrarse.put("Correo", Cor);  // Agrega el valor del correo
                                registrarse.put("Pin", pin);  // Agrega el valor del Pin
                                registrarse.put("Confirmar", con);  // Agrega el valor de la Confirmación

                                registrarse.put("Saldo", saldo);  // Agrega el valor del saldo

                                try {
                                    SQLiteDatabase writableDatabase = helper.getWritableDatabase();  // Obtiene una instancia de la base de datos en modo escritura
                                    writableDatabase.insert("articulos", null, registrarse);  // Inserta los valores en la tabla "articulos"
                                    writableDatabase.close();  // Cierra la base de datos

                                    et1.setText("");  // Limpia el campo Nombre
                                    et2.setText("");  // Limpia el campo Celular
                                    et3.setText("");  // Limpia el campo Correo
                                    et4.setText("");  // Limpia el campo Pin
                                    et5.setText("");  // Limpia el campo Confirmar

                                    Toast.makeText(this, "Tu registro se creó exitosamente", Toast.LENGTH_SHORT).show(); // Muestra un mensaje indicando que el registro se creó exitosamente

                                    Intent intent = new Intent(this, Programa.class);  // Crea un Intent para redirigir a la clase Programa
                                    startActivity(intent);  // Inicia la actividad Programa
                                    finish();  // Finaliza la actividad actual

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(this, "Error al registrar. Inténtalo nuevamente", Toast.LENGTH_SHORT).show();  // Muestra un mensaje indicando que ocurrió un error al registrar
                                }
                            } else {
                                Toast.makeText(this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();  // Muestra un mensaje indicando que el correo electrónico ya está registrado
                            }
                        } else {
                            Toast.makeText(this, "El número de celular ya está registrado", Toast.LENGTH_SHORT).show();  // Muestra un mensaje indicando que el número de celular ya está registrado
                        }
                    } else {
                        Toast.makeText(this, "El número de celular debe tener 10 dígitos", Toast.LENGTH_SHORT).show();  // Muestra un mensaje indicando que el número de celular debe tener 10 dígitos
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();  // Muestra un mensaje indicando que las contraseñas no coinciden
                }
            } else {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();  // Muestra un mensaje indicando que el correo es inválido
            }
        }
    }

    // Método para verificar si el correo electrónico tiene un formato válido
    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$");
        return pattern.matcher(email).matches();
    }

    // Método para verificar si el número de celular ya está registrado en la base de datos
    public boolean isNumeroRegistrado(String numero) {
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();  // Obtiene una instancia de la base de datos en modo lectura
        String[] projection = {"Celular"};  // Columnas a consultar
        String selection = "Celular = ?";  // Clausula WHERE
        String[] selectionArgs = {numero};  // Valores de la clausula WHERE
        Cursor cursor = readableDatabase.query("articulos", projection, selection, selectionArgs, null, null, null);
        boolean isRegistrado = cursor.getCount() > 0;
        cursor.close();  // Cierra el cursor
        readableDatabase.close();  // Cierra la base de datos
        return isRegistrado;
    }

    // Método para verificar si el correo electrónico ya está registrado en la base de datos
    public boolean isCorreoRegistrado(String correo) {
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();  // Obtiene una instancia de la base de datos en modo lectura
        String[] projection = {"Correo"};  // Columnas a consultar
        String selection = "Correo = ?";  // Clausula WHERE
        String[] selectionArgs = {correo};  // Valores de la clausula WHERE
        Cursor cursor = readableDatabase.query("articulos", projection, selection, selectionArgs, null, null, null);
        boolean isRegistrado = cursor.getCount() > 0;
        cursor.close();  // Cierra el cursor
        readableDatabase.close();  // Cierra la base de datos
        return isRegistrado;
    }

    // Método para realizar la validación de los campos de entrada
    public void validacion() {
        String Nom = et1.getText().toString();  // Obtiene el valor del campo Nombre
        String Cel = et2.getText().toString();  // Obtiene el valor del campo Celular
        String Cor = et3.getText().toString();  // Obtiene el valor del campo Correo
        String pin = et4.getText().toString();  // Obtiene el valor del campo Pin
        String con = et5.getText().toString();  // Obtiene el valor del campo Confirmar

        if (Nom.isEmpty()) {
            et1.setError("Campo obligatorio");  // Establece un mensaje de error en el campo Nombre
        }

        if (Cel.isEmpty()) {
            et2.setError("Campo obligatorio");  // Establece un mensaje de error en el campo Celular
        }

        if (Cor.isEmpty()) {
            et3.setError("Campo obligatorio");  // Establece un mensaje de error en el campo Correo
        }

        if (pin.isEmpty()) {
            et4.setError("Campo obligatorio");  // Establece un mensaje de error en el campo Pin
        }

        if (con.isEmpty()) {
            et5.setError("Campo obligatorio");  // Establece un mensaje de error en el campo Confirmar
        }
    }
}
