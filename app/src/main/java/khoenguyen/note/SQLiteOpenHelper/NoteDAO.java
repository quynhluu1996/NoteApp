package khoenguyen.note.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import khoenguyen.note.Database.Database;

/**
 * Created by Admin on 5/2/2017.
 * Ket noi voi co so du lieu
 */
public class NoteDAO {
    SQLiteDatabase sqLiteDatabase;
    Database database;
    String[] column = {database._ID, database.TITLE,database.NOTE,database.DATE, database.TIME};
    public NoteDAO(Context context) {
        database = new Database(context);
    }

    public void open() {
        sqLiteDatabase = database.getWritableDatabase();
    }
    public void close(){
        database.close();
    }
    public boolean addNote(NoteDTO noteDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(database.TITLE, noteDTO.getTitle());
        contentValues.put(database.NOTE, noteDTO.getNote());
        contentValues.put(database.DATE, noteDTO.getDate());
        contentValues.put(database.TIME, noteDTO.getTime());
        long id = sqLiteDatabase.insert(database.DB_TABLE_NAME, null,contentValues);
        if(id != 0){
            return true;
        }else{
            return false;
        }
    }
    public ArrayList<NoteDTO> importListNote(){
        ArrayList<NoteDTO> listNote= new ArrayList<NoteDTO>();

        String truyvan = "SELECT * FROM " + database.DB_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(truyvan,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex(database._ID));
            //String name = cursor.getString(cursor.getColumnIndex(database.STUDENT_NAME));
            String title = cursor.getString(cursor.getColumnIndex(database.TITLE));
            String note = cursor.getString(cursor.getColumnIndex(database.NOTE));
            String date = cursor.getString(cursor.getColumnIndex(database.DATE));
            String time = cursor.getString(cursor.getColumnIndex(database.TIME));
            NoteDTO noteDTO = new NoteDTO();
            noteDTO.setId(id);
            noteDTO.setTitle(title);
            noteDTO.setNote(note);
            noteDTO.setDate(date);
            noteDTO.setTime(time);
            //noteDTO.setName(name);
            listNote.add(noteDTO);

            cursor.moveToNext();
        }
        return listNote;
    }
    public boolean delNote(NoteDTO noteDTO){
        //Delete from Table WHERE _id = st.getid();
        int test = sqLiteDatabase.delete(database.DB_TABLE_NAME, database._ID + " = " + noteDTO.getId(),null);
        if(test != 0){
            return true;
        }else{
            return false;
        }
    }
    public boolean modNote(NoteDTO noteDTO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(database.TITLE, noteDTO.getTitle().toString());
        contentValues.put(database.NOTE, noteDTO.getNote().toString());
        contentValues.put(database.DATE, noteDTO.getDate().toString());
        contentValues.put(database.TIME, noteDTO.getTime().toString());
        int test = sqLiteDatabase.update(database.DB_TABLE_NAME,contentValues,database._ID + " = "+ noteDTO.getId(), null );
        if(test != 0){
            return true;
        }else{
            return false;
        }
    }
}
