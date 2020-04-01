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


    }
    public void nouveauContact(View view) {
        //cette fonction permet d'envoyer les donner a mettre dans la BDD pour ajouter un contact
        String name = nom.getText().toString();
        String phone = tel.getText().toString();
        final CheckBox checkBox =  findViewById(R.id.fav);

        fav= checkBox.isChecked();

        if(ValidateName()&&ValidatePhone()){//On verifie que les champs nom et telephone ne sont pas vide
            Db.createContact(name, prenom.getText().toString(),adresse.getText().toString(),phone,mail.getText().toString(),fav);
            startActivity(new Intent(this, MainActivity.class));

        }

        }




    public boolean ValidateName() {
        String name = nom.getText().toString();

        boolean validate = true;
        if(name.equals("")){
            String message="Le champ nom est obligatoire";
            validate=false;
            SendMessage(message);
        }
        return validate;
    }//on valide ici le champ nom s'il n'est pas vide

    public boolean ValidatePhone(){
        String phone = tel.getText().toString();

        boolean validate = true;
         if(phone.equals("")){
            String message="Le champ telephone est obligatoire";
            validate=false;
             SendMessage(message);

         }else if(validate==true){
            try{
                Integer.parseInt(phone);
            }catch(NumberFormatException e){
                String message="Il faut que le telephone soit un nombre pour que cela fonctionne";
                SendMessage(message);
                validate=false;
            }
         }
        return validate;
    }//on valide ici le champ telephone s'il n'est pas vide et si c'est un entier

    private void SendMessage(String message) {
        Context context = getApplicationContext();
        int duration =Toast.LENGTH_SHORT;

        Toast toast =Toast.makeText(context, message, duration);
        toast.show();
    }//on envoie le message si les champ sont validés ou non


}
