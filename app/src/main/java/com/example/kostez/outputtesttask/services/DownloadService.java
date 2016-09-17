package com.example.kostez.outputtesttask.services;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kostez.outputtesttask.R;
import com.example.kostez.outputtesttask.parse.Quote;
import com.example.kostez.outputtesttask.parse.StorageStaceOXmlParser;
import com.example.kostez.outputtesttask.volley.Config;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.example.kostez.outputtesttask.applications.MyApplication.getAppContext;
import static com.example.kostez.outputtesttask.applications.MyApplication.hasConnection;
import static com.example.kostez.outputtesttask.fragments.ServiceFragment.DOWNLOAD_PENDING_PARAM;

/**
 * Created by Kostez on 14.09.2016.
 */
public class DownloadService extends Service {

    private PendingIntent pendingIntent;
    private List<Quote> quotes;
    protected Binder binder;

    public class LocalBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new LocalBinder();
        download();
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pendingIntent = intent.getParcelableExtra(DOWNLOAD_PENDING_PARAM);
        return super.onStartCommand(intent, flags, startId);
    }

    private void download() {
        this.getData();
    }

    private void getData() {

        if (hasConnection(getAppContext())) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET,  Config.DATA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                parseData(response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // handle error response

                        }
                    }
            );
            Volley.newRequestQueue(getAppContext()).add(stringRequest);
        } else {
            Toast.makeText(getAppContext(), getAppContext().getString(R.string.please_connect_to_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void parseData(String response) throws IOException, XmlPullParserException {

//        InputStream stream = new ByteArrayInputStream(response.getBytes(UTF_8));

        byte ptext[] = response.getBytes("ISO-8859-1");
        String value = new String(ptext, "UTF-8");

        InputStream stream = new ByteArrayInputStream(value.getBytes());
        StorageStaceOXmlParser storageStaceOXmlParser = new StorageStaceOXmlParser();
        storageStaceOXmlParser.parse(stream);
//        quotes.addAll(storageStaceOXmlParser.getQuotes());

        quotes = storageStaceOXmlParser.getQuotes();

        if (!quotes.isEmpty()) {
//            Intent intent = new Intent().putExtra(DOWNLOAD_RESULT, true);
            try {
                System.out.println("--- sendingPendingIntent");
//
//                Intent intentASD = new Intent().putExtra(PARAM_RESULT, true);
//                pendingIntent.send(DownloadService.this, Activity.RESULT_OK, intentASD);
                pendingIntent.send(Activity.RESULT_OK);



            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
