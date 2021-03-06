package com.example.projetcontact;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListView listViewFav;

    private DbContact Db;


    EditText nom;
    EditText prenom;
    EditText tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Recuperation des id des edit text*/
        nom =  findViewById(R.id.Nom);
        prenom =  findViewById(R.id.Prenom);
        tel =  findViewById(R.id.Tel);

        listView =  findViewById(R.id.ListContact);
        listViewFav = findViewById(R.id.ListFavs);
        listView.setOnItemClickListener(vueContactOnClick);
        listViewFav.setOnItemClickListener(vueContactOnClick);

        listView.setVisibility(View.VISIBLE);
        listViewFav.setVisibility(View.GONE);

        registerForContextMenu(listView);
        registerForContextMenu(listViewFav);


        Db=new DbContact(this);
        Db.open();
        fillData();
    }
    /*fonction pour ajouter un contact en amenant à la page NouveauContact qui nous demande les infos à saisir*/
    public void add(View view) {

        startActivity(new Intent(this, NouveauContact.class));

    }


    //Au clic sur un contact on accede aux informations de ce contact
    private ListView.OnItemClickListener vueContactOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            //on definit la page ou seront affichés les infos sur le contact
            Intent i=new Intent(MainActivity.this,VueContact.class);
            i.putExtra("ID_CONTACT",id);
            startActivity(i);

        }

    };




    @Override
    //création d'un context menu lors d'un long appui sur un item sur la liste pour savoir si on veut ajouter un contact aux favoris ou si on veut le supprimer
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);//on recupere les items du menu
    }

    @Override
    //declaration de l'option menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //déclaration du menu permettant de tout supprimer
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {//si l'id correspond à efface_tout alors on cré une boite de dialogue
            case R.id.supprimer_tout:
                new AlertDialog.Builder(MainActivity.this)//
                        .setTitle("Confirmation")//titre
                        .setMessage("Êtes-vous sûr de vouloir tout supprimer?")//message
                        .setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {//bouton non
                            public void onClick(DialogInterface dialog, int which) {//donner action au bouton
                            }
                        })
                        .setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {//bouton valide
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Db.deleteAllContact();
                                fillData();
                            }
                        })
                        .show();//permet d'afficher la box
                return true;

            case R.id.action_favs:
                //On masque la liste des contacts et on affiche celle des favoris (WIP)


                if(listViewFav.getVisibility()==View.VISIBLE){
                    listViewFav.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }else if(listView.getVisibility()==View.VISIBLE){
                    listViewFav.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }

                return true;

            case R.id.readQr:
                Intent i=new Intent(MainActivity.this,scannQR.class);
                startActivity(i);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void fillData() {
        // Curseur pour tout les contacts
        Cursor c1 = Db.fetchAllContact();

        // Curseur pour les contacts favoris
        Cursor c2 = Db.fetchFavs();
        startManagingCursor(c1);
        startManagingCursor(c2);

        String[] from = new String[] { DbContact.KEY_NOM,DbContact.KEY_PRENOM};
        int[] to = new int[] { R.id.champ2,R.id.champ1 };

        SimpleCursorAdapter contact = new SimpleCursorAdapter(this, R.layout.contact, c1, from, to);
        SimpleCursorAdapter favs = new SimpleCursorAdapter(this, R.layout.contact, c2, from, to);
        listView.setAdapter(contact);
        listViewFav.setAdapter(favs);

        //On recupere les données des contacts présents dans la BDD Ici ils sont rangés par ordre alphabétique de noms
    }


    @Override
    //déclarationdes activités du context menu
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Cursor c=Db.fetchContact(info.id);
        startManagingCursor(c);

        switch (item.getItemId()) {
            case R.id.supp://Ici on appeelle l'activité pour supprimer un contact
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmation")//titre
                        .setMessage("Êtes-vous sûr de vouloir supprimer cet item ?")//message
                        .setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener(){//bouton non
                            public void onClick(DialogInterface dialog, int which){//donner action au bouton
                            }
                        })
                        .setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Db.deleteContact(info.id);
                                fillData();
                            }
                        })
                        .show();
                return true;
            case R.id.appel://ICI on appelle le contact via le menu contextuel


                String appel=c.getString(c.getColumnIndex(DbContact.KEY_TEL));

                Intent callIntent=new Intent(Intent.ACTION_DIAL);

                callIntent.setData(Uri.parse("tel:"+appel));
                startActivity(callIntent);

                return true;
            case R.id.sms://ICI on envoie un sms le contact via le menu contextuel

                String sms=c.getString(c.getColumnIndex(DbContact.KEY_TEL));

                Intent smsIntent=new Intent(Intent.ACTION_VIEW);

                smsIntent.setData(Uri.parse("sms:"+sms));
                startActivity(smsIntent);

                return true;
            case R.id.mail://ICI on envoie un mail le contact via le menu contextuel

                String mail=c.getString(c.getColumnIndex(DbContact.KEY_MAIL));

                Intent mailIntent=new Intent(Intent.ACTION_SEND);
                mailIntent.setType("message/rfc822");
                mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail});
                startActivity(mailIntent);

                return true;
            case R.id.localiser://ICI on localise le contact via le menu contextuel

                String loc=(String)c.getString(c.getColumnIndex(DbContact.KEY_ADRESS));

                Intent loca = new Intent(Intent.ACTION_VIEW);
                loca.putExtra(SearchManager.QUERY,"");
                Uri location = Uri.parse("geo:0,0?q="+Uri.encode(loc));
                loca.setData(location);
                startActivity(loca);

                return true;
            case R.id.menumodif://ici on appelle la page de modification d'un contact
                Intent intent=new Intent(MainActivity.this,modif.class);
                intent.putExtra("ID_CONTACT",info.id);
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }










}