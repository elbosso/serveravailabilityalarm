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

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class KnowledgeDB extends java.lang.Object implements Comparator<Map.Entry<String,String> > {
	private Map<String,String> directory=new java.util.HashMap();
//	private List<Contact> entries=new ArrayList<Contact>();
	public List<Map.Entry<String,String> > getEntries(){
		java.util.List rv=new java.util.LinkedList<Map.Entry<String,String> >(directory.entrySet());
		Collections.sort(rv,this);
		return rv;
	}
	KnowledgeDB()
	{
		super();
		directory.put("c", "c");
		directory.put("a","a");
		directory.put("b", "b");
	}
	public void delete(java.lang.String key)
	{
		directory.remove(key);
	}
	public void add(java.lang.String key,String value)
	{
		directory.put(key, value);
	}
	public void modify(java.lang.String selectedKey,java.lang.String key,String value)
	{
		directory.remove(selectedKey);
		directory.put(key,value);
	}
	public void save (ObjectOutputStream oStream) throws IOException {
		oStream.writeObject(directory);
	}
	public void load (ObjectInputStream iStream) throws IOException, ClassNotFoundException {
		directory = (Map<String, String>) iStream.readObject();
	}
	public Map<String,String> load(Context context) {
		String FILENAME = "knowledgedb";
		java.io.InputStream is = null;
		ObjectInputStream iStream = null;
		try {
			is = context.openFileInput(FILENAME);
			iStream = new ObjectInputStream(is);
			load(iStream);
			iStream.close();
			iStream = null;
			is.close();
			is = null;
		} catch (Exception exp) {
		} finally {
			try {
				if (iStream != null)
					iStream.close();
				if (is != null)
					is.close();
			} catch (IOException exp) {

			}
		}
		return directory;
	}
	@Override
	public int compare(Map.Entry<String, String> stringStringEntry, Map.Entry<String, String> t1) {
		return stringStringEntry.getKey().compareTo(t1.getKey());
	}
	public String get(String key)
	{
		java.lang.String rv=null;
		if((directory!=null)&&(directory.containsKey(key)))
			rv=directory.get(key);
		return rv;
	}
	
/*	public void addContact(Entry entry){

		if(entries.contains(entry)==false)
		{
			directory.put(contact.getDisplayName(),contact);
			this._contacts.add(contact);
		}
		else
		{
			Contact alreadyThere=directory.get(contact.getDisplayName());
			if(alreadyThere.getJabber()==null)
				alreadyThere.setJabber(contact.getJabber());
			if(alreadyThere.getPhoneNumber()==null)
				alreadyThere.setPhoneNumber(contact.getPhoneNumber());
			if(alreadyThere.getLookupKey()==null)
				alreadyThere.setPhoneNumber(contact.getLookupKey());
		}
	}
*/
}
