package com.example.projetcontact;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class modif extends AppCompatActivity {
    private DbContact Db;


    EditText nomtx;
    EditText prenomtx;
    EditText teltx;
    EditText adressetx;
    EditText mailtx;

    Boolean fav;
    String phone;
    String name;
    Cursor c;
    Intent i;
    long idContact;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modif);

        nomtx =  findViewById(R.id.Nom);
        prenomtx =  findViewById(R.id.Prenom);
        teltx =  findViewById(R.id.Tel);
        adressetx =  findViewById(R.id.adresse);
        mailtx =  findViewById(R.id.Mail);

        Db=new DbContact(this);
        Db.open();
        final CheckBox checkBox =  findViewById(R.id.fav);//verification de valeur de checbox
        fav= checkBox.isChecked();
        i=getIntent();
        idContact=i.getLongExtra("ID_CONTACT",-1);

        c = Db.fetchContact(idContact);
        if (c.moveToFirst()) {
            String prenom = c.getString(c.getColumnIndex(DbContact.KEY_PRENOM));
            String nom=c.getString(c.getColumnIndex(DbContact.KEY_NOM));
            String adresse=c.getString(c.getColumnIndex(DbContact.KEY_ADRESS));
            String telephone=c.getString(c.getColumnIndex(DbContact.KEY_TEL));
            String mail=c.getString(c.getColumnIndex(DbContact.KEY_MAIL));
            prenomtx.setText(prenom);
            nomtx.setText(nom);
            adressetx.setText(adresse);
            teltx.setText(telephone);
            mailtx.setText(mail);

        }


    }

    public void modifierContact(View view) {
        //cette fonction permet d'envoyer les donner a mettre dans la BDD pour ajouter un contact
        name = nomtx.getText().toString();
        phone = teltx.getText().toString();
        idContact=i.getLongExtra("ID_CONTACT",-1);

        i=getIntent();
        c = Db.fetchContact(idContact);
        int id = (int)c.getLong( c.getColumnIndex(DbContact.KEY_ROWID) );


        if(ValidateName()&&ValidatePhone()){//On verifie que les champs nom et telephone ne sont pas vide
            Db.updateContact(id,name, prenomtx.getText().toString(),adressetx.getText().toString(),phone,mailtx.getText().toString(),fav);
            startActivity(new Intent(this, MainActivity.class));//on repart sur la page principale

        }

    }




    public boolean ValidateName() {
        String name = nomtx.getText().toString();

        boolean validate = true;
        if(name.equals("")){
            String message="Le champ nom est obligatoire";
            validate=false;
            SendMessage(message);
        }
        return validate;
    }//on valide ici le champ nom s'il n'est pas vide

    public boolean ValidatePhone(){
        String phone = teltx.getText().toString();

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
        int duration = Toast.LENGTH_SHORT;

        Toast toast =Toast.makeText(context, message, duration);
        toast.show();
    }//on envoie le message si les champ sont validés ou non
}
