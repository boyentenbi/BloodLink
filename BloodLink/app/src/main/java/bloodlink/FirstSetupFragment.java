package samples.exoguru.bloodlink;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FirstSetupFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    private Button gotItButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.first_setup_fragment, container);

        getDialog().setTitle(getString(R.string.firstSetupTitle));

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        gotItButton = (Button) view.findViewById(R.id.gotItButton);
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotIt();

            }
        });

        // Do something else
        return view;
    }

    private void gotIt() {
        Intent i = new Intent(getActivity(), SettingsActivity.class);
        startActivity(i);
        Toast.makeText(getActivity(), "Closed first time setup prompt", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void onClick(View v) {
        Log.v("FirstSetupFragment", "Button clicked");
    }

}