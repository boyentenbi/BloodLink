package samples.exoguru.bloodlink;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmDonationFragment extends DialogFragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private Button cancelButton;
    private Button OKButton;
    private EditText editText;
    private Incident incident;


    public Incident getIncident() {
        return incident;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.confirm_donation_fragment, container);

        getDialog().setTitle("Confirm your donation: ");

        this.editText = (EditText) view.findViewById(R.id.confirmationCodeEditText);


        cancelButton = (Button) view.findViewById(R.id.CANCELbutton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Confirmation cancelled", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        OKButton = (Button) view.findViewById(R.id.OKbutton);
        OKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptConfirm();

            }
        });

/*
        View editView = view.findViewById(R.id.editText);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editView, InputMethodManager.SHOW_IMPLICIT);
*/
        // Do something else
        return view;
    }

    private void attemptConfirm() {
        String attemptedCode = editText.getText().toString();

        if (attemptedCode.equals(this.incident.getActualCode())) {

            // Update the donated state of the incident
            this.incident.setDonated(true);

            // Remove the incident from the alerts file and add it to the history file
            this.mainActivity.alertsFragment.alertsExpandableListAdapter.alertsList.moveToHistory(this.incident);

            // Reload the objects
            this.mainActivity.alertsFragment.alertsExpandableListAdapter.alertsList = IncidentsList.getEligibleNowAlertsList();
            this.mainActivity.historyFragment.historyListViewAdapter.historyList = IncidentsList.getHistoryList();

            // Update both tab views
            this.mainActivity.historyFragment.historyListViewAdapter.notifyDataSetChanged();
            this.mainActivity.alertsFragment.alertsExpandableListAdapter.notifyDataSetChanged();

            Toast.makeText(this.getActivity(), "Donation confirmed", Toast.LENGTH_SHORT).show();

            // Update the date of last donation
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("lastDonationDateInMillis", Long.toString(incident.getRequestEndDateInMillis()));
            editor.commit();
            dismiss();


        } else {
            Toast.makeText(getActivity(), "Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        Log.v("DonationFragment", "Button clicked");
    }


}