package com.example.projetcontact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class scannQR extends AppCompatActivity {

    private Button buttonScan;
    private TextView textViewName, textViewAddress,textViewPrenom,textViewTel,textViewMail;
    private IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scann_qr);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewPrenom = (TextView) findViewById(R.id.textViewPrenom);
        textViewMail = (TextView) findViewById(R.id.textViewMail);
        textViewTel = (TextView) findViewById(R.id.textViewTell);

        qrScan = new IntentIntegrator(this);

    }

    @Override
    //lecture du qrCode
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Resultat non trouvé", Toast.LENGTH_LONG).show();
            } else {
                String[] obj = result.getContents().split(System.getProperty ("line.separator"));//divise la chaine de caractere au endroits ou il y a un
                textViewName.setText(obj[0]);
                textViewPrenom.setText(obj[1]);
                textViewMail.setText(obj[2]);
                textViewAddress.setText(obj[3]);
                textViewTel.setText(obj[4]);



            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClick(View view) {
        qrScan.initiateScan();//lancement de la lecture du code
    }
    public void ajoutBDD(View view) {
        DbContact db = new DbContact(this);
        db.open();
        db.createContact(textViewName.getText().toString(),textViewPrenom.getText().toString(),textViewAddress.getText().toString(),textViewTel.getText().toString(),textViewMail.getText().toString()
        ,false);
        Intent intent=new Intent(scannQR.this,MainActivity.class);
        startActivity(intent);

    }
}