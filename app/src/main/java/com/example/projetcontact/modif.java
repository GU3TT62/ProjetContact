package com.example.projetcontact;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class modif  extends AppCompatActivity {

    private DbContact Db;


    EditText nom;
    EditText prenom;
    EditText tel;
    EditText adresse;
    EditText mail;

    Boolean fav;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modif);

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
}

