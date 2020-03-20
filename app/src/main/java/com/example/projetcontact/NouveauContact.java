package com.example.projetcontact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class NouveauContact extends AppCompatActivity {

    private DbContact Db;


    EditText nom;
    EditText prenom;
    EditText tel;
    EditText adresse;
    EditText mail;

    Boolean fav;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nouveaucontact);

        nom =  findViewById(R.id.Nom);
        prenom =  findViewById(R.id.Prenom);
        tel =  findViewById(R.id.Tel);
        adresse =  findViewById(R.id.adresse);
        mail =  findViewById(R.id.Mail);

        Db=new DbContact(this);
        Db.open();
        final CheckBox checkBox =  findViewById(R.id.fav);
        if (checkBox.isChecked()) {
            fav=true;
        }else{
            fav=false;
        }

    }
    public void nouveauContact(View view) {

        Db.createContact(nom.getText().toString(), prenom.getText().toString(),adresse.getText().toString(),tel.getText().toString(),mail.getText().toString(),fav);
        startActivity(new Intent(this, MainActivity.class));


    }



}
