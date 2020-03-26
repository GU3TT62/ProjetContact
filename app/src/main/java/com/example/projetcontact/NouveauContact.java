package com.example.projetcontact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


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
        String name = nom.getText().toString();
        String phone = tel.getText().toString();
        int favInt=0;
        if(fav){
            favInt=1;
        }
        if(ValidateName(name)&&ValidatePhone(phone)){//On verifie que les champs nom et telephone ne sont pas vide
            Db.createContact(name, prenom.getText().toString(),adresse.getText().toString(),phone,mail.getText().toString(),fav);
            startActivity(new Intent(this, MainActivity.class));

        }

        }




    public boolean ValidateName(String name) {
        name = nom.getText().toString();

        boolean validate = true;
        if(name.equals("")){
            String message="Le champ nom est obligatoire";
            validate=false;
            SendMessage(message);
        }
        return validate;
    }
    public boolean ValidatePhone(String phone){
        phone = tel.getText().toString();

        boolean validate = true;
         if(phone.equals("")){
            String message="Le champ telephone est obligatoire";
            validate=false;
             SendMessage(message);

         }else if(validate==true){
            try{
                int test = Integer.parseInt(phone);
            }catch(NumberFormatException e){
                String message="Il faut que le telephone soit un nombre pour que cela fonctionne";
                SendMessage(message);
                validate=false;
            }
         }
        return validate;
    }

    private void SendMessage(String message) {
        Context context = getApplicationContext();
        int duration =Toast.LENGTH_SHORT;

        Toast toast =Toast.makeText(context, message, duration);
        toast.show();
    }


}
