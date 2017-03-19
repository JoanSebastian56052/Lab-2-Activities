package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{
    private static final int REQUEST_CODE = 1;
    private AutoCompleteTextView userView;
    private EditText passwordView;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public void onResume() {
        super.onResume();
        userView.setError(null);
        passwordView.setError(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userView = (AutoCompleteTextView) findViewById(R.id.user);
        passwordView=(EditText) findViewById(R.id.password);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        Cursor search = db.rawQuery("select * from"+StatusContract.TABLE_LOGIN, null);
        if(search.moveToFirst()){
            Intent newActivity = new Intent(this, NavigationDrawer.class);
            startActivity(newActivity);
        }
        db.close();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String u="", p="";
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if(data.hasExtra("user")&&data.hasExtra("pass")) {
                u = data.getExtras().getString("user");
                p = data.getExtras().getString("pass");
            }
            if(!u.equals(".")) {
                userView.setText(u);
                passwordView.setText(p);
                Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "El registro no se puedo realiar", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void signUpLogin(View v) {
        Intent newActivity = new Intent(this, SignUpActivity.class);
        startActivityForResult(newActivity, REQUEST_CODE);
    }
    public void signInLogin(View v) throws InterruptedException {
        String user = userView.getText().toString();
        String password = passwordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(user)) {
            userView.setError("Campo Requerido");
            focusView = userView;
            cancel = true;
        } else if(TextUtils.isEmpty(password)) {
            passwordView.setError("Campo Requerido");
            focusView = passwordView;
            cancel = true;
        } else if(!isPassword(user,password)) {
            passwordView.setError("Usuario o contraseña incorrectos");
            focusView=passwordView;
            cancel = true;
        }
        if(cancel) {
            focusView.requestFocus();
        } else {
            signUpLogin();
        }
    }
    public boolean isPassword(String sName, String pass) {
        db = dbHelper.getWritableDatabase();
        Cursor search = db.rawQuery("select * from"+StatusContract.TABLE_USER+"where nickname='"+sName+"'",null);
        String validation="";
        if(search.moveToFirst()) {
            do {
                validation = search.getString(3);
            } while(search.moveToFirst());
        }
        db.close();
        if(pass.equals(validation)) {
            return true;
        }
        return false;
    }

    public void signUpLogin() throws InterruptedException {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StatusContract.Column_Login.ID, (1));
        values.put(StatusContract.Column_Login.NAME, userView.getText().toString());
        db.insertWithOnConflict(StatusContract.TABLE_LOGIN, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        Intent newActivity = new Intent(this, NavigationDrawer.class);
        startActivity(newActivity);
    }
}