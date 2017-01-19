package instinctools.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by orion on 30.12.16.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.TABLE_REPOSITORIES_CREATE);
        db.execSQL(DBConstants.TABLE_REPOSITORY_OWNER_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}