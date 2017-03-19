package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.provider.BaseColumns;

/**
 * Created by sebas on 19/03/17.
 */

public class StatusContract {
    public static final String DB_NAME = "lab2activities.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_USER = "usuario";
    public static final String TABLE_EVENT = "evento";
    public static final String TABLE_LOGIN = "logueado";

    public class Column_Login {
        public static final String  ID = BaseColumns._ID;
        public static final String NAME = "nickname";
    }

    public class Column_User {
        public static final String ID = BaseColumns._ID;
        public static final String NAME = "nickname";
        public static final String MAIL = "mail";
        public static final String PASS = "password";
        public static final String PICTURE = "picture";
    }
    public class Column_Event {
        public static final String ID = BaseColumns._ID;
        public static final String NEVENT = "event";
        public static final String DESCRIPCION = "descripcion";
        public static final String PUNTUACION = "puntuacion";
        public static final String RESPONSABLE = "responsable";
        public static final String UBICACION = "ubicacion";
        public static final String FECHA = "fecha";
        public static final String PICTURE = "picture";
    }
}
