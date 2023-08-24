package com.example.proyectocamara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {

    Toast toast;
    CharSequence text;
    int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        text = "Registro con exito";
        duration = Toast.LENGTH_SHORT;
    }

    public void registroDatos(View view) {
        Intent i = new Intent(this, MainActivity.class );
        toast = Toast.makeText(MainActivity3.this, text, duration);//muestra mensaje al registrarse con exito
        toast.show();
        startActivity(i); //despues del registro regresa a la pantalla principal
    }

    public void volver(View view) {
        Intent i = new Intent(this, MainActivity.class );
        startActivity(i);
    }

}