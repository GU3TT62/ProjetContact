package com.example.projetcontact;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class NouveauContact extends AppCompatActivity {

    private DbContact Db;
    private int nbTache=1;
    private ActionMode actionMode;

    EditText nom;
    EditText prenom;
    EditText tel;
    EditText adresse;
    Boolean fav;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouveaucontact);

        nom = (EditText) findViewById(R.id.Nom);
        prenom = (EditText) findViewById(R.id.Prenom);
        tel = (EditText) findViewById(R.id.Tel);
        adresse = (EditText) findViewById(R.id.adresse);
        Db=new DbContact(this);
        Db.open();
        final CheckBox checkBox = (CheckBox) findViewById(R.id.fav);
        if (checkBox.isChecked()) {
            fav=true;
        }else{
            fav=false;
        }

    }
    public void nouveauContact(View view) {

        Db.createContact(nom.getText().toString(), prenom.getText().toString(),adresse.getText().toString(),tel.getText().toString(),fav);
        startActivity(new Intent(this, MainActivity.class));


    }



}
