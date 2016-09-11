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
*/package de.elbosso.serveravailabilityalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddKnowledgeDBItem extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_knowledge_dbitem);
        Intent myStarter = getIntent();
        if(myStarter!=null)
        {
            if(myStarter.getStringExtra("key")!=null) {
                ((EditText) findViewById(R.id.editText4)).setText(myStarter.getStringExtra("key"));
                ((EditText) findViewById(R.id.editText4)).setEnabled(false);
                ((TextView) findViewById(R.id.add_list_item_ok)).setText(R.string.add_list_item_mod);
            }
            else
            {
                ((EditText) findViewById(R.id.editText4)).setEnabled(true);
                ((TextView) findViewById(R.id.add_list_item_ok)).setText(R.string.add_list_item_ok);
            }
            if(myStarter.getStringExtra("value")!=null)
                ((EditText)findViewById(R.id.editText5)).setText(myStarter.getStringExtra("value"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_add_knowledge_dbitem, menu);
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    public void buttonOnClick(View v) {
        Button button=(Button) v;
        if(button.getId()==R.id.add_list_item_cancel)
        {
            //Intent i = new Intent(getApplicationContext(), KnowledgeDBListActivity.class);
            //startActivity(i);
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
        else if(button.getId()==R.id.add_list_item_ok)
        {
            String key=((EditText)findViewById(R.id.editText4)).getText().toString();
            String value=((EditText)findViewById(R.id.editText5)).getText().toString();
            if((key.trim().length()<1)||(value.trim().length()<1))
                Toast.makeText(this, "Both key and value must be set!", Toast.LENGTH_SHORT).show();
            else
            {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("key", key);
                returnIntent.putExtra("value", value);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

}
