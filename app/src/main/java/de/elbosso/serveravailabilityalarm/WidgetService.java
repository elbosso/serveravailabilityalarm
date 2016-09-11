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

/**
 * Created by elbosso on 7/16/14.
 */
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by elbosso on 4/19/14.
 */
public class WidgetService extends Service
{
     public static final String PREFS_NAME = "serveravailabilityalarm.MyPrefsFile";
    public static final java.lang.String REFRESH_DATA_INTENT = "serveravailabilityalarm.refresh.action.acknowledge";
    private boolean alive;
    private String msg="";

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        buildUpdate();

        return super.onStartCommand(intent, flags, startId);
    }

    private void buildUpdate()
    {
        if(NetworkUtil.getConnectivityStatus(getApplicationContext())== NetworkUtil.TYPE_WIFI)
            startPingThread();
    }

    private void startPingThread() {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        String urlString = settings.getString("url",null);
        if((urlString!=null)&&(urlString.trim().length()>0))
            new PingTask().execute(new java.lang.String[]{urlString.trim()});
    }
//http://tichy.fritz.box/server_status.txt
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    private class PingTask extends AsyncTask<String, Void, PingResult> {
        @Override
        protected PingResult doInBackground(String... hosts) {
            boolean response = false;
            java.lang.String found=null;
            for (String host : hosts) {
/*                InetAddress in;
                in = null;
                try {
                    in = InetAddress.getByName(host.toString());
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Definimos un tiempo en el cual ha de responder
                try {
                    response=in.isReachable(15000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
//                info.setText(e.toString());
                }
                if((response==false)&&(in!=null))
                {
                    java.net.Socket sock=null;
*/                    java.lang.String line=null;
                    java.io.InputStream is=null;
                    java.io.InputStreamReader isr=null;
                    java.io.BufferedReader br=null;
                    try {
/*                        java.net.SocketAddress socketAddress=new InetSocketAddress(in,22);
                        sock=new java.net.Socket();
                        int timeout=2000;//milliseconds
                        sock.connect(socketAddress,timeout);
*/                        java.net.URL url=new URL(host);
                        is=url.openStream();
                        isr=new InputStreamReader(is);
                        br=new BufferedReader(isr);
                        line=br.readLine();
                        if(line!=null)
                        {
                            java.text.DateFormat df=new SimpleDateFormat("yyyy-MM-dd_HH:mm");
                            java.util.Date probed=df.parse(line);
                            java.util.Date now=new java.util.Date();
                            long p=probed.getTime();
                            long n=now.getTime();
                            long diff=n-p;
                            SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                            int minutes_age=settings.getInt("age",3);
                            if(diff>minutes_age*1000*60)
                                found="too old: server said "+line+"("+probed+"/"+p+"); now its "+df.format(now)+"("+now+"/"+n+")";
                            else
                                response=true;
                        }
                        else
                        found="line was null";
                        br.close();
                        br=null;
                        isr.close();
                        isr=null;
                        is.close();
                        is=null;
                    }
                    catch(java.text.ParseException exp)
                    {
                        found=line;
                    }
                    catch(java.lang.Throwable t)
                    {
                        found=t.getClass().getSimpleName();
                    }
                    finally {
                        try
                        {
                            if(br!=null)
                                br.close();
                            if(isr!=null)
                                isr.close();
                            if(is!=null)
                                is.close();
                        }
                        catch(IOException exp)
                        {

                        }
                    }
/*                    if(sock!=null)
                    {
                        try {
                            sock.close();
                        }
                        catch(java.lang.Throwable t)
                        {

                        }
                    }
                }
*/            }
            return new PingResult(response,found);
        }

        @Override
        protected void onPostExecute(PingResult result) {
            alive=result.isAlive();
            msg=result.getMsg();
            {
                String lastUpdated = DateFormat.format("MMMM dd, yyyy HH:mm:ss", new Date()).toString();

                RemoteViews view = new RemoteViews(getPackageName(), R.layout.appwidget);
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
                java.lang.String s="";
                int perc=0;
                if(batteryStatus!=null)
                {
                    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                    boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL;
//                if(isCharging==false)
                    {
                        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                        float batteryPct = level / (float)scale*100f;
                        perc=(int)batteryPct;
                        s="("+perc+"%)";
                    }
                    if(isCharging==false)
                    {
                        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                        int soll=settings.getInt("perc",25);
                        if(perc<soll)
                        {
                            msg="Low on Battery (below "+soll+"%) - act accordingly!";
                            alive=false;
                        }
                    }
//                    getApplicationContext().unregisterReceiver(batteryStatus);
                }

                view.setTextViewText(R.id.textView, lastUpdated+" "+s);
                view.setTextViewText(R.id.textView2, (NetworkUtil.getConnectivityStatus(getApplicationContext())== NetworkUtil.TYPE_WIFI)?(alive?"its alive!":"its dead!"):"its undecided!");
                view.setTextViewText(R.id.textView3, (NetworkUtil.getConnectivityStatus(getApplicationContext())== NetworkUtil.TYPE_WIFI)?(alive?"ok":msg):"no connection!");

                if((msg!=null)||(alive==false))
                {
                    doSignal();
                }
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean signalled=settings.getBoolean("signalled",false);
                if(signalled)
                    view.setTextViewText(R.id.textView4, "Bitte quittieren!");
                else
                    view.setTextViewText(R.id.textView4, "");
                if(alive)
                    if(signalled)
                        view.setImageViewResource(R.id.imageView, R.drawable.warning_yellow);
                    else
                        view.setImageViewResource(R.id.imageView, R.drawable.button_hkchen);
                else
                    view.setImageViewResource(R.id.imageView,R.drawable.button_x);
                // Push update for this widget to the home screen
                ComponentName thisWidget = new ComponentName(WidgetService.this, WidgetProvider.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(WidgetService.this);
                manager.updateAppWidget(thisWidget, view);
            }
        }
    }
    private void doSignal()
    {
        ContactList contactList=Utilities.getContacts(getContentResolver());
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean signalled=settings.getBoolean("signalled", false);
        boolean reminded=settings.getBoolean("reminded",false);
        boolean dosendsms=false;
        Calendar cal=Calendar.getInstance();
        if(signalled==false) {
            dosendsms=true;
        }
        else
        {
            if(reminded==false)
            {
                int smsremindh=settings.getInt("smsremindh",7);
                int smsremindm=settings.getInt("smsremindm",30);
                reminded=(cal.get(cal.HOUR_OF_DAY)>smsremindh)||((cal.get(cal.HOUR_OF_DAY)==smsremindh)&&(cal.get(cal.MINUTE)>=smsremindm));
                dosendsms=reminded;
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("reminded", reminded);
                // Commit the edits!
                editor.commit();
            }
        }
        String FILENAME = "hello_file";
        java.io.InputStream is=null;
        java.io.InputStreamReader isr=null;
        java.io.BufferedReader br=null;
        for(Contact c:contactList.getContacts())
        {
            c.setSelected(false);
        }
        try
        {
            is=openFileInput(FILENAME);
            isr=new InputStreamReader(is);
            br=new BufferedReader(isr);
            java.lang.String line=br.readLine();
            while(line!=null)
            {
                for(Contact c:contactList.getContacts())
                {
                    if(c.getLookupKey().equals(line))
                        c.setSelected(true);
                }


                line=br.readLine();
            }
            int position=0;
        }
        catch(IOException exp)
        {
        }
        finally {
            try
            {
                if(br!=null)
                    br.close();
                if(isr!=null)
                    isr.close();
                if(is!=null)
                    is.close();
            }
            catch(IOException exp)
            {

            }
        }
        int count=0;
        int smsbeforeh=settings.getInt("smsbeforeh",12);
        int smsbeforem=settings.getInt("smsbeforem",1);
        int smsafterh=settings.getInt("smsafterh",12);
        int smsafterm=settings.getInt("smsafterm",00);
        int index = msg.indexOf(" ");
        if (index > -1) {
            KnowledgeDB knowledgeDB = Utilities.getKnowledgeDB();
            knowledgeDB.load(this.getApplicationContext());
            String key = msg.substring(0, index);
            Toast.makeText(WidgetService.this, "searching in kdb for" + key, Toast.LENGTH_SHORT).show();
            String url = knowledgeDB.get(key);
            if (url != null)
                msg = msg + " " + url;
        }
        for(Contact c:contactList.getContacts())
        {
            if(c.isSelected())
            {
                boolean didt=false;
                if((c.getPhoneNumber()!=null)&&(dosendsms==true)) {
                    boolean send = false;
                    send = (cal.get(cal.HOUR_OF_DAY) < smsbeforeh) || ((cal.get(cal.HOUR_OF_DAY) == smsbeforeh) && (cal.get(cal.MINUTE) <= smsbeforem));
                    if (send == false)
                        send = (cal.get(cal.HOUR_OF_DAY) > smsafterh) || ((cal.get(cal.HOUR_OF_DAY) == smsafterh) && (cal.get(cal.MINUTE) >= smsafterm));
                    if (send) {
                        Toast.makeText(WidgetService.this, "sending sms to " + c.getDisplayName(), Toast.LENGTH_SHORT).show();
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(c.getPhoneNumber(), null, msg, null, null);
                        didt=true;
                    }
                }
                if((c.getJabber()!=null)&&(signalled==false))
                {
                    new SendJabberMessageTask(this).execute(msg,c.getJabber());
                    didt=true;
                }
                if(didt)
                    ++count;
            }
            if(count>0)
            {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("signalled", true);

        // Commit the edits!
        editor.commit();
                sendBroadcast(new Intent(WidgetService.REFRESH_DATA_INTENT));
//                GlobalDataContainer.getSharedInstance().setSignalled(true);
            }
        }
    }
}