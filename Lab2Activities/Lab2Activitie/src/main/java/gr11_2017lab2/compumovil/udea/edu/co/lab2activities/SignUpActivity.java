package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class SignUpActivity extends Activity
{
    private TextView txt;
    private Button btn;
    private Bitmap picture;
    private boolean control=false;
    private static final int REQUEST_CODE_GALLERY=1;
    private static final int REQUEST_CODE_CAMERA=2;
    private ImageView targetImage;
    EditText[] txtValidate= new EditText[4];
    DBHelper dbh;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        picture= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_profile);
        txtValidate[4]=(EditText)findViewById(R.id.editTextConfirmPassword);
        txtValidate[2]=(EditText)findViewById(R.id.editTextPassword);
        txtValidate[0]=(EditText)findViewById(R.id.editTextUserName);
        txtValidate[1]=(EditText)findViewById(R.id.editTextEmail);
        dbh= new DBHelper(this);
        btn= (Button)findViewById(R.id.signUpButton);
        btn.setEnabled(false);
        TextWatcher btnActivation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(verificarVaciosSinMessage(txtValidate)){
                    btn.setEnabled(true);
                }else{
                    btn.setEnabled(false);
                }
            }

        };
        for (int n=0; n<txtValidate.length; n++){
            txtValidate[n].addTextChangedListener(btnActivation);
        }
        targetImage=(ImageView)findViewById(R.id.profilePicture);
        targetImage.setImageBitmap(picture);
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        if (control) {
            data.putExtra("user",txtValidate[0].getText().toString());
            data.putExtra("pass",txtValidate[0].getText().toString());
        } else {
            data.putExtra("user", ".");
            data.putExtra("pass", ".");
        }
        setResult(RESULT_OK, data);
        super.finish();
    }
    public void Validar(View v){
        View focusView=null;
        if (!verificarVacios(txtValidate)) {

        }else if (txtValidate[1].getText().toString().contains("@")){
            txtValidate[1].setError("el correo no es válido");
            focusView=txtValidate[1];
        }else if (!txtValidate[4].getText().toString().equals(txtValidate[2].getText().toString())) {
            txtValidate[4].setError("las contraseñas no coinciden");
            focusView=txtValidate[4];
        }else if (!existName(txtValidate[0].getText().toString())){
            db= dbh.getWritableDatabase();
            ContentValues values= new ContentValues();
            Cursor search= db.rawQuery("select count(*)from usuario", null);
            search.moveToFirst();
            int aux= Integer.parseInt(search.getString(0));
            values.put(StatusContract.Column_User.ID,(aux+1));
            values.put(StatusContract.Column_User.NAME, txtValidate[0].getText().toString());
            values.put(StatusContract.Column_User.PASS, txtValidate[2].getText().toString());
            values.put(StatusContract.Column_User.MAIL, txtValidate[1].getText().toString());
            values.put(StatusContract.Column_User.PICTURE, getBitmapAsByteArray(picture));
            db.insertWithOnConflict(StatusContract.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
            control=true;
            finish();
        }else{
            txtValidate[0].setError(("El usuario ya existe"));
            focusView= txtValidate[0];
        }
    }
    public boolean existName(String sName){
        db= dbh.getWritableDatabase();
        Cursor nick= db.rawQuery("select * from"+ StatusContract.TABLE_USER+"where nickname='"+sName+"'", null);
        if (nick.moveToFirst()){
            db.close();
            return true;

        }
        return false;
    }
    public boolean verificarVacios(EditText[]txtValidate){
        View focus=null;
        for (int i=0;i<txtValidate.length;i++){
            if(txtValidate[i].getText().toString().isEmpty()){
                txtValidate[i].setError("Este campo es requerido");
                focus=txtValidate[i];
                return false;
            }
        }
        return true;
    }
    public boolean verificarVaciosSinMessage(EditText[] txtValidate){
        View focus=null;
        for (int i=0;i<txtValidate.length; i++){
            if ((txtValidate[i].getText().toString().isEmpty())){
                return false;
            }
        }
        return true;
    }
    public void ClickGallery(View v){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_GALLERY);

    }
    public void ClickCamera(View v){
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,REQUEST_CODE_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK &&(requestCode==REQUEST_CODE_GALLERY || requestCode==REQUEST_CODE_CAMERA)){
            try{
                Uri targetUri=data.getData();
                picture=redimensionarImagenMaximo(BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri)),350,350);
                targetImage.setImageBitmap(picture);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,outputStream);
        return outputStream.toByteArray();

    }
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap,float newWidth,float newHeigth){
        int width=mBitmap.getWidth();
        int height=mBitmap.getHeight();
        float scaleWidth=((float)newWidth/width);
        float scaleHeight=((float)newHeigth/height);
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(mBitmap,0,0,width,height,matrix,false);

    }

}