package com.example.kostez.outputtesttask.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kostez.outputtesttask.R;
import com.example.kostez.outputtesttask.parse.Quote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostez on 15.09.2016.
 */


public class ServiseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROG = 0;

    private List<Quote> quotes;

    public ServiseRecyclerViewAdapter(List<Quote> quotes) {

        if (quotes != null)
            this.quotes = (quotes);
        else
            this.quotes = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_card_view_item, parent, false);
            vh = new ServiceViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ServiceViewHolder) {
            Quote quote = quotes.get(position);
            ((ServiceViewHolder) holder).id.setText(String.valueOf(quote.getId()));
            ((ServiceViewHolder) holder).date.setText(quote.getDate());
            ((ServiceViewHolder) holder).text.setText(quote.getText());

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return quotes.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    public void addItem(Quote quote) {
        this.quotes.add(quote);
    }

    public void remove(int position) {
        this.quotes.remove(position);
    }

    private class ServiceViewHolder extends RecyclerView.ViewHolder {

        public TextView id;
        public TextView date;
        public TextView text;

        public ServiceViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.tv_id);
            date = (TextView) view.findViewById(R.id.tv_date);
            text = (TextView) view.findViewById(R.id.tv_text);
        }
    }

    private class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}