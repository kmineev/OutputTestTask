package com.example.kostez.outputtesttask.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostez.outputtesttask.ItemClickSupport;
import com.example.kostez.outputtesttask.R;
import com.example.kostez.outputtesttask.adapters.ListRecyclerViewAdapter;
import com.example.kostez.outputtesttask.data.DatabaseHelperFactory;
import com.example.kostez.outputtesttask.data.ItemRecyclerViewTable;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import static com.example.kostez.outputtesttask.adapters.ListRecyclerViewAdapter.LAST_POSITION;
import static com.example.kostez.outputtesttask.adapters.ListRecyclerViewAdapter.MENU_DELETE;
import static com.example.kostez.outputtesttask.adapters.ListRecyclerViewAdapter.MENU_EDIT;
import static com.example.kostez.outputtesttask.applications.MyApplication.getAppContext;

/**
 * Created by Kostez on 11.09.2016.
 */
public class ListFragment extends Fragment {

    public static final String LIST_FRAGMENT_TAG = "list_fragment_tag";
    public static final String ADD_DIALOG_TAG = "add_dialog_tag";
    private static final String EDIT_DIALOG_TAG = "edit_dialog_tag";

    private Dao<ItemRecyclerViewTable, Integer> listRecyclerViewTableDao;
    private QueryBuilder<ItemRecyclerViewTable, Integer> queryBuilder;
    private PreparedQuery<ItemRecyclerViewTable> preparedQuery;
    private List<ItemRecyclerViewTable> itemsList;

    private RecyclerView recyclerView;
    private ListRecyclerViewAdapter listRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                System.out.println("--- onItemClicked");
                showDialog(EDIT_DIALOG_TAG, itemsList.get(position).getItemTitle(), position);
            }
        });

//        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
//                System.out.println("--- onItemLongClicked");
//
//
//
//                return true;
//            }
//        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            listRecyclerViewTableDao = DatabaseHelperFactory.getDatabaseHelper().getListRecyclerViewTableDao();
            queryBuilder = listRecyclerViewTableDao.queryBuilder();
            preparedQuery = queryBuilder.prepare();
            itemsList = listRecyclerViewTableDao.query(preparedQuery);

            initViews();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initViews() throws SQLException {
        linearLayoutManager = new LinearLayoutManager(getAppContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        listRecyclerViewAdapter = new ListRecyclerViewAdapter(getActivity(), itemsList);
        recyclerView.setAdapter(listRecyclerViewAdapter);
    }

    public void showDialog(String tag, String oldTitle, int position) {

        switch (tag) {
            case ADD_DIALOG_TAG:
                AddDialog addDialog = new AddDialog();
                addDialog.setOldTitle(oldTitle);
                addDialog.show(getFragmentManager(), ADD_DIALOG_TAG);
                break;
            case EDIT_DIALOG_TAG:
                EditDialog editDialog = new EditDialog();
                editDialog.setPositionItem(position);
                editDialog.setOldTitle(oldTitle);
                editDialog.show(getFragmentManager(), ADD_DIALOG_TAG);
                break;
        }
    }

    public void addItem(String title) throws SQLException {
        ItemRecyclerViewTable itemRecyclerViewTable = new ItemRecyclerViewTable();
        itemRecyclerViewTable.setItemTitle(title);

        listRecyclerViewTableDao.create(itemRecyclerViewTable);
//        listRecyclerViewAdapter.add(itemRecyclerViewTable, LAST_POSITION);
        itemsList.add(itemRecyclerViewTable);
        listRecyclerViewAdapter.notifiyInserted(LAST_POSITION);
    }

    public void editItem(String title, int position) throws SQLException {
        ItemRecyclerViewTable itemRecyclerViewTable = itemsList.get(position);
        itemRecyclerViewTable.setItemTitle(title);
        listRecyclerViewTableDao.update(itemRecyclerViewTable);
        listRecyclerViewAdapter.notifiyChanged(position);
    }

    public void removeItem(int position) throws SQLException {
        ItemRecyclerViewTable itemRecyclerViewTable = itemsList.get(position);

        listRecyclerViewTableDao.delete(itemRecyclerViewTable);
        itemsList.remove(position);
        listRecyclerViewAdapter.notifiyRemoved(position);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;

        try {
            position = listRecyclerViewAdapter.getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case MENU_EDIT:
                System.out.println("--- MENU_EDIT:Position is " + position);
                showDialog(EDIT_DIALOG_TAG, itemsList.get(position).getItemTitle(), position);
                break;
            case MENU_DELETE:
                // do your stuff
                System.out.println("--- MENU_DELETE:Position is " + position);
                try {
                    removeItem(position);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
}
