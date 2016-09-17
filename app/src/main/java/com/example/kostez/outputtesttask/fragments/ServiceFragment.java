package com.example.kostez.outputtesttask.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.example.kostez.outputtesttask.R;
import com.example.kostez.outputtesttask.adapters.ServiseRecyclerViewAdapter;
import com.example.kostez.outputtesttask.parse.Quote;
import com.example.kostez.outputtesttask.services.DownloadService;
import com.example.kostez.outputtesttask.volley.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.kostez.outputtesttask.applications.MyApplication.getAppContext;

/**
 * Created by Kostez on 13.09.2016.
 */
public class ServiceFragment extends Fragment {

    public static final String SERVICE_FRAGMENT_TAG = "scaling_fragment_tag";
    public final static String DOWNLOAD_PENDING_PARAM = "download_pending_param";
    public static final int DOWNLOAD_CODE = 178;
    public static final int SERVICE_FRAGMENT_ID = 103;


    private OnLoadMoreListener onLoadMoreListener;
    private List<String> siteData = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private ServiceConnection downloadServiceConnection;
    private DownloadService downloadService;
    private Intent intent;
    private List<Quote> quotes = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ServiseRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getAppContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ServiseRecyclerViewAdapter(quotes);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intent = new Intent(getAppContext(), DownloadService.class);

        downloadServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                downloadService = ((DownloadService.LocalBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        initViews();
    }

    private void initViews() {

        onLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                System.out.println("");
                adapter.addItem(null);
                adapter.notifyItemInserted(quotes.size() - 1);

                PendingIntent pi = getActivity().createPendingResult(DOWNLOAD_CODE, intent, 0);
                Intent pendIntent = new Intent(getActivity(), DownloadService.class).putExtra(DOWNLOAD_PENDING_PARAM, pi);
                getActivity().startService(pendIntent);
            }
        };

        if (quotes.isEmpty()) {
            onLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().stopService(new Intent(getActivity(), DownloadService.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(intent, downloadServiceConnection, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println();
        if (resultCode == Activity.RESULT_OK) {

            adapter.remove(quotes.size() - 1);
            adapter.notifyItemRemoved(quotes.size());

            for (int i = 0; i < downloadService.getQuotes().size(); i++) {
                Quote quote = downloadService.getQuotes().get(i);
                adapter.addItem(quote);
                adapter.notifyItemInserted(quotes.size() - 1);
            }
        }
    }
}