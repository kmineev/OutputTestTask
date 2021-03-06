package com.example.kostez.outputtesttask.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.kostez.outputtesttask.R;

import java.sql.SQLException;

import static com.example.kostez.outputtesttask.applications.MyApplication.getAppContext;
import static com.example.kostez.outputtesttask.fragments.ListFragment.LIST_FRAGMENT_TAG;

/**
 * Created by Kostez on 12.09.2016.
 */
public class AddDialog extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "myLogs";
    private EditText input;
    private AlertDialog.Builder alertDialog;
    ListFragment listFragment;

    private String oldTitle = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        input = new EditText(getAppContext());
        input.setTextColor(ContextCompat.getColor(getAppContext(), R.color.Black));
        input.setText(oldTitle);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_new_item)
                .setMessage(R.string.input_items_title)
                .setView(input)
                .setNegativeButton(R.string.revert, this)
                .setPositiveButton(R.string.done, this);

        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch (i) {
            case Dialog.BUTTON_POSITIVE:
                try {
                    ((ListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG)).addItem(input.getText().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
        }

    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        listFragment = ((ListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG));

        if (!input.getText().toString().equals(oldTitle)) {

            alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle(R.string.new_title_was_not_saved)
                    .setMessage(R.string.save_title)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            try {
                                listFragment.addItem(input.getText().toString());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }
    }

    public String getOldTitle() {
        return oldTitle;
    }

    public void setOldTitle(String oldTitle) {
        this.oldTitle = oldTitle;
    }
}
