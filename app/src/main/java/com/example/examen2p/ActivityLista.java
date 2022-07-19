package com.example.examen2p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.examen2p.Configuration.Configuration;
import com.example.examen2p.Contacts.Contacts;
import com.example.examen2p.Screen2.AdapterList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;

public class ActivityLista extends AppCompatActivity {

    Button btnLAtras, btnEliminar, btnActualizar;
    ListView lista;
    EditText buscar;
    ArrayList<Contacts> contacts = new ArrayList<>();
    RequestQueue requestQueue;
    Contacts cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        init();
        btnLAtras.setOnClickListener(this::onClickBack);
    }

    private void onClickBack(View view) {
        Intent ventana = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ventana);
        finish();
    }

    private void init(){
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnLAtras = findViewById(R.id.btnAtras);
        lista = findViewById(R.id.txtLLista);
        buscar = findViewById(R.id.txtBuscar);
        requestQueue = Volley.newRequestQueue(this);

        requestToApi();
        try {
            lista.setAdapter(new AdapterList(this, contacts));
        } catch (Exception ex) {
            message(ex.getMessage());
        }
    }

    private void requestToApi(){
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET,
                Configuration.Endpoint_get_all_contacts,
                null,
                this::onResponse,
                this::onErrorResponse
        );
        requestQueue.add(jsonRequest);
    }

    public void message(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void onErrorResponse(@NonNull VolleyError error) {
        message(error.getMessage());
    }

    private void onResponse(@NonNull JSONArray response) {
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject objeto = response.getJSONObject(i);
                    cont = new Contacts();
                    cont.setId(objeto.getString("id"));
                    cont.setNombre(objeto.getString("nombre"));
                    cont.setTelefono(objeto.getString("telefono"));
                    cont.setLatitud(objeto.getString("latitude"));
                    cont.setLongitud(objeto.getString("longitude"));
                    byte[] decodedBytes = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        decodedBytes = Base64.getDecoder().decode(objeto.getString("firma"));
                    }
                    cont.setFirma(decodedBytes);
                    contacts.add(cont);
                } catch (JSONException e) {
                    message(e.getMessage());
                }
            }
        }
    }
}