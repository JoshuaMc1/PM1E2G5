package com.example.examen2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.examen2p.Configuration.Configuration;
import com.example.examen2p.Screen1.CaptureBitmap;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button btnLimpiar, btnVer, btnGuardar, btnGPS;
    EditText txtNombre, txtTelefono, txtLongitud, txtLatitud;
    CaptureBitmap firma;
    private RequestQueue requestQueue;
    String nuevaVariable = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnLimpiar.setOnClickListener(this::onClickEraseSignature);
        btnGuardar.setOnClickListener(this::onClickSaveContact);
        btnVer.setOnClickListener(this::onClickGoScreen2);
        btnGPS.setOnClickListener(this::onClickGPS);
    }

    private void onClickGPS(View view) {
        getLocations();
    }

    private void onClickGoScreen2(View view) {
        Intent ventana = new Intent(getApplicationContext(), ActivityLista.class);
        startActivity(ventana);
        finish();
    }

    private void onClickSaveContact(View view) {
        if (validateEmptyFields(txtNombre) && validateEmptyFields(txtTelefono) && validateEmptyFields(txtLatitud) && validateEmptyFields(txtLongitud)){
            if(firma.getBytes().length > 8020){
                saveContact();
            } else message("Debe ingresar su firma");
        } else message("Hay campos vacios");
    }

    private void onClickEraseSignature(View view) {
        firma.ClearCanvas();
    }

    private void saveContact(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject objeto;
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("nombre", txtNombre.getText().toString());
        parametros.put("telefono", txtTelefono.getText().toString());
        parametros.put("longitude", txtLongitud.getText().toString());
        parametros.put("latitude", txtLatitud.getText().toString());
        parametros.put("firma", convertToBase64());
        objeto = new JSONObject(parametros);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Configuration.Endpoint_create_contact, objeto,
                this::onResponseCreateContacts,
                this::onErrorResponseCreateContacts);
        requestQueue.add(jsonObjectRequest);
    }

    private String convertToBase64(){
        return Base64.encodeToString(firma.getBytes(), Base64.DEFAULT);
    }

    private void getLocations() {
        if (checkIfLocationOpened()) {
            try {
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(500);
                locationRequest.setFastestInterval(1000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
                } else {
                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);
                                    if(locationResult != null && locationResult.getLocations().size() > 0){
                                        int locationIndex = locationResult.getLocations().size() - 1;
                                        txtLatitud.setText(String.valueOf(locationResult.getLocations().get(locationIndex).getLatitude()));
                                        txtLongitud.setText(String.valueOf(locationResult.getLocations().get(locationIndex).getLongitude()));
                                    }
                                }
                            }, Looper.getMainLooper());
                }
            } catch (Exception ex) {
                message("Error: " + ex.getMessage());
            }
        } else message("Debe activar el GPS");
    }

    private void init(){
        btnLimpiar = findViewById(R.id.btnS1BorrarFirma);
        btnVer = findViewById(R.id.btnS1Ver);
        btnGPS = findViewById(R.id.btnS2GPS);
        btnGuardar = findViewById(R.id.btnS1Guardar);
        txtNombre = findViewById(R.id.txtS1Nombre);
        txtTelefono = findViewById(R.id.txtS1Telefono);
        txtLatitud = findViewById(R.id.txtS1Latitud);
        txtLongitud = findViewById(R.id.txtS1Longitud);
        firma = findViewById(R.id.txtS1Firma);
    }

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return provider.contains("gps") || provider.contains("network");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocations();
            } else message("Permiso denegado");
        }
    }

    public void cleanFields(){
        txtNombre.setText("");
        txtTelefono.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        firma.ClearCanvas();
    }

    private boolean validateEmptyFields(EditText object){
        return object.length() > 0;
    }

    public void message(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    private void onResponseCreateContacts(JSONObject response) {
        try {
            if (response.length() > 0) {
                message("Code => " + response.getString("Code") + "\nMeesage => " + response.getString("Message"));
            } else message("No hubo respuesta.");
        } catch (JSONException ex) {
            message("Error 1: " + ex.getMessage());
            ex.printStackTrace();
        }
        cleanFields();
    }

    private void onErrorResponseCreateContacts(VolleyError error) {
        message("Error 2: " + error.getMessage());
        error.printStackTrace();
    }
}