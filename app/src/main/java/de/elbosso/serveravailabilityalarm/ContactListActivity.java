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

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ContactListActivity extends ListActivity {

    private ContactList contactList;

    /**
     * Called when the activity is first created.
     */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        contactList = Utilities.getContacts(getContentResolver());


        ArrayAdapter<Contact> adapter = new ContactAdapter(this, contactList.getContacts());

        setListAdapter(adapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        String FILENAME = "hello_file";
        String string = "hello world!";
        FileOutputStream fos = null;
        java.io.PrintWriter pw = null;
        try {

            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            pw = new PrintWriter(fos);
            for (int position = 0; position < contactList.getContacts().size(); ++position) {
                if (getListView().isItemChecked(position))
                    pw.println(contactList.getContacts().get(position).getLookupKey());
            }
            pw.close();
            pw = null;
            fos.close();
            fos = null;
        } catch (IOException exp) {

        } finally {
            try {
                if (pw != null)
                    pw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException exp) {

            }
        }
    }

    public void onResume() {
        super.onResume();
        String FILENAME = "hello_file";
        java.io.InputStream is = null;
        java.io.InputStreamReader isr = null;
        java.io.BufferedReader br = null;
        for (Contact c : contactList.getContacts()) {
            c.setSelected(false);
        }
        try {
            is = openFileInput(FILENAME);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            java.lang.String line = br.readLine();
            while (line != null) {
                for (Contact c : contactList.getContacts()) {
                    if (c.getLookupKey() != null)
                        if (c.getLookupKey().equals(line))
                            c.setSelected(true);
                }


                line = br.readLine();
            }
            int position = 0;
            for (Contact c : contactList.getContacts()) {
                if (c.isSelected()) {
                    getListView().setItemChecked(position, true);


                }
                ++position;
            }
        } catch (IOException exp) {
        } finally {
            try {
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
            } catch (IOException exp) {

            }
        }
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
}



