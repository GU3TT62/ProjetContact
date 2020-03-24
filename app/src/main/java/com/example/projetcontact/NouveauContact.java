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
        //cette fonction permet d'envoyer les donner a mettre dans la BDD pour ajouter un contact
        Db.createContact(nom.getText().toString(), prenom.getText().toString(),adresse.getText().toString(),tel.getText().toString(),mail.getText().toString(),fav);
        startActivity(new Intent(this, MainActivity.class));


    }
  /*  nom.setOnFocusChangeListener(newOnFocusChangeListener(){
        @Override
        public void onFocusChange(View v ,boolean hasFocus){
            if(!hasFocus){
                // code to execute when EditText loses focus
                //(APPELER LA FONCTION DE VALIDATION)
            }
        }
    });*/



}
