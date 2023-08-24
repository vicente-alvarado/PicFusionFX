package com.example.proyectocamara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity4 extends AppCompatActivity {

    ImageView visora;
    ImageView visord;
    MediaStore.Images photoProcesada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        visora = findViewById(R.id.imgAntes);
        Bundle extras = getIntent().getExtras();//obtiene el bitmap(imagen capturada) para mostrarlo en el Image View
        Bitmap bmp = (Bitmap) extras.getParcelable("Bitmap");
        visora.setImageBitmap(bmp);


    }


    public void volver(View view) {
        Intent i = new Intent(this, MainActivity2.class );
        startActivity(i);
    }

    public void cerraSesion(View view) {
        Intent i = new Intent(this, MainActivity.class );
        startActivity(i);
    }
}
