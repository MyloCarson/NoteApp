package mylocarson.noteapp.utils;

/**
 * Created by user on 17/02/2018.
 */

public class NoteAppModel {
    private int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String note;
    private String title;
}
