package com.example.projetcontact;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListView listView2;


    private DbContact Db;


    EditText nom;
    EditText prenom;
    EditText tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        nom =  findViewById(R.id.Nom);
        prenom =  findViewById(R.id.Prenom);
        tel =  findViewById(R.id.Tel);

        listView =  findViewById(R.id.ListContact);
        listView2 =  findViewById(R.id.VueContact);

        registerForContextMenu(listView);


        Db=new DbContact(this);
        Db.open();
        fillData();
    }
    public void add(View view) {

        startActivity(new Intent(this, NouveauContact.class));

    }

        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            startActivity(new Intent(this, VueContact.class));
        }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatic private ally handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = Db.fetchAllContact();
        startManagingCursor(c);

        String[] from = new String[] { DbContact.KEY_NOM,DbContact.KEY_PRENOM};
        int[] to = new int[] { R.id.champ1,R.id.champ2 };

        String[] from2 = new String[] { DbContact.KEY_NOM,DbContact.KEY_PRENOM,DbContact.KEY_TEL, DbContact.KEY_ADRESS,DbContact.Key_FAV};
        int[] to2 = new int[] { R.id.champ1,R.id.champ2, };

        // Now create an array adapter and set it to display using our row

        SimpleCursorAdapter contact = new SimpleCursorAdapter(this, R.layout.contact, c, from, to);
        listView.setAdapter(contact);

        SimpleCursorAdapter vuecontact = new SimpleCursorAdapter(this, R.layout.vuecontact, c, from2, to2);
        listView2.setAdapter(vuecontact);

    }

    //up
    private void fillDataFav() {
        // Get all of the notes from the database and create the item list
        Cursor c = Db.fetchFavs();
        startManagingCursor(c);

        String[] from = new String[] { DbContact.KEY_NOM,DbContact.KEY_PRENOM};
        int[] to = new int[] { R.id.champ1,R.id.champ2 };



        // Now create an array adapter and set it to display using our row

        SimpleCursorAdapter contact = new SimpleCursorAdapter(this, R.layout.contact, c, from, to);
        listView.setAdapter(contact);



    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.supp:
                Db.deleteContact(info.id);
                fillData();
                return true;
            case R.id.fav:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }










}
