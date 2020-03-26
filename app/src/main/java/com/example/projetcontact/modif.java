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
        final CheckBox checkBox =  findViewById(R.id.fav);
        if (checkBox.isChecked()) {
            fav=true;
        }else{
            fav=false;
        }
         i=getIntent();
        idContact=i.getLongExtra("ID_CONTACT",-1);

        c = Db.fetchContact(idContact);
        if (c.moveToFirst()) {
            String prenom = c.getString(c.getColumnIndex(Db.KEY_PRENOM));
            String nom=c.getString(c.getColumnIndex(Db.KEY_NOM));
            String adresse=c.getString(c.getColumnIndex(Db.KEY_ADRESS));
            String telephone=c.getString(c.getColumnIndex(Db.KEY_TEL));
            String mail=c.getString(c.getColumnIndex(Db.KEY_MAIL));
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
        //String id1 = c.getString( c.getColumnIndex(Db.KEY_ROWID) );
        //long id=Long.parseLong(id1);
        if(ValidateName(name)&&ValidatePhone(phone)){//On verifie que les champs nom et telephone ne sont pas vide
            Db.updateContact(idContact,name, prenomtx.getText().toString(),adressetx.getText().toString(),phone,mailtx.getText().toString(),fav);
            startActivity(new Intent(this, MainActivity.class));

        }

    }




    public boolean ValidateName(String name) {
        name = nomtx.getText().toString();

        boolean validate = true;
        if(name.equals("")){
            String message="Le champ nom est obligatoire";
            validate=false;
            SendMessage(message);
        }
        return validate;
    }
    public boolean ValidatePhone(String phone){
        phone = teltx.getText().toString();

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
        int duration = Toast.LENGTH_SHORT;

        Toast toast =Toast.makeText(context, message, duration);
        toast.show();
    }
}
