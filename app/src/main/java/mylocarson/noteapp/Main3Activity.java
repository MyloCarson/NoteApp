package mylocarson.noteapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import mylocarson.noteapp.utils.NoteAppContract;
import mylocarson.noteapp.utils.NoteAppDbHelper;

public class Main3Activity extends AppCompatActivity {
    EditText noteTitleEditText, noteEditText;
    NoteAppDbHelper noteAppDbHelper;
    SQLiteDatabase db;

    private int EDIT_NOTE_ID = 1000;
    private boolean isEditNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteEditText = (EditText)findViewById(R.id.noteText);
        noteTitleEditText=(EditText)findViewById(R.id.noteTitle);

        noteAppDbHelper = new NoteAppDbHelper(this);
        db = noteAppDbHelper.getWritableDatabase();

        Intent intent = getIntent();
        if (intent.getExtras()!=null){
            isEditNote = true;
            int id = intent.getIntExtra(MainActivity.NOTE_ID,1000);
            EDIT_NOTE_ID = id;
            String selection = NoteAppContract.NoteAppEntry._ID + " = ?";
            String [] selectionArgs = {Integer.toString(id)};
            String [] projections = {NoteAppContract.NoteAppEntry.COLUMN_NAME_NOTE, NoteAppContract.NoteAppEntry.COLUMN_NAME_TITLE};
            Cursor cursor = db.query(NoteAppContract.NoteAppEntry.TABLE_NAME,projections,selection,selectionArgs,null,null,null,null);

            while (cursor.moveToNext()){
                noteEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(NoteAppContract.NoteAppEntry.COLUMN_NAME_NOTE)));
                noteTitleEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(NoteAppContract.NoteAppEntry.COLUMN_NAME_TITLE)));
            }
            cursor.close();
        }

    }

    @Override
    public void onBackPressed() {
        Log.e("isEditable",Boolean.toString(isEditNote));
        if (isEditNote){
            updateNote();
        }else{
            saveNote();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case   R.id.save:
                if (isEditNote){
                    updateNote();
                }else{
                    saveNote();
                }
                break;
        }
        return  true;
    }

    private void saveNote(){
        if (noteEditText.getText().toString().isEmpty() && noteEditText.getText().toString().trim().length() ==0){
            noteEditText.setError("Enter note");
        }else{
            String note = noteEditText.getText().toString();
            String title = noteTitleEditText.getText().toString();
            if (title.isEmpty() ){
                if (note.length()>10){
                title = note.substring(0,10);}
                else{
                    title = note;
                }
            }


            ContentValues cv = new ContentValues();
            cv.put(NoteAppContract.NoteAppEntry.COLUMN_NAME_TITLE, title);
            cv.put(NoteAppContract.NoteAppEntry.COLUMN_NAME_NOTE,note);

            long new_primaryKey = db.insert(NoteAppContract.NoteAppEntry.TABLE_NAME,null,cv);
            if (new_primaryKey!=0){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();

            }

        }
    }

    private void updateNote(){
        if (noteEditText.getText().toString().isEmpty() && noteEditText.getText().toString().trim().length() ==0){
            noteEditText.setError("Enter note");
        }else{
            String note = noteEditText.getText().toString();
            String title = noteTitleEditText.getText().toString();
            if (title.isEmpty() ){
                if (note.length()>10){
                    title = note.substring(0,10);}
                else{
                    title = note;
                }

            }

            ContentValues cv = new ContentValues();
            cv.put(NoteAppContract.NoteAppEntry.COLUMN_NAME_TITLE, title);
            cv.put(NoteAppContract.NoteAppEntry.COLUMN_NAME_NOTE,note);

            String selection = NoteAppContract.NoteAppEntry._ID + " = ?";
            String [] selectionArgs = {Integer.toString(EDIT_NOTE_ID)};

            Log.e("InsertLog","About to insert!!1");
            int count =  db.update(NoteAppContract.NoteAppEntry.TABLE_NAME,cv,selection,selectionArgs);
            if (count !=0){
                Log.e("InsertLog","Inserted!!1");
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }


        }
    }
}
