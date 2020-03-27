package com.example.projetcontact;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.WriterException;

public class VueContact  extends AppCompatActivity {
    private DbContact Db;

    private Intent callIntent;
    private Intent smsIntent;
    private Intent loca;
    private Intent mailIntent;
    private Bitmap bitmap ;

    private TextView prenomtx;
    private TextView nomtx;
    private TextView adressetx;
    private TextView teltx;
    private TextView mailtx;
    private Cursor c;
    private ImageView imageView;



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

        imageView = findViewById(R.id.imageView2);


        Db=new DbContact(this);
        Db.open();


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
    public void loca(View view){
        String SelectedTaskCursor = (String) adressetx.getText().toString();

        loca.putExtra(SearchManager.QUERY,"");
        Uri location = Uri.parse("geo:0,0?q="+Uri.encode(SelectedTaskCursor));
        loca.setData(location);
        startActivity(loca);
    }//Activite permettant de localiser le contact

    public void Call(View view){
        String SelectedTaskCursor = (String) teltx.getText().toString();
        callIntent.setData(Uri.parse("tel:"+SelectedTaskCursor));
        startActivity(callIntent);
    }//Activite permettant d'appeler le contact

    public void Sms(View view){
        String SelectedTaskCursor = (String) teltx.getText().toString();
        smsIntent.setData(Uri.parse("sms:"+SelectedTaskCursor));
        startActivity(smsIntent);
    }//Activité permettant d'envoyer un sms au contact

    public void Email(View view){
        String SelectedTaskCursor = (String) mailtx.getText().toString();
        mailIntent.setType("message/rfc822");
        mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{SelectedTaskCursor});
        startActivity(mailIntent);
    }//Activité permettant d'envoyer un mail au contact

    public void modifier(View view){
        Intent i=getIntent();
        long idContact=i.getLongExtra("ID_CONTACT",-1);

        Intent intent=new Intent(VueContact.this,modif.class);
        intent.putExtra("ID_CONTACT",idContact);
        startActivity(intent);


    }
    //activité permettant d'envoyer sur la page de modification d'un contact

    /*public void qrCode(View view){
        try {
            bitmap = TextToImageEncode(nomtx.getText().toString());

            imageView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }*/


}
