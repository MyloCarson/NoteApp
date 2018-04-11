package mylocarson.noteapp.utils;

import android.provider.BaseColumns;

/**
 * Created by user on 17/02/2018.
 */

public class NoteAppContract {
    private NoteAppContract (){}

    public static class NoteAppEntry implements BaseColumns{
        public static final String TABLE_NAME= "note_app";
        public static final String COLUMN_NAME_TITLE ="title";
        public static final String COLUMN_NAME_NOTE ="note";

    }
}
