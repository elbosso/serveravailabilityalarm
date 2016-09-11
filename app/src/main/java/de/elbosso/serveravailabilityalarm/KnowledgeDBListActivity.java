/*
Copyright (c) 2012-2016.

Juergen Key. Alle Rechte vorbehalten.

Weiterverbreitung und Verwendung in nichtkompilierter oder kompilierter Form,
mit oder ohne Veraenderung, sind unter den folgenden Bedingungen zulaessig:

   1. Weiterverbreitete nichtkompilierte Exemplare muessen das obige Copyright,
die Liste der Bedingungen und den folgenden Haftungsausschluss im Quelltext
enthalten.
   2. Weiterverbreitete kompilierte Exemplare muessen das obige Copyright,
die Liste der Bedingungen und den folgenden Haftungsausschluss in der
Dokumentation und/oder anderen Materialien, die mit dem Exemplar verbreitet
werden, enthalten.
   3. Weder der Name des Autors noch die Namen der Beitragsleistenden
duerfen zum Kennzeichnen oder Bewerben von Produkten, die von dieser Software
abgeleitet wurden, ohne spezielle vorherige schriftliche Genehmigung verwendet
werden.

DIESE SOFTWARE WIRD VOM AUTOR UND DEN BEITRAGSLEISTENDEN OHNE
JEGLICHE SPEZIELLE ODER IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE
UNTER ANDEREM EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER
SOFTWARE FUER EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL IST DER AUTOR
ODER DIE BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN,
ZUFAELLIGEN, SPEZIELLEN, BEISPIELHAFTEN ODER FOLGENDEN SCHAEDEN (UNTER ANDEREM
VERSCHAFFEN VON ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER
NUTZUNGSFAEHIGKEIT; VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER
GESCHAEFTSUNTERBRECHUNG), WIE AUCH IMMER VERURSACHT UND UNTER WELCHER
VERPFLICHTUNG AUCH IMMER, OB IN VERTRAG, STRIKTER VERPFLICHTUNG ODER
UNERLAUBTE HANDLUNG (INKLUSIVE FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM
WEG SIE AUCH IMMER DURCH DIE BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR,
WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.
*/
package de.elbosso.serveravailabilityalarm;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class KnowledgeDBListActivity extends ListActivity implements ActionMode.Callback{

    private KnowledgeDB knowledgeDB;
    protected ActionMode mActionMode;
    public int selectedItem = -1;

    /**
     * Called when the activity is first created.
     */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.footerview, null);
        Button list_view_add_btn = (Button) footerLayout.findViewById(R.id.list_view_add_btn);

        getListView().addFooterView(footerLayout);
        knowledgeDB = Utilities.getKnowledgeDB();


        ArrayAdapter<Map.Entry<String,String> > adapter = new KnowledgeAdapter(this, knowledgeDB.getEntries());

        setListAdapter(adapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (mActionMode != null) {
                    return false;
                }
                selectedItem = position;

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = KnowledgeDBListActivity.this.startActionMode(KnowledgeDBListActivity.this);
                view.setSelected(true);
                return true;
            }
        });///        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list_view_add_btn.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), AddKnowledgeDBItem.class);
//                intent.set
                startActivityForResult(intent,1);
            }
        });
    }

     public void save(){
        String FILENAME = "knowledgedb";
        FileOutputStream fos = null;
        ObjectOutputStream oStream=null;
        try {

            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            oStream = new ObjectOutputStream(fos);
            knowledgeDB.save(oStream);
            oStream.flush();
            oStream.close();
            oStream=null;
            fos.close();
            fos = null;
        } catch (IOException exp) {

        } finally {
            try {
                if (oStream != null)
                    oStream.close();
                if (fos != null)
                    fos.close();
            } catch (IOException exp) {

            }
        }
    }

    public void onStart() {
        super.onStart();
        knowledgeDB.load(this.getApplicationContext());
        ArrayAdapter<Map.Entry<String, String>> adapter = new KnowledgeAdapter(this, knowledgeDB.getEntries());

        setListAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)

    {

        super.onListItemClick(l, v, position, id);

/*                  Object o=this.getListAdapter().getItem(position);

                  Contact c=(Contact)o;

                  Toast.makeText(this, c.getDisplayName()+" "+c.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext());
              builder.setContentText(c.getDisplayName()+" "+c.getPhoneNumber());
              builder.setContentTitle("ServerAvailability");
              builder.setSmallIcon(R.drawable.ic_launcher);
              Notification noti=builder.build();
              ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).notify(1,noti);

*/
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = actionMode.getMenuInflater();
        // Assumes that you have "contexual.xml" menu resources
        inflater.inflate(R.menu.knowledgedb_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }
    private void show(String mode)
    {
        Toast.makeText(KnowledgeDBListActivity.this,
                "Item in position " + selectedItem + " "+mode,
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        Map.Entry<String,String> selectedEntry=(Map.Entry<String,String>)getListAdapter().getItem(selectedItem);
        switch (menuItem.getItemId()) {
            case R.id.action_kdb_delete:
                show("delete "+selectedEntry.getKey());
                knowledgeDB.delete(selectedEntry.getKey());
                ArrayAdapter<Map.Entry<String,String> > adapter = new KnowledgeAdapter(this, knowledgeDB.getEntries());

                setListAdapter(adapter);
                // Action picked, so close the CAB
                actionMode.finish();
                save();
                return true;
            case R.id.action_kdb_edit:
                show("edit " + selectedEntry.getKey());
                Intent intent=new Intent(getApplicationContext(), AddKnowledgeDBItem.class);
                intent.putExtra("key",selectedEntry.getKey());
                intent.putExtra("value", selectedEntry.getValue());
//                intent.set
                startActivityForResult(intent, 2);
                // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        selectedItem=-1;
        mActionMode=null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String key=data.getStringExtra("key");
                String value=data.getStringExtra("value");
                knowledgeDB.add(key,value);
                ArrayAdapter<Map.Entry<String,String> > adapter = new KnowledgeAdapter(this, knowledgeDB.getEntries());

                setListAdapter(adapter);
                save();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Map.Entry<String,String> selectedEntry=(Map.Entry<String,String>)getListAdapter().getItem(selectedItem);
                String selectedKey=selectedEntry.getKey();
                String key=data.getStringExtra("key");
                String value=data.getStringExtra("value");
                knowledgeDB.modify(selectedKey, key, value);
                ArrayAdapter<Map.Entry<String,String> > adapter = new KnowledgeAdapter(this, knowledgeDB.getEntries());

                setListAdapter(adapter);
                save();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
            mActionMode.finish();
        }
    }//onActivityResult
}



