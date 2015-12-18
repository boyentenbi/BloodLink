package samples.exoguru.bloodlink;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by peter on 11/08/15.
 */
public class IncidentsList extends ArrayList<Incident> implements Serializable {

    static final long serialVersionUID = -8269919032459722816L;

    static MainActivity mainActivity;
    static String alertsFileName = "alertsList";
    static String historyFileName = "historyList";

    /*
        private static void sortOldAlerts() {

            try {
                FileInputStream fis = mainActivity.getBaseContext().openFileInput(alertsFileName);
                ObjectInputStream is = new ObjectInputStream(fis);
                // Read the alerts list
                IncidentsList dirtyAlertsList = (IncidentsList) is.readObject();

                Log.i("loadFromFile", "dirtyIncidentsList is size " + dirtyAlertsList.size());

                // Close the input streams
                is.close();
                fis.close();

                // Prune the inappropriate alerts
                IncidentsList cleanAlertsList = new IncidentsList();

                Date date = new Date();

                for (Incident alert : dirtyAlertsList) {

                    if (alert.getRequestEndDateInMillis() < date.getTime()
                            && !alertt.getDonated()) {

                        cleanAlertsList.add(alert);

                    }
                }
            } catch (Exception Ex) {
                Log.e("cleanAlertsListFile", Ex.getMessage());

            }
        }
    */
    private static void checkFileExists(String fileName) {

        //Log.i("checkFile", "Checking for file: " + fileName);

        try {
            File f = new File(mainActivity.getBaseContext().getFilesDir(), fileName);
            // Make the alerts file if it doesn't exist and add a new object
            if (!f.exists() || f.isDirectory()) {

                Log.i("checkFile", "No file named " + fileName + ", making a new one!");
                f.createNewFile();
                IncidentsList incidentsList = new IncidentsList();

                incidentsList.saveToFile(fileName);

            } else {
                //Log.i("checkFile", "File " + fileName + " exists, didn't make a new one!");
            }
        } catch (Exception Ex) {
            Log.e("checkFile", Ex.getMessage());
        }
    }

    public static IncidentsList getEligibleNowAlertsList() {

        try {
            IncidentsList alertsList = loadFromFile(alertsFileName);

            //Log.i("getAlerts", "alertsList is size " + alertsList.size() + " before operations");
            //Log.i("getAlerts", "historyList is size " + historyList.size() + " before operations");

            // Make a new list for the eligible now alerts
            IncidentsList eligibleNowAlertsList = new IncidentsList();

            Date date = new Date();

            // Iterate over alerts list
            for (Incident incident : alertsList) {

                Log.i("getAlerts", "checking: " + incident);

                boolean isExpired = incident.getRequestEndDateInMillis() < date.getTime();
                //boolean donated = incident.getDonated();
                boolean doesBloodMatch = doesBloodMatch(incident.getBloodType());

                //Log.i("getAlert", " isExpired: " + Boolean.toString(isExpired));
                //Log.i("getAlert", " donated: " + Boolean.toString(donated));
                //Log.i("getAlert", " bloodMatch: " + Boolean.toString(doesBloodMatch));

                // Add appropriate alerts to eligible now alerts list
                if (doesBloodMatch && !isExpired) {
                    Log.i("getAlert", " adding to eligible now");
                    eligibleNowAlertsList.add(incident);
                }

                // Remove all expired alerts from alerts file
                if (isExpired) {
                    Log.i("getAlert", "popping from alerts list");
                    removeFromFile(incident, alertsFileName);
                }

            }

            Log.i("getAlerts", "eligibleIncidentsList is size " + eligibleNowAlertsList.size());

            // Return the alerts list that has the alerts for which the user is eligible now
            return eligibleNowAlertsList;

        } catch (Exception Ex) {
            String err = (Ex.getMessage()==null)?"Null message":Ex.getMessage();
            Log.e("getAlerts", err);
            return null;
        }
    }

    public static IncidentsList getHistoryList() {

        return loadFromFile(historyFileName);

    }

    private static IncidentsList loadFromFile(String fileName) {

        checkFileExists(fileName);

        Log.i("loadFromFile", "reading from file: " + fileName);

        try {
            // Start the input streams (what are they?)
            FileInputStream fis = mainActivity.getBaseContext().openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);

            // Read the alerts list
            IncidentsList incidentsList = (IncidentsList) is.readObject();

            // Close the input streams
            is.close();
            fis.close();

            Log.i("loadFromFile", "list from " + fileName + " is size " + incidentsList.size());

            return incidentsList;

        } catch (Exception Ex) {
            Log.i("loadFromFile", Ex.getMessage());
            return null;
        }

    }

    private void saveToFile(String fileName) {

        Log.i("saveToFile", "saving to file: " + fileName);

        try {
            // Start output streams (also no idea what they are)
            FileOutputStream fos = mainActivity.getBaseContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);

            // Write the list to file
            os.writeObject(this);

            // Close the outputs streams
            os.close();
            fos.close();
        } catch (Exception Ex) {
            Log.i("saveToFile", Ex.getMessage());
        }

    }

    // The file is the canonical alert list. We only insert to file and read from file.
    // We do not insert to the object.
    public static void insertToFile(Incident newIncident, String fileName) throws IOException, ClassNotFoundException {

        IncidentsList incidentsList = loadFromFile(fileName);

        incidentsList.insertToObject(newIncident);

        Log.i("insertToFile", "List is size " + incidentsList.size() + " after insertion");

        incidentsList.saveToFile(fileName);
    }

    private void insertToObject(Incident newIncident) {

        if (this.size() > 0) {

            // Iterate over alerts already there, starting from the end
            for (int position = this.size() - 1; position >= 0; position--) {
                Log.i("insertToFile", "Checking position " + position);

                // If we're at 0th position, the new alert is the closest to expiring.
                if (position == 0) {
                    this.add(0, newIncident);
                    // redundant break
                    break;
                }
                // If not at 0th position
                else {
                    // If new alert expires between the position alert and the one before that, it goes exactly here
                    if (newIncident.getRequestEndDateInMillis() <= this.get(position).getRequestEndDateInMillis()
                            && newIncident.getRequestEndDateInMillis() >= this.get(position - 1).getRequestEndDateInMillis()) {
                        this.add(position, newIncident);
                        Log.i("insertToObject", "New alert inserted in position " + position);
                        break;
                    }
                }
            }
        } else {
            this.add(newIncident);
        }
    }

    private static void removeFromFile(Incident oldIncident, String fileName) {

        IncidentsList incidentsList = loadFromFile(fileName);

        //Log.i("removeFromFile", "incidents list is size " + Integer.toString(incidentsList.size()) + "before removal");

        int poppedCounter = 0;

        Iterator<Incident> iter = incidentsList.iterator();

        while (iter.hasNext()) {
            Incident incident = iter.next();

            if (incident.getStartDateInMillis().equals(oldIncident.getStartDateInMillis())) {
                iter.remove();
                poppedCounter++;
            }
        }


        /*
        int oldSize = incidentsList.size();


        for (int i = 0; i < incidentsList.size(); i++) {

            if (incidentsList.get(i).getIncidentId().equals(oldIncident.getIncidentId())) {

                incidentsList.remove(incident);

            }
        }*/

        if (poppedCounter != 1) {
            Log.e("removeFromFile", "popped " + poppedCounter + " incidents from " + fileName);
        }


        //Log.i("removeFromFile", "incidents list is size " + Integer.toString(incidentsList.size()) + "after removal");

        incidentsList.saveToFile(fileName);

    }

    public void moveToHistory(Incident incident) {

        removeFromFile(incident, alertsFileName);

        try {
            // Don't use insert method because we always add to beginning of history
            IncidentsList historyList = loadFromFile(historyFileName);
            historyList.add(0, incident);
            historyList.saveToFile(historyFileName);
        } catch (Exception Ex) {
            Log.e("moveToHistory", Ex.getMessage());
        }
    }
/*
    private static void addDummyData(int numAlerts) {

        // Create dummy Incident objects
        for (int i = 0; i < numAlerts; i++) {

            String incidentId = Integer.toString(i);
            String hospitalId = "000";
            String bloodType = "B+";
            String requestMessage = "Dummy request message" + incidentId
                    + " at hospital " + hospitalId + " plz!";
            String thanksMessage = "thanksMessage set in dummy creation method";
            int responsesNeeded = 20;
            int responsesHad = 0;

             // creates calendar
          // sets calendar time/date
           // adds one hour
            long requestEndDateInMillis = cal.getTimeInMillis(); // returns new date object, one hour in the future

            Incident newIncident = new Incident(incidentId, hospitalId,
                    bloodType, responsesNeeded, requestMessage,
                    thanksMessage, requestEndDateInMillis, responsesHad);

            // cba to create all objects and then save, just repeat method
            try {
                insertToFile(newIncident);
            } catch (Exception Ex) {
                Log.e("addDummyData", Ex.getMessage());
            }

        }

    }
    */

    public static boolean doesBloodMatch(String bloodType1) {


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mainActivity.getBaseContext());

        String bloodType2 = preferences.getString("bloodType", "defaultBloodType");

        //Log.i("doesBloodMatch", "alerts have blood types " + bloodType1 + ", " + bloodType2 + " respectively.");

        boolean bloodMatch = bloodType1.equals(bloodType2);


        return bloodMatch;
    }


}
