package com.mycompany.bloodlinkhospital;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CompletedListViewAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private CompletedFragment completedFragment;
    private ArrayList historyList;
    private FragmentActivity activity;
    private static LayoutInflater inflater=null;
    public Resources res;
    Incident incident = null;
    int i=0;


    public CompletedListViewAdapter(CompletedFragment completedFragment, ArrayList historyList, Resources resLocal) {

        /********** Take passed values **********/
        this.completedFragment = completedFragment;
        this.activity = completedFragment.getActivity();
        this.historyList= historyList;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) completedFragment.getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if(historyList.size()<=0)
            return 1;
        return historyList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView headerTextView;
        public TextView thanksMessageTextView;
        public TextView pointsTextView;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if(convertView==null){

            view = inflater.inflate(R.layout.completed_requests_row, null);

            holder = new ViewHolder();

            holder.headerTextView = (TextView) view.findViewById(R.id.headerText);
            holder.thanksMessageTextView= (TextView)view.findViewById(R.id.thanksMessageText);
            holder.pointsTextView=(TextView)view.findViewById(R.id.pointsText);

            /************  Set holder with LayoutInflater ************/
            view.setTag( holder );
        }
        else
            holder=(ViewHolder)view.getTag();

        if(historyList.size()<=0)
        {
            holder.headerTextView.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            incident=null;
            Incident incident = (Incident) historyList.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.headerTextView.setText("Donated " + incident.getBloodType() + " blood to hospital number " + incident.getHospitalId());
            holder.thanksMessageTextView.setText("thanks message text set by method");
            holder.pointsTextView.setText("99");

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            view.setOnClickListener(new OnItemClickListener( position ));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int position;

        OnItemClickListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
            completedFragment.onItemClick(position);
        }
    }
}

