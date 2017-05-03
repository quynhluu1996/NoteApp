package khoenguyen.note.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 5/3/2017.
 */

public class Database extends SQLiteOpenHelper {
    public static final String DB_TABLE_NAME = "NOTE";
    public static final int DB_VERSION = 1;
    public static final String _ID = "_id";
    public static final String TITLE = "TITLE";
    public static final String NOTE = "NOTE";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
    public Database(Context context) {
        super(context, DB_TABLE_NAME, null, DB_VERSION);
    }
    public static final String DATABASE_CREATE = "CREATE TABLE "
            + DB_TABLE_NAME
            + "( "
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT NOT NULL,"
            + NOTE + " TEXT NOT NULL,"
            + DATE + " TEXT NOT NULL,"
            + TIME + " TEXT NOT NULL"
            +");";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_NAME);
        onCreate(db);
    }
}
