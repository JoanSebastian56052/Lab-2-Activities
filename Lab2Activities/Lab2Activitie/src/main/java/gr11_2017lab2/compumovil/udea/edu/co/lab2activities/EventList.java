package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.support.v4.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sebas on 19/03/17.
 */

public class EventList extends ListFragment {
    DBHelper dbHelper;
    SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper =new DBHelper(getActivity().getBaseContext());
        ListarEventos();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void ListarEventos(){//operacion para listar sitios, muestra en el activity los sitios guardados
        ArrayList<String> responsable = new ArrayList();
        ArrayList<String> descripcion = new ArrayList();
        ArrayList<String> evento = new ArrayList();
        ArrayList<String> puntuacion= new ArrayList(),ids = new ArrayList();
        ArrayList<String> fecha = new ArrayList();
        ArrayList picture = new ArrayList();
        boolean control=false;
        db= dbHelper.getReadableDatabase();
        Cursor test=db.rawQuery("select * from "+StatusContract.TABLE_EVENT+" order by "+ StatusContract.Column_Event.NEVENT, null);
        if (test.moveToFirst()) {
            do{
                ids.add(test.getString(0));
                responsable.add(test.getString(1));
                descripcion.add(test.getString(2));
                evento.add(test.getString(3));
                fecha.add(test.getString(4));
                puntuacion.add(test.getString(5));
                picture.add(test.getBlob(6));
            }while(test.moveToNext());
            control=true;
        } else{
            Toast.makeText(getActivity().getBaseContext(),"Sin sitios",Toast.LENGTH_LONG).show();
        }
        db.close();
        if(control) {
            ArrayList aList=new ArrayList();
            for (int i = 0; i < ids.size(); i++) {
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("evento", "Evento: " + evento.get(i));
                hm.put("descripcion", "Descripción : " + descripcion.get(i));
                hm.put("responsable", "Responsable : " + responsable.get(i));
                hm.put("fecha", "Fecha : "+fecha.get(i));
                hm.put("puntuacion", "Puntuacion : " + puntuacion.get(i));
                hm.put("picture", BitmapFactory.decodeByteArray((byte[]) picture.get(i), 0, ((byte[]) picture.get(i)).length));
                aList.add(hm);
            }
            String from[];
            int to[];
            from = new String[]{"evento", "descripcion", "fecha", "puntuacion","responsable", "picture"};
            to = new int[]{R.id.NEVENTO, R.id.DESCRIPCION, R.id.FECHA, R.id.PUNTUACION,R.id.RESPONSABLE, R.id.LugarImagen};
            ExtendedSimpleAdapter adapter = new ExtendedSimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
            setListAdapter(adapter);
        }
    }

}
