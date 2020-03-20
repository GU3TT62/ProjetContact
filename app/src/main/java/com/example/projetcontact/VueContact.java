package com.example.projetcontact;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class VueContact extends Activity {
    private DbContact Db;
    private ListView listView2;

    private Intent SmaIntent = new Intent(Intent.ACTION_SEND);
    private Intent Loca = new Intent(Intent.ACTION_VIEW);

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vuecontact);
        Intent i=getIntent();
        long idContact=i.getLongExtra("ID_CONTACT",-1);

        listView2 =  findViewById(R.id.VueContact);

        Cursor c = Db.fetchContact(idContact);
        startManagingCursor(c);

        String[] from2 = new String[] { DbContact.KEY_NOM,DbContact.KEY_PRENOM,DbContact.KEY_TEL, DbContact.KEY_ADRESS,DbContact.KEY_MAIL,DbContact.Key_FAV};
        int[] to2 = new int[] { R.id.champ1,R.id.champ2,R.id.champ3,R.id.champ4,R.id.champ5,R.id.champ6 };

        SimpleCursorAdapter vuecontact = new SimpleCursorAdapter(this, R.layout.vuecontact, c, from2, to2);
        listView2.setAdapter(vuecontact);

    }

   /* public void loca(){
        Loca.putExtra(SearchManager.QUERY,"");
        Uri location = Uri.parse("geo:0,0?q="+Uri.encode(SelectedTask));
        Loca.setData(location);
        startActivity(Loca);
    }*/




    /*webIntent.putExtra(SearchManager.QUERY,SelectedTask);
    startActivity(webIntent);
                                    mode.finish();
                                    return true;
                                case R.id.map:
    //*/
}
