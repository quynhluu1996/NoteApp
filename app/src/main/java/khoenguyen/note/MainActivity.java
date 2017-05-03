package khoenguyen.note;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import khoenguyen.note.Adapter.NoteAdapter;
import khoenguyen.note.DateTime.SetDateTime;
import khoenguyen.note.SQLiteOpenHelper.NoteDAO;
import khoenguyen.note.SQLiteOpenHelper.NoteDTO;

public class MainActivity extends AppCompatActivity {
    CardView cardViewNote;
    public static final int REQUEST_CODE_NEW = 1;
    public static final int RESULT_CODE_NEW = 2;
    ArrayList<NoteDTO> dsNote;
    NoteDAO noteDAO;
    NoteAdapter adapter;
    RecyclerView listNote;
    //NoteDAO noteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //chay logo tren action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        noteDAO = new NoteDAO(this);
        noteDAO.open();
        dsNote = noteDAO.importListNote();
        listNote = (RecyclerView) findViewById(R.id.cardList);
        adapter = new NoteAdapter(dsNote);
        adapter.notifyDataSetChanged();
        listNote.setAdapter(adapter);

    }

    //Tao icon + tren action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //chuyen sang activity_note khi click '+'
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW);
                break;
        }
        return true;
    }
    /*@Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNote:
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                NoteDTO newNote = dsNote.get(menuInfo.position);
                noteDAO.addNote(newNote);
                adapter.notifyDataSetChanged();
                startActivityForResult(intent, REQUEST_CODE_NEW);
                break;
        }
        return true;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW){
            switch (resultCode){
                case RESULT_CODE_NEW:
                    NoteDTO noteDTONew = (NoteDTO) data.getSerializableExtra("note");
                    noteDAO.addNote(noteDTONew);
                    dsNote = noteDAO.importListNote();
                    dsNote = new ArrayList<NoteDTO>();
                    adapter = new NoteAdapter(dsNote);
                    adapter.notifyDataSetChanged();
                    listNote.setAdapter(adapter);

                    //Toast.makeText(MainActivity.this, studentDTONew.getName().toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
