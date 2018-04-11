package mylocarson.noteapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mylocarson.noteapp.adapter.CustomAdapter;
import mylocarson.noteapp.utils.CustomItemTouch;
import mylocarson.noteapp.utils.NoteAppContract;
import mylocarson.noteapp.utils.NoteAppDbHelper;
import mylocarson.noteapp.utils.NoteAppModel;

public class
MainActivity extends AppCompatActivity {
    TextView noNotes;
    RecyclerView recyclerView;
    NoteAppDbHelper noteAppDbHelper;
    ArrayList<NoteAppModel> noteList = new ArrayList<>();
    CustomAdapter customAdapter;
    CustomItemTouch.RecyclerItemTouchHelperListener recyclerItemTouchHelperListener;
    boolean confirmDelete = true;
    public static final String NOTE_ID = "note_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        noNotes = (TextView)findViewById(R.id.no_notes);
        noteAppDbHelper = new NoteAppDbHelper(this);
        final SQLiteDatabase sqLiteDatabase = noteAppDbHelper.getReadableDatabase();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Main3Activity.class);
                startActivity(intent);
                finish();
            }
        });



        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ NoteAppContract.NoteAppEntry.TABLE_NAME, null);

        if (cursor!=null){
             while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NoteAppContract.NoteAppEntry.COLUMN_NAME_TITLE));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(NoteAppContract.NoteAppEntry.COLUMN_NAME_NOTE));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(NoteAppContract.NoteAppEntry._ID));

                NoteAppModel noteAppModel = new NoteAppModel();
                noteAppModel.setId(id);
                noteAppModel.setTitle(title);
                noteAppModel.setNote(note);
//                 Log.e("NOTE!!!!!",note);

                noteList.add(noteAppModel);

            }

        }
        cursor.close();

        toggle_noNotes();

        customAdapter =new CustomAdapter( noteList);
        customAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(customAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        recyclerItemTouchHelperListener = new CustomItemTouch.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                if (viewHolder instanceof CustomAdapter.MyViewHolder){
                    if (direction == ItemTouchHelper.LEFT){
                        final NoteAppModel noteAppModel = noteList.get(viewHolder.getAdapterPosition()) ;
                        final int deleteIndex = viewHolder.getAdapterPosition();

                        String selectedTitle =  noteAppModel.getTitle();
                        String columnSelection = NoteAppContract.NoteAppEntry.COLUMN_NAME_TITLE+ " LIKE ? ";
                        String[] selectionArgs = {selectedTitle};

                        customAdapter.removeNote(viewHolder.getAdapterPosition());
                        toggle_noNotes();

                        Snackbar snackbar = Snackbar.make(recyclerView,selectedTitle + " was deleted!", Snackbar.LENGTH_SHORT);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirmDelete =  false;
                                customAdapter.restoreNote(noteAppModel,deleteIndex);
                            }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();

//                    this deletes from the db finally
//                    if (confirmDelete == true){
//                        sqLiteDatabase.delete(NoteAppContract.NoteAppEntry.TABLE_NAME,columnSelection,selectionArgs);
//                    }
                        if (snackbar.isShown() && confirmDelete){
                            sqLiteDatabase.delete(NoteAppContract.NoteAppEntry.TABLE_NAME,columnSelection,selectionArgs);
                        }
                    }else if (direction == ItemTouchHelper.RIGHT){
                        Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                        intent.putExtra(NOTE_ID,noteList.get(position).getId());
                        startActivity(intent);
                        finish();
                    }


                }
            }
        };
        CustomItemTouch customItemTouch = new CustomItemTouch(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, recyclerItemTouchHelperListener);
        new ItemTouchHelper(customItemTouch).attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onDestroy() {
        noteAppDbHelper.close();
        super.onDestroy();
    }

    public void toggle_noNotes(){
        if (noteList.size() ==0){
            noNotes.setVisibility(View.VISIBLE);
        }else{
            noNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.version:
                Snackbar.make(recyclerView,"Version 0.0.1",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.info:
                final Snackbar snackbar = Snackbar.make(recyclerView,"Swipe Left to Edit \nSwipe Right to Delete ",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setActionTextColor(Color.BLUE);
                snackbar.show();
        }
        return true;
    }
}
