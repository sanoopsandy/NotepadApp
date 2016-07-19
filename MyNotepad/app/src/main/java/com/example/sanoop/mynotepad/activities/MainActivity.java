package com.example.sanoop.mynotepad.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.sanoop.mynotepad.R;
import com.example.sanoop.mynotepad.constants.Constants;
import com.example.sanoop.mynotepad.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private ListView obj;
    DatabaseHelper mydb;
    ListView mylist;
    Menu menu;
    Button btnNewNote;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    Context context = this;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Notes");
        mydb = new DatabaseHelper(this);
        btnNewNote = (Button) findViewById(R.id.btn_new_note);
        mylist = (ListView) findViewById(R.id.listView1);
        Cursor c = mydb.fetchAll();
        if (c.getCount() == 0){
            mylist.setVisibility(View.GONE);
            btnNewNote.setVisibility(View.VISIBLE);
        }else {
            mylist.setVisibility(View.VISIBLE);
            btnNewNote.setVisibility(View.GONE);
        }
        String[] fieldNames = new String[] {
                Constants.TITLE,
                Constants.ID
        };
        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(getApplicationContext(),
                        NoteEditorActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();
            }
        });
        int[] display = new int[] { R.id.txt_title, R.id.txt_idrow };
        adapter = new SimpleCursorAdapter(this, R.layout.row_note_list, c, fieldNames,
                display, 0);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                LinearLayout linearLayoutParent = (LinearLayout) arg1;
                LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent
                        .getChildAt(0);
                TextView m = (TextView) linearLayoutChild.getChildAt(1);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id",
                        Integer.parseInt(m.getText().toString()));
                Intent intent = new Intent(getApplicationContext(),
                        NoteEditorActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(getApplicationContext(),
                        NoteEditorActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
