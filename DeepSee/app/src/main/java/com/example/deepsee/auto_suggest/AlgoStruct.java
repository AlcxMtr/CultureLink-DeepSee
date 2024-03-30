package com.example.deepsee.auto_suggest;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AlgoStruct implements Serializable {

    // These will be used to initialize efficient SortedIncrementLists
    public static final int EFFICIENT = 10;
    public static final int EFFICIENT_APPS = 5;

    // number of times an app must be used for us to start considering it
    public static final int SIGNIFICANT_USES = 3;

    // Coefficient on depth of search for comparison algorithm
    public static final int DEPTH = 2;

    // Number of apps opened
    public int numApps;

    // ID of last opened app
    private int lastApp;

    // HashMaps link each app to a local ID
    private HashMap<String, Integer> appIdMap;
    private HashMap<Integer, String> idAppMap;

    // SortedIncrementArray keeps usage counters for each opened app
    // All operations are O(1) except nMostUsed which is O(n)
    private SortedIncrementList appUsageList;

    // Each app that has been used 3 times+ has an entry in the list
    // Each entry has an associated list of counters for apps opened in sequence after it
    private HashMap<Integer, SortedIncrementList> sequenceMatrix;

    // L
    private SortedIncrementList[] hourOfDay;

    public AlgoStruct() {
        numApps = 0;
        lastApp = 0;
        appIdMap = new HashMap<String, Integer>();
        idAppMap = new HashMap<Integer, String>();
        appUsageList = new SortedIncrementList(EFFICIENT);
        sequenceMatrix = new HashMap<Integer, SortedIncrementList>();
        hourOfDay = new SortedIncrementList[24]; // 24 hours in a day
        for (int i = 0; i < 24; i++) {
            hourOfDay[i] = new SortedIncrementList(EFFICIENT_APPS);
        }
    }

    // Method to increment the count for a specific app
    // If the app is new, need to handle adding new app
    public void appOpened(String appName) {
        int appId; // App's index in appIndexMap
        // Check if the app is already in appIndexMap
        if (appIdMap.containsKey(appName)) {
            // Get the app's id
            appId = appIdMap.get(appName).intValue();
        } else {
            // Generate new index if app is new
            numApps += 1;
            appId = numApps;
            appIdMap.put(appName, appId);
            idAppMap.put(appId, appName);
        }
        // Update the app counter and matrices

        appUsageList.incrementCount(appId);
        if (appUsageList.numUses(appId) >= SIGNIFICANT_USES) {
            updateMatrices(appId);
        }
    }

    // Method to update matrices based on opened app
    private void updateMatrices(int appId) {
        if (lastApp != 0) {
            // Update sequence matrix
            if (!(sequenceMatrix.containsKey(lastApp))) {
                sequenceMatrix.put(lastApp, new SortedIncrementList(EFFICIENT_APPS));
            }
            sequenceMatrix.get(lastApp).incrementCount(appId);
        }
        lastApp = appId;
        // Get the current time and hour of the day
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        // Update time of day matrix
        hourOfDay[hour].incrementCount(appId);
    }

    // This inner class will let us compare app usage values in a maxHeap
    static class AppValue implements Comparable<AppValue> {
        int appId; // App ID
        int value; // App Usage
        public AppValue(int appId, int value) {
            this.appId = appId;
            this.value = value;
        }
        // Implement compareTo method to compare apps based on usage
        @Override
        public int compareTo(AppValue other) {
            // Compare in descending order for our max heap
            return Integer.compare(other.value, this.value);
        }
    }

    // Find the n most relevant apps for current situation
    // If we can't find n, we return the largest number that we can find
    public String[] mostRelevant(int n) {
        PriorityQueue<AppValue> maxHeap = new PriorityQueue<AppValue>();

        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();

        // May be null if no usage values available
        SortedIncrementList sequenceList = sequenceMatrix.get(lastApp);
        SortedIncrementList thisHourList = hourOfDay[hour];

        // The arraylists with number of times each app was opened
        // MAKE SURE WE DO NOT MODIFY THEM (breaking encapsulation but we get O(1) complexity)
        boolean sequence = false;
        boolean thisHour = false;
        HashMap<Integer, Integer> sequenceMap = null;
        HashMap<Integer, Integer> thisHourMap = null;
        if (sequenceList != null) {
            sequenceMap = sequenceList.nMostUsed(DEPTH*n);
            sequence = true;
        }
        if (thisHourList != null) {
            thisHourMap = thisHourList.nMostUsed(DEPTH*n);
            thisHour = true;
        }

        // Add the apps that are in sequenceMap and in both maps
        if (sequence) {
            for (int id : sequenceMap.keySet()) {
                int value = 0;
                if (thisHour && thisHourMap.containsKey(id)) {
                    // THIS IS THE ALGORITHM'S CORE RELATIONSHIP
                    value = sequenceMap.get(id) + thisHourMap.get(id);
                    thisHourMap.remove(id);
                } else {
                    value = sequenceMap.get(id);
                }
                AppValue app = new AppValue(id, value);
                maxHeap.add(app);
            }
        }
        // Add the apps that are in thisHourMap
        if (thisHour) {
            for (int id : thisHourMap.keySet()) {
                int value = thisHourMap.get(id);
                AppValue app = new AppValue(id, value);
                maxHeap.add(app);
            }
        }
        // Return up to n apps from the current maxHeap (check for null)
        // If data unavailable from matrices/maxHeap, grab from overall appUsageList
        // If data unavailable from anywhere past some index, return null

        // Hashmap of added app ids
        HashMap<Integer, Boolean> addedId = new HashMap<Integer, Boolean>();

        int l = n;
        int m = numApps;
        if (m < l) l = m;
        m = maxHeap.size();
        String[] topApps = new String[n];

        for (int i = 0; i < m; i++) {
            int appId = maxHeap.poll().appId;
            topApps[i] = idAppMap.get(appId);
            addedId.put(appId, true);
        }
        // In case not enough data in maxHeap, we add from overall appUsageList
        int j = 0;
        int[] appIds = appUsageList.nIntsMostUsed(l);
        for (int i = m; i < l; i++) {
            while (addedId.containsKey(appIds[j])) {
                j++;
            }
            topApps[i] = idAppMap.get(appIds[j]);
            j++;
        }
        return topApps;
    }
}
