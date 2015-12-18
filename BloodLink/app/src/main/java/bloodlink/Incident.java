package samples.exoguru.bloodlink;


import android.view.View;

import java.io.Serializable;

/**
 * Created by Peter on 29/07/2015.
 */
public class Incident implements Serializable {

    static final long serialVersionUID = 519526666815578075L;

    private String callerName;
    private String hospitalName;
    private String bloodType;
    private String requestMessage;
    private String thanksMessage;
    private int totalResponsesNeeded;
    private boolean donated;
    private String actualCode;
    private Long requestEndDateInMillis;


    private Long startDateInMillis;

    private View view;


    // Class constructor
    public Incident(String callerName, String hospitalName, String bloodType,
                    int totalResponsesNeeded, String requestMessage, String thanksMessage,
                    Long requestEndDateInMillis, String actualCode, Long startDateInMillis) {

        // Set Incident fields
        this.callerName = callerName;
        this.hospitalName = hospitalName;
        this.bloodType = bloodType;
        this.thanksMessage = thanksMessage;
        this.requestMessage = requestMessage;
        this.totalResponsesNeeded = totalResponsesNeeded;
        this.donated = false;
        this.actualCode = actualCode;
        this.requestEndDateInMillis = requestEndDateInMillis;
        this.startDateInMillis = startDateInMillis;
    }

    /**
     * ***** Set Methods ***************
     */

    public void setDonated(boolean donated) {
        this.donated = donated;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * ****** Get Methods ***************
     */

    public String getCallerName() {
        return callerName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getStartDateInMillis() {
        return startDateInMillis;
    }

    public View getView() {
        return view;
    }

    public Long getRequestEndDateInMillis() {
        return requestEndDateInMillis;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public String getThanksMessage() {
        return thanksMessage;
    }


    public String getHospitalName() {
        return hospitalName;
    }

    public String getBloodType() {
        return bloodType;
    }

    public int getTotalResponsesNeeded() {
        return totalResponsesNeeded;
    }


    public boolean getDonated() {
        return donated;
    }

    public String getActualCode() {
        return actualCode;
    }

}
