package com.mycompany.bloodlinkhospital;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Edwin on 15/02/2015.
 */
public class CompletedFragment extends Fragment {

    protected ListView listView;
    private ArrayList<Incident> historyList = new ArrayList<Incident>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Inflate (render) the view
        View view = inflater.inflate(R.layout.completed_requests_fragment, parent, false);
        ListView listView = (ListView) view.findViewById(R.id.historyListView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Create a ListView java object private to the CompletedFragment object
        ListView historyListView = (ListView) getActivity().findViewById(R.id.historyListView);
        // Create CompletedListViewAdapter
        Resources res = getResources();
        CompletedListViewAdapter completedListViewAdapter = new CompletedListViewAdapter(this, historyList, res);
        // Set adapter to view
        historyListView.setAdapter(completedListViewAdapter);

    }

    /*****************
     * This function used by adapter
     ****************/
    public void onItemClick(int position) {
        Incident incident = (Incident) historyList.get(position);
    }
}
