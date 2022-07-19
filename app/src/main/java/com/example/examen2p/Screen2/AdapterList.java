package com.example.examen2p.Screen2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examen2p.Contacts.Contacts;
import com.example.examen2p.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class AdapterList extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<Contacts> listaDatos;
    TextView id, nombre, telefono, latitud, longitud;
    ImageView firma;

    public AdapterList(Context context, ArrayList<Contacts> data){
        this.context = context;
        this.listaDatos = data;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.item_list, null);
        try {
            init(vista);
            id.setText(listaDatos.get(i).getId());
            nombre.setText(listaDatos.get(i).getNombre());
            telefono.setText(listaDatos.get(i).getTelefono());
            latitud.setText(listaDatos.get(i).getLatitud());
            longitud.setText(listaDatos.get(i).getLongitud());
            Bitmap bitmap = null;
            ByteArrayInputStream bais = new ByteArrayInputStream(listaDatos.get(i).getFirma());
            bitmap = BitmapFactory.decodeStream(bais);
            firma.setImageBitmap(bitmap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return vista;
    }

    @Override
    public int getCount() {
        return listaDatos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private void init(View vista){
        id = vista.findViewById(R.id.txtLID);
        nombre = vista.findViewById(R.id.txtLNombre);
        telefono = vista.findViewById(R.id.txtLTelefono);
        latitud = vista.findViewById(R.id.txtLLatitud);
        longitud = vista.findViewById(R.id.txtLLongitud);
        firma = vista.findViewById(R.id.txtLFirma);
    }
}
