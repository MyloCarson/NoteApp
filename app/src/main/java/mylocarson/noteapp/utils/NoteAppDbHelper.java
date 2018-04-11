package mylocarson.noteapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 17/02/2018.
 */

public class NoteAppDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "noteapp.db";

    private static final String CREATE_TABLE_STATEMENT = " CREATE TABLE " + NoteAppContract.NoteAppEntry.TABLE_NAME + " ( "+
            NoteAppContract.NoteAppEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + NoteAppContract.NoteAppEntry.COLUMN_NAME_TITLE+
            " TEXT NOT NULL , "+ NoteAppContract.NoteAppEntry.COLUMN_NAME_NOTE + " TEXT NOT NULL );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteAppContract.NoteAppEntry.TABLE_NAME;
    public NoteAppDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
