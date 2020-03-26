package com.example.projetcontact;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VueContact  extends AppCompatActivity {
    private DbContact Db;

    private Intent callIntent;
    private Intent smsIntent;
    private Intent loca;
    private Intent mailIntent;

    private TextView prenomtx;
    private TextView nomtx;
    private TextView adressetx;
    private TextView teltx;
    private TextView mailtx;



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vuecontact);

        Intent i=getIntent();
        loca = new Intent(Intent.ACTION_VIEW);
        callIntent=new Intent(Intent.ACTION_DIAL);
        smsIntent=new Intent(Intent.ACTION_VIEW);
        mailIntent=new Intent(Intent.ACTION_SEND);

        long idContact=i.getLongExtra("ID_CONTACT",-1);
        prenomtx=findViewById(R.id.Prenom);
        nomtx=findViewById(R.id.Nom);
        adressetx=findViewById(R.id.Adresse);
        teltx=findViewById(R.id.Telephone);
        mailtx=findViewById(R.id.Mail);
        Db=new DbContact(this);
        Db.open();

        //listView2 =  findViewById(R.id.VueContact);

        Cursor c = Db.fetchContact(idContact);
       // startManagingCursor(c);
        if (c.moveToFirst()) {
            String id = c.getString( c.getColumnIndex(Db.KEY_ROWID) );
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


    public void loca(View view){
        String SelectedTaskCursor = (String) adressetx.getText().toString();

        loca.putExtra(SearchManager.QUERY,"");
        Uri location = Uri.parse("geo:0,0?q="+Uri.encode(SelectedTaskCursor));
        loca.setData(location);
        startActivity(loca);
    }
    public void Call(View view){
        String SelectedTaskCursor = (String) teltx.getText().toString();
        callIntent.setData(Uri.parse("tel:"+SelectedTaskCursor));
        startActivity(callIntent);
    }
    public void Sms(View view){
        String SelectedTaskCursor = (String) teltx.getText().toString();
        smsIntent.setData(Uri.parse("sms:"+SelectedTaskCursor));
        startActivity(smsIntent);
    }public void Email(View view){
        String SelectedTaskCursor = (String) mailtx.getText().toString();
        mailIntent.setType("message/rfc822");
        mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{SelectedTaskCursor});
        startActivity(mailIntent);
    }




    /*webIntent.putExtra(SearchManager.QUERY,SelectedTask);
    startActivity(webIntent);
                                    mode.finish();
                                    return true;
                                case R.id.map:
    //*/
}
