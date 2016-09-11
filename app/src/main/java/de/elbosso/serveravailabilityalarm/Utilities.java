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

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by elbosso on 8/13/14.
 */
public class Utilities
{
    public static KnowledgeDB getKnowledgeDB()
    {
        return new KnowledgeDB();
    }

    public static ContactList getContacts(ContentResolver cr)

    {

        ContactList contactList=new ContactList();

        Uri uri= ContactsContract.Data.CONTENT_URI;//Phone.CONTENT_URI;


        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cur=cr.query(uri, null, null, null, sortOrder);

        if(cur.getCount()>0)

        {

            String id;

            String name;

            while (cur.moveToNext())

            {

                String mimetype=cur.getString(cur.getColumnIndex(ContactsContract.Data.MIMETYPE));
                if(mimetype!=null)
                {
                    if(mimetype.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                        String data1 = cur.getString(cur.getColumnIndex(ContactsContract.Data.DATA1));
                        String phNumber = cur.getString(cur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phNumber != null) {
//                    if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(phNumber);
                            long rid = cur.getLong(cur.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
                            int PHONE_TYPE = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                            switch (PHONE_TYPE) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
// home number
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
// mobile number
                                {
                                    Contact c = new Contact();
                                    id = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                                    name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                    c.setId(id);
                                    c.setDisplayName(name);
                                    c.setLookupKey(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY)));
                                    c.setPhoneNumber(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                                    contactList.addContact(c);
                                    break;
                                }
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
// work(office) number
                                    break;
                            }
                        }
                    }
                    else if(mimetype.equals(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE))
                    {
                        int protocol=cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
                        if(protocol==ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER)
                        {
                            Contact c = new Contact();
                            name = cur.getString(cur.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                            c.setDisplayName(name);
                            c.setJabber(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA1)));
                            c.setLookupKey(cur.getString(cur.getColumnIndex(ContactsContract.Data.LOOKUP_KEY)));
                            contactList.addContact(c);

                        }
                    }
                }
            }
        }
        cur.close();

        return contactList;

    }

}
