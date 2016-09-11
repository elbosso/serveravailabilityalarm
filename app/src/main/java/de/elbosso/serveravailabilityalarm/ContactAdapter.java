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

import java.util.List;


import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {

	private final List<Contact> _contacts;
	private final Activity _context;
	
	public ContactAdapter(Activity context, List<Contact> contacts)
	{
		super(context, android.R.layout.simple_list_item_multiple_choice,contacts);
		this._contacts=contacts;
        this._context=context;
	}
	static class ViewHolder {
        protected TextView text;
        protected CheckBox cb;
		private Contact  _contact;
		protected void setContact(Contact contact)
		{
			text.setText(contact.getDisplayName()+" "+contact.getPhoneNumber());
            cb.setChecked(true);
			_contact=contact;
		}
		protected Contact getContact() {return _contact;}
	}
	@Override
	public Contact getItem(int position)
	{
		return _contacts.get(position);
	}
/*	@Override


	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view=null;
		if(convertView==null)
		{
			LayoutInflater inflater=_context.getLayoutInflater();
			view=inflater.inflate(R.layout.contactlistitem, null);
			final ViewHolder viewHolder=new ViewHolder();
            viewHolder.text=(TextView)view.findViewById(R.id.txtDisplayName);
            viewHolder.cb=(CheckBox)view.findViewById(R.id.checkBox);
			viewHolder.setContact(_contacts.get(position));
			view.setTag(viewHolder);
		}
		
		return view;
	}
*/}
