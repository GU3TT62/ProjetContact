package com.example.projetcontact;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class VueContact  extends AppCompatActivity {
    private Intent callIntent;
    private Intent smsIntent;
    private Intent loca;
    private Intent mailIntent;

    private String prenom;
    private String nom;
    private String telephone;
    private String adresse;
    private String mail;

    private TextView adressetx;
    private TextView teltx;
    private TextView mailtx;
    private ImageView imageView;

    public final static int QRcodeWidth = 300 ;//taille du qr code


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vuecontact);

        Intent i=getIntent();
        loca = new Intent(Intent.ACTION_VIEW);
        callIntent=new Intent(Intent.ACTION_DIAL);
        smsIntent=new Intent(Intent.ACTION_VIEW);
        mailIntent=new Intent(Intent.ACTION_SEND);

        long idContact=i.getLongExtra("ID_CONTACT",-1);

        TextView prenomtx = findViewById(R.id.Prenom);
        TextView nomtx = findViewById(R.id.Nom);
        adressetx=findViewById(R.id.Adresse);
        teltx=findViewById(R.id.Telephone);
        mailtx=findViewById(R.id.Mail);

        imageView = findViewById(R.id.imageView2);


        DbContact db = new DbContact(this);
        db.open();


        Cursor c = db.fetchContact(idContact);
        if (c.moveToFirst()) {


            prenom = c.getString(c.getColumnIndex(DbContact.KEY_PRENOM));
            nom= c.getString(c.getColumnIndex(DbContact.KEY_NOM));
            adresse= c.getString(c.getColumnIndex(DbContact.KEY_ADRESS));
            telephone= c.getString(c.getColumnIndex(DbContact.KEY_TEL));
            mail= c.getString(c.getColumnIndex(DbContact.KEY_MAIL));


            prenomtx.setText(prenom);
            nomtx.setText(nom);
            adressetx.setText(adresse);
            teltx.setText(telephone);
            mailtx.setText(mail);

        }
    }
    public void loca(View view){
        String SelectedTaskCursor =  adressetx.getText().toString();

        loca.putExtra(SearchManager.QUERY,"");
        Uri location = Uri.parse("geo:0,0?q="+Uri.encode(SelectedTaskCursor));
        loca.setData(location);
        startActivity(loca);
    }//Activite permettant de localiser le contact

    public void Call(View view){
        String SelectedTaskCursor =  teltx.getText().toString();
        callIntent.setData(Uri.parse("tel:"+SelectedTaskCursor));
        startActivity(callIntent);
    }//Activite permettant d'appeler le contact

    public void Sms(View view){
        String SelectedTaskCursor =  teltx.getText().toString();
        smsIntent.setData(Uri.parse("sms:"+SelectedTaskCursor));
        startActivity(smsIntent);
    }//Activité permettant d'envoyer un sms au contact

    public void Email(View view){
        String SelectedTaskCursor =  mailtx.getText().toString();
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

    public void qrCode(View view){
        try {
            String infos=nom+ System.getProperty ("line.separator")//pour ajouter un saut de ligne
                    +prenom+ System.getProperty ("line.separator")
                    +telephone+ System.getProperty ("line.separator")
                    +adresse+System.getProperty ("line.separator")
                    +mail+System.getProperty ("line.separator");
            Bitmap bitmap = TextToImageEncode(infos);//on met les informations à stocker dans bitmap

            imageView.setImageBitmap(bitmap);//on met notre bitmap dans l'image view

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {//on code notre Bitmap
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
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
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);//definitiondes couleur du code
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);//onc cré leqrcode

        bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);//on donne le nombre de pixels du qr code
        return bitmap;
    }


}
