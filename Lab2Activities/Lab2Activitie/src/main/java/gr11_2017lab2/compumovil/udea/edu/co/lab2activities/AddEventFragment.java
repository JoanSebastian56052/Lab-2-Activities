package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {
    Bitmap pict;
    private static final int REQUEST_CODE_GALLERY=1;
    private static final int REQUEST_CODE_CAMERA=2;
    private ImageView targetImageR;
    DBHelper dbH;
    SQLiteDatabase db;
    static final int PICK_REQUESET=1337;
    Uri contact=null;
    Button btnR;
    EditText[]txtValidarR=new EditText[6];
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        dbH = new DBHelper(getActivity().getBaseContext());
        view = inflater.inflate(R.layout.fragment_add_event, container, false);
        pict = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.ic_arrive);
        targetImageR = (ImageView) view.findViewById(R.id.LugarImagen);
        targetImageR.setImageBitmap(pict);
        txtValidarR[0] = (EditText) view.findViewById(R.id.NEVENTO);
        txtValidarR[1] = (EditText) view.findViewById(R.id.RESPONSABLE);
        txtValidarR[2] = (EditText) view.findViewById(R.id.PUNTUACION);
        txtValidarR[3] = (EditText) view.findViewById(R.id.FECHA);
        txtValidarR[4] = (EditText) view.findViewById(R.id.UBICACION);
        txtValidarR[5] = (EditText) view.findViewById(R.id.DESCRIPCION);

        btnR = (Button) view.findViewById(R.id.buttonLugar);
        btnR.setEnabled(false);

        TextWatcher btnActivation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (verificarVaciosSinMenssageR(txtValidarR)) {
                    btnR.setEnabled(true);
                } else {
                    btnR.setEnabled(false);
                }
            }
        };
        for (int n=0;n<txtValidarR.length;n++){
            txtValidarR[n].addTextChangedListener(btnActivation);
        }
        return view;
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (requestCode==PICK_REQUESET){
            if (resultCode==getActivity().RESULT_OK){
                contact=data.getData();
            }
        }
        if (resultCode==getActivity().RESULT_OK && (requestCode==REQUEST_CODE_GALLERY|| requestCode==REQUEST_CODE_CAMERA)){
            try{
                Uri targetUri=data.getData();
                pict=redimensionarImagenMaximo(BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri)),400,350);
                targetImageR.setImageBitmap(pict);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    public boolean verificarVaciosSinMenssageR(EditText[] txtValidarR){
        for (int i=0;i<txtValidarR.length;i++){
            if (txtValidarR[i].getText().toString().isEmpty()){
                return false;
            }
        }
        return false;
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,outputStream);
        return outputStream.toByteArray();
    }
    public void ValidarEventos(){
        db=dbH.getWritableDatabase();
        ContentValues values =new ContentValues();
        Cursor search = db.rawQuery("select count(*)from"+ StatusContract.TABLE_EVENT,null);
        search.moveToFirst();
        int aux=Integer.parseInt(search.getString(0));
        values.put(StatusContract.Column_Event.ID,(aux +1));
        values.put(StatusContract.Column_Event.NEVENT, txtValidarR[0].getText().toString());
        values.put(StatusContract.Column_Event.RESPONSABLE, txtValidarR[1].getText().toString());
        values.put(StatusContract.Column_Event.PUNTUACION, txtValidarR[2].getText().toString());
        values.put(StatusContract.Column_Event.FECHA, txtValidarR[3].getText().toString());
        values.put(StatusContract.Column_Event.UBICACION, txtValidarR[4].getText().toString());
        values.put(StatusContract.Column_Event.DESCRIPCION, txtValidarR[5].getText().toString());
        search = db.rawQuery("select * from " + StatusContract.TABLE_LOGIN, null);
        search.moveToFirst();
        values.put(StatusContract.Column_Event.PICTURE, getBitmapAsByteArray(pict));
        db.insertWithOnConflict(StatusContract.TABLE_EVENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }
    public void ClickGalleryR() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    public void ClickCameraR() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }
    public Bitmap redimensionarImagenMaximo (Bitmap mBitmap, float newWidth, float newHeight) {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth)/width;
        float scaleHeight = ((float)newHeight)/height;
        Matrix matriz = new Matrix();
        matriz.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matriz, false);
    }
}
