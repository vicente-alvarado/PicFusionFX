package com.example.proyectocamara;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    EditText edtP;
    EditText edtN;
    String edtR;
    Button btnCamara;
    Button btnSiguiente;
    CharSequence text;
    int duration;
    Bundle extras;
    Bitmap imgBitmap; //imagen capturada
    Toast toast;
    // Define una variable para almacenar la ruta de la imagen capturada
    String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtP = findViewById(R.id.edtPositivo);
        edtN = findViewById(R.id.edtNegativo);

        text = "Imagen capturada con exito";
        duration = Toast.LENGTH_SHORT;

        TextView txtUser = (TextView) findViewById(R.id.nomuser);
        Bundle bundle = getIntent().getExtras();
        txtUser.setText(bundle.getString("usuario"));

        btnCamara = findViewById(R.id.btncam);
        btnSiguiente = findViewById(R.id.btncam);
        //visor = findViewById(R.id.imageV);

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camaraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguiente(view);
            }
        });
    }


    //este codigo permite abrir la camara
    ActivityResultLauncher<Intent> camaraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        //permite visualizar la imagen tomada en la siguiente pantalla
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK){
                extras = result.getData().getExtras();
                imgBitmap = (Bitmap) extras.get("data");

                // Guardar la imagen en el sistema de archivos del dispositivo
                File imageFile = saveImageToStorage(imgBitmap);

                // Obtener la ruta de la imagen guardada como cadena
                if (imageFile != null) {
                    String imagePath = imageFile.getAbsolutePath();
                }

                toast = Toast.makeText(MainActivity2.this, text, duration);
                toast.show();//muestra un pequeño mensaje al tomar una foto con exito
            }

        }
    });

    public void siguiente(View view) {
        Intent i = new Intent(this, MainActivity4.class );
        extras.putParcelable("Bitmap", imgBitmap);
        i.putExtras(extras);//envia la imagen bitmap capturada al main activity 4
        startActivity(i);//lleva a la siguiente activity

        // Crear una instancia de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:5000") // Reemplaza BASE_URL con la URL de tu servidor Flask
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear una instancia de la interfaz ApiService
        ApiService apiService = retrofit.create(ApiService.class);

        // Crear una solicitud JSON
        // Obtener los valores de los campos de entrada
        // Obtener los valores de los campos de entrada desde activity_main2.xml
        EditText edtTextoUser = findViewById(R.id.edtTextoUser);
        EditText edtTextoUser2 = findViewById(R.id.edtTextoUser2);

        // Obtener los textos de los campos de entrada
        String promptPositivo = edtTextoUser.getText().toString();
        String promptNegativo = edtTextoUser2.getText().toString();

        // Crear una solicitud JSON con los valores adecuados
        ImageProcessingRequest request = new ImageProcessingRequest(promptPositivo, promptNegativo, imagePath);

        // Enviar la solicitud al servidor para procesar la imagen
        Call<ImageProcessingResponse> call = apiService.generateImage(request);


        // Enviar la solicitud al servidor para procesar la imagen
        call = apiService.generateImage(request);
        call.enqueue(new Callback<ImageProcessingResponse>() {
            @Override
            public void onResponse(Call<ImageProcessingResponse> call, Response<ImageProcessingResponse> response) {
                if (response.isSuccessful()) {
                    // La respuesta exitosa contiene la ruta de la imagen procesada
                    String outputPath = response.body().getOutputPath();

                    // Obtén una referencia al ImageView
                    ImageView imgDespues = findViewById(R.id.imgDespues);

                    // Cargar y mostrar la imagen procesada con Picasso
                    Picasso.get().load("http://127.0.0.1:5000" + "/get_image?output_path=" + outputPath).into(imgDespues);
                } else {
                    // Manejar errores aquí
                }
            }


            @Override
            public void onFailure(Call<ImageProcessingResponse> call, Throwable t) {
                // Manejar errores de red aquí
            }
        });
    }

    public void enviarDatos(){
        String url = "http://localhost:5000/generate_image";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    //edtP.setText(jsonObject.getString("prompt"));
                    //edtN.setText(jsonObject.getString("negative_prompt"));
                    //edtR.setText(jsonObject.getString("prompt"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        })
        {
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("prompt", edtP.getText().toString());
                params.put("negative_prompt", edtN.getText().toString());
                params.put("image_path", edtR.toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private File getImageFile() throws IOException {
        String imageName = "photo";
        String ruta = Environment.getExternalStorageDirectory().toString();
        //File storageDir = new File(ruta + "/miimagenes");
        File storageDir = new File(ruta);

        File imageFile = File.createTempFile(imageName, ".png", storageDir);
        edtR = imageFile.getAbsolutePath();
        return imageFile;
    }

    public void cerraSesion(View view) {
        Intent i = new Intent(this, MainActivity.class );
        startActivity(i);
    }

    private File saveImageToStorage(Bitmap bitmap) {
        // Comprueba si el almacenamiento externo está disponible
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Directorio donde se guardará la imagen
            File directory = new File(Environment.getExternalStorageDirectory(), "NombreDeTuDirectorio");

            // Asegúrate de que el directorio exista; si no, créalo
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Nombre del archivo de imagen
            String fileName = "captured_image.jpg";

            // Crear el archivo en el directorio
            File imageFile = new File(directory, fileName);

            try {
                // Guardar la imagen en el archivo
                FileOutputStream fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                return imageFile;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}