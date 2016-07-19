package com.example.sanoop.mynotepad.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sanoop.mynotepad.R;
import com.example.sanoop.mynotepad.constants.Constants;
import com.example.sanoop.mynotepad.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteEditorActivity extends AppCompatActivity {
    private DatabaseHelper mydb;
    EditText title;
    EditText content;
    String dateString;
    Bundle extras;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        getSupportActionBar().setTitle("Note");
        title = (EditText) findViewById(R.id.etxt_title);
        content = (EditText) findViewById(R.id.etxt_content);
        mydb = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();
                String nam = rs.getString(rs.getColumnIndex(Constants.TITLE));
                String contents = rs.getString(rs.getColumnIndex(Constants.CONTENT));
                if (!rs.isClosed()) {
                    rs.close();
                }
                title.setText((CharSequence) nam);
                content.setText((CharSequence) contents);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            getMenuInflater().inflate(R.menu.menu_note_editor, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Delete this note?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        mydb.deleteNotes(id_To_Update);
                                        Toast.makeText(NoteEditorActivity.this, "Deleted Successfully",Toast.LENGTH_SHORT).show();
                                        goToHomePage();
                                    }
                                })
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                    }
                                });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();
                return true;
            case R.id.menu_save:
                Bundle extras = getIntent().getExtras();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c.getTime());
                dateString = formattedDate;
                if (extras != null) {
                    int Value = extras.getInt("id");
                    if (Value > 0) {
                        if (content.getText().toString().trim().equals("")
                                || title.getText().toString().trim().equals("")) {
                            Toast toast = Toast.makeText(this, "Please fill in name of the note", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            if (mydb.updateNotes(id_To_Update, title.getText()
                                    .toString(), dateString, content.getText()
                                    .toString())) {
                                goToHomePage();
                                Toast toast = Toast.makeText(this, "Your note Updated Successfully!!!", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(this, "There's an error. That's all I can tell. Sorry!", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    } else {
                        if (content.getText().toString().trim().equals("")
                                || title.getText().toString().trim().equals("")) {
                            Toast toast = Toast.makeText(this, "Please fill in content of the note", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            if (mydb.insertNotes(title.getText().toString(), dateString,
                                    content.getText().toString())) {
                                goToHomePage();
                                Toast toast = Toast.makeText(this, "Added Successfully.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(this, "Unfortunately Task Failed.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        goToHomePage();
        return;
    }

    public void goToHomePage(){
        Intent intent = new Intent(
                getApplicationContext(),
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
