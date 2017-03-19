package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String TAG = DBHelper.class.getSimpleName();


    public DBHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlLogin = String.format("create table %s(%s int primary key, %s text unique)",
                StatusContract.TABLE_LOGIN,
                StatusContract.Column_Login.ID,
                StatusContract.Column_Login.NAME);
        db.execSQL(sqlLogin);
        String sqlUser = String.format("create table %s(%s int primary key, %s text unique, %s text, %s text, %s blob)",
                StatusContract.TABLE_USER,
                StatusContract.Column_User.ID,
                StatusContract.Column_User.NAME,
                StatusContract.Column_User.MAIL,
                StatusContract.Column_User.PASS,
                StatusContract.Column_User.PICTURE);
        db.execSQL(sqlUser);
        String sqlEvent = String.format("create table %s(%s int primary key, %s text unique, %s text, %s text, %s text, " +
                        "%s text, %s text, %s blob )",
                StatusContract.TABLE_EVENT,
                StatusContract.Column_Event.ID,
                StatusContract.Column_Event.NEVENT,
                StatusContract.Column_Event.RESPONSABLE,
                StatusContract.Column_Event.PUNTUACION,
                StatusContract.Column_Event.FECHA,
                StatusContract.Column_Event.UBICACION,
                StatusContract.Column_Event.DESCRIPCION,
                StatusContract.Column_Event.PICTURE);
        db.execSQL(sqlEvent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exist " + StatusContract.TABLE_USER);
        db.execSQL("drop table if exist " + StatusContract.TABLE_EVENT);
        db.execSQL("drop table if exist " + StatusContract.TABLE_LOGIN);
    }
}