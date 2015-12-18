package com.mycompany.bloodlinkhospital;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForceCompleteFragment extends DialogFragment implements View.OnClickListener {

    private AlertsFragment alertsFragment;

    private CompletedFragment completedFragment;
    private Button cancelButton;
    private Button OKButton;
    private EditText editText;
    private Incident incident;
    private String actualCode;

    public void setCompletedFragment(CompletedFragment completedFragment) {
        this.completedFragment = completedFragment;
    }


    public void setAlertsFragment(AlertsFragment alertsFragment) {
        this.alertsFragment = alertsFragment;
    }

    public void setIncident(Incident incident){
        this.incident = incident;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.actualCode = this.incident.getActualCode();
        View view = inflater.inflate(R.layout.force_complete_fragment, container);

        getDialog().setTitle("Enter key to force complete:");

        this.editText = (EditText) view.findViewById(R.id.forceCompleteKeyEditText);


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

        if (attemptedCode.equals(this.actualCode)) {
            this.incident.setDonated(true);

            Toast.makeText(this.getActivity(), "Donation confirmed", Toast.LENGTH_SHORT).show();
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