package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {
    private ImageView targetImageR;
    DBHelper dbHelper;
    SQLiteDatabase db;
    TextView[] txtValidateR = new TextView[2];
    View view;
    public InformationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity().getBaseContext());
        view = inflater.inflate(R.layout.fragment_information, container, false);
        txtValidateR[0]=(TextView)view.findViewById(R.id.viewName);
        txtValidateR[1]=(TextView)view.findViewById(R.id.viewMail);
        targetImageR=(ImageView)view.findViewById(R.id.viewProfile);
        db = dbHelper.getWritableDatabase();
        Cursor search = db.rawQuery("select * from " + StatusContract.TABLE_LOGIN, null);
        search.moveToFirst();
        String aux = search.getString(1);
        search = db.rawQuery("select * from"+ StatusContract.TABLE_USER+"where"+StatusContract.Column_User.NAME+"='"+aux+"'",null);
        search.moveToFirst();
        txtValidateR[0].setText("Usuario: "+search.getString(1));
        txtValidateR[1].setText("E-mail: "+search.getString(2));
        byte[] auxx = search.getBlob(5);
        Bitmap pict = BitmapFactory.decodeByteArray(auxx, 0, (auxx).length);
        targetImageR.setImageBitmap(pict);
        db.close();
        return view;
    }
}
