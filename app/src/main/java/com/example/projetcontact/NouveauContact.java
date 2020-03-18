package com.example.projetcontact;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class NouveauContact extends AppCompatActivity {

    private DbContact Db;
    private int nbTache=1;
    private ActionMode actionMode;

    EditText nom;
    EditText prenom;
    EditText tel;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouveaucontact);

        nom = (EditText) findViewById(R.id.Nom);
        prenom = (EditText) findViewById(R.id.Prenom);
        tel = (EditText) findViewById(R.id.Tel);
        Db=new DbContact(this);
        Db.open();

    }
    public void nouveauContact(View view) {

        Db.createContact(nom.getText().toString(), prenom.getText().toString(),"trg",tel.getText().toString(),false);
        startActivity(new Intent(this, MainActivity.class));


    }



}
