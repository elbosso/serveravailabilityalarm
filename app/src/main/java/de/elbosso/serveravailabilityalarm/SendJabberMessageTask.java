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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by elbosso on 9/11/16.
 */
public class SendJabberMessageTask extends AsyncTask<String, Void, String> {
    private Context context;

    SendJabberMessageTask(Context context)
    {
        super();
        this.context=context;
    }
    @Override
    protected String doInBackground(String... params) {

        // params comes from the execute() call: params[0] is the url
        sendJabberMessage(context,params[0],params[1]);
        return params[1];
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, "sent test jabber message to " + result, Toast.LENGTH_SHORT).show();
    }
    public static void sendJabberMessage(final Context context,java.lang.String messageContent,java.lang.String receiver)   {
        try {
        Resources res = context.getResources();
        String packageName = context.getApplicationContext().getPackageName();
            int id = res.getIdentifier("raw/jouliejabber.jks", "raw", packageName);
            id = res.getIdentifier("jouliejabber.jks", "raw", packageName);
        InputStream ins = res.openRawResource(R.raw.jouliejabber);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(ins, "wz8ohis6ft".toCharArray());
        TrustManagerFactory tmf =TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        SSLContext sslctx = SSLContext.getInstance("TLS");
        sslctx.init(null, tmf.getTrustManagers(), new SecureRandom());
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            } };
//            sslctx.init(null, trustAllCerts, new SecureRandom());
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
        String jabber_user = settings.getString("jabber_user", null);
        String jabber_pass = settings.getString("jabber_pass", null);
        String jabber_host = settings.getString("jabber_host", null);
        int jabber_port = settings.getInt("jabber_port", 5222);
        if(verifyJabberConfig(jabber_user,jabber_pass,jabber_host)) {

            configBuilder.setUsernameAndPassword(jabber_user, jabber_pass);
            configBuilder.setResource("jabbernotification");
            configBuilder.setHost(jabber_host);
            configBuilder.setPort(jabber_port);
            configBuilder.setServiceName("serveravailabilityalarm");
            configBuilder.setCustomSSLContext(sslctx);
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            configBuilder.setHostnameVerifier(allHostsValid);
//            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

                AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build());
                SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                // Connect to the server
                connection.connect();
                connection.login();
                String from = connection.getUser();

                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                Chat newChat = chatManager.createChat(receiver);
                Message message = new Message(receiver, Message.Type.chat);

                message.setFrom(from);

                message.setBody(messageContent);

                //message.addExtension(new DefaultExtensionElement("from", from));

                //message.addExtension(new DefaultExtensionElement("to", to));

                newChat.sendMessage(message);
                newChat.close();
                // Disconnect from the server
                connection.disconnect();
        }
        else
        {
            Toast.makeText(context, "please check jabber configuration!", Toast.LENGTH_SHORT).show();
        }
        } catch (final Throwable e) {
            e.printStackTrace();
/*                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
*/                // Get a handler that can be used to post to the main thread
            Handler mainHandler = new Handler(context.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } // This is your code
            };
            mainHandler.post(myRunnable);

            //reportHelper.showExceptionInformation(e.getLocalizedMessage());
        }
    }

    private static boolean verifyJabberConfig(String jabber_user, String jabber_pass, String jabber_host) {
        boolean rv=true;
        if(rv)
            rv=jabber_user!=null;
        if(rv)
            rv=jabber_user.trim().length()>0;
        if(rv)
            rv=jabber_pass!=null;
        if(rv)
            rv=jabber_pass.trim().length()>0;
        if(rv)
            rv=jabber_host!=null;
        if(rv)
            rv=jabber_host.trim().length()>0;
        return rv;
    }
}