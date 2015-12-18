package com.mycompany.bloodlinkhospital;


import android.view.View;

import java.util.Date;

/**
 * Created by Peter on 29/07/2015.
 */
public class Incident {

    private int incidentId;
    private int hospitalId;
    private String bloodType;
    private String requestMessage;
    private String thanksMessage;
    private int totalResponsesNeeded;
    private boolean donated;
    private String actualCode;
    private Date requestEndDate;
    private int responsesHad;

    private View view;

    // Class constructor
    public Incident(int incidentId, int hospitalId, String bloodType, int totalResponsesNeeded, String requestMessage, String thanksMessage, Date requestEndDate, int responsesHad) {

        // Set Incident fields
        this.incidentId = incidentId;
        this.hospitalId = hospitalId;
        this.bloodType = bloodType;
        this.thanksMessage = thanksMessage;
        this.requestMessage = requestMessage;
        this.totalResponsesNeeded = totalResponsesNeeded;
        this.donated = false;
        this.actualCode = "999";
        this.requestEndDate = requestEndDate;
        this.responsesHad = responsesHad;
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

    public View getView() {
        return view;
    }

    public Date getRequestEndDate() {
        return requestEndDate;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public String getThanksMessage() {
        return thanksMessage;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public String getBloodType() {
        return bloodType;
    }

    public int getTotalResponsesNeeded() {
        return totalResponsesNeeded;
    }

    public int getResponsesHad() { return responsesHad;}

    public boolean getDonated() {
        return donated;
    }

    public String getActualCode() {
        return actualCode;
    }

}
