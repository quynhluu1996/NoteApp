package khoenguyen.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import khoenguyen.note.Adapter.NoteAdapter;
import khoenguyen.note.DateTime.SetDateTime;
import khoenguyen.note.SQLiteOpenHelper.NoteDAO;
import khoenguyen.note.SQLiteOpenHelper.NoteDTO;

/**
 * Created by Admin on 4/28/2017.
 */

public class NoteActivity extends AppCompatActivity {
    private TextView txtDate, txtTime, timeDefault;
    private EditText edTitle, edNote;
    ImageButton imageButtonAlarm;
    private ImageView mImageView;
    private SetDateTime setDateTime;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;
    private static final String IMAGE_DIRECTORY = "/note";
    NoteDTO noteDTO;

    NoteDAO noteDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        //chay logo tren action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getFormWidgets();
        setDateTime.getDefaultInfor();
        setDateTime.addEventFormWidgets();

        noteDTO = (NoteDTO) getIntent().getSerializableExtra("note");
        //dsNote = noteDAO.importListNote();

    }
    //Tao icons tren action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }
    //load cac control theo id
    private void getFormWidgets() {
        edTitle = (EditText) findViewById(R.id.title);
        edNote = (EditText)findViewById(R.id.note);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        timeDefault = (TextView) findViewById(R.id.timeDefault);
        imageButtonAlarm = (ImageButton) findViewById(R.id.alarm_btn);
        mImageView = (ImageView) findViewById(R.id.cameraImport);
        setDateTime = new SetDateTime(this, txtDate, txtTime, timeDefault, imageButtonAlarm);
    }

    //Todo: luu du lieu tro lai main
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.check:
                //Todo
                noteDTO.setTitle(edTitle.getText().toString());
                noteDTO.setNote(edNote.getText().toString());
                noteDTO.setDate(txtDate.getText().toString());
                noteDTO.setTime(txtTime.getText().toString());

                Intent data = new Intent();
                data.putExtra("note", noteDTO);
                setResult(MainActivity.RESULT_CODE_NEW, data);
                finish();
                //noteDTO = (StudentDTO) getIntent().getSerializableExtra("sinhvien");
                //dsNote.add(noteDTO);
                //noteDAO.addNote(noteDTO);
                /*dsStudent.add(studentDTO);
                boolean kiemtra = studentDAO.addStudent(studentDTO);

                if(kiemtra){
                    Toast.makeText(MainActivity.this, "Them thanh cong!",Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }*/
                /*Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);*/
                return true;
            case R.id.colors:
                ColorChooserDialog dialog = new ColorChooserDialog(this);
                dialog.setTitle("Choose Color");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        //Todo: do whatever you want to with the values
                    }
                });
                //customize the dialog however you want
                dialog.show();
                return true;
            case R.id.camera:
                showPictureDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPictureDialog(){
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Take photo",
                "Choose photo" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    //take a photo with camera app
    protected void dispatchTakePictureIntent() {
        mImageView = (ImageView)findViewById(R.id.cameraImport);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    //Get the thumbnail
    //return Intent delivered to onActivityResult() as a small Bitmap in the extras, under the key "data".
    // The following code retrieves this image and displays it in an ImageView.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            saveImage(imageBitmap);
        }else if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
            if (data != null) {
                //Get data from Uri
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    mImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void choosePhotoFromGallary() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(mediaScanIntent, REQUEST_GALLERY);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
