package khoenguyen.note.Adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import khoenguyen.note.NoteActivity;
import khoenguyen.note.R;
import khoenguyen.note.SQLiteOpenHelper.NoteDTO;

/**
 * Created by Admin on 5/3/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    /*Context context;
    int resource;
    ArrayList<NoteDTO> objects;
    public NoteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NoteDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(resource,parent,false);
        TextView title = (TextView) v.findViewById(R.id.cardview_note_title);
        TextView note = (TextView) v.findViewById(R.id.note_item1);
        TextView date_time = (TextView) v.findViewById(R.id.note_item4);
        title.setText(objects.get(position).getTitle().toString());
        note.setText(objects.get(position).getNote().toString());
        date_time.setText(objects.get(position).getDate().toString() + " " + objects.get(position).getTime().toString());
        return v;
    }*/
    private ArrayList<NoteDTO> noteList;

    public NoteAdapter(ArrayList<NoteDTO> noteList) {
        this.noteList = noteList;
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int i) {
        NoteDTO noteDTO = noteList.get(i);
        noteViewHolder.title.setText(noteDTO.getTitle());
        noteViewHolder.note.setText(noteDTO.getNote());
        noteViewHolder.date_time.setText(noteDTO.getDate() + " "+ noteDTO.getTime());
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new NoteViewHolder(itemView);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView note;
        protected TextView date_time;

        public NoteViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.cardview_note_title);
            note = (TextView) v.findViewById(R.id.note_item1);
            date_time = (TextView) v.findViewById(R.id.note_item4);
        }
    }
}
