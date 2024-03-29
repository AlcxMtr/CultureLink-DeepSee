package com.example.deepsee.auto_suggest;

import java.util.ArrayList;
import java.util.HashMap;

public class SortedIncrementList {

    // For apps used efficient times or less, insertion will be guaranteed O(1)
    // For apps used more than efficient times, it will in practice be O(1)
    private int efficient;
    private int counter; // How many apps have been added

    private int[] efficientIndices;

    // HashMap stores at which index the counter is stored
    // The key is the app id, the value is the index in 'usageValues' ArrayList
    private HashMap<Integer, Integer> appIndices;

    // ArrayList keeps usage counters for each added app
    // This array is sorted in non-increasing order
    // Supports efficient insertion/increment operation
    private ArrayList<Integer> usageCounters;

    // ArrayList stores app id of any particular index in 'usageValues'
    private ArrayList<Integer> indexToId;

    public SortedIncrementList(int efficient) {
        this.efficient = efficient;
        efficientIndices = new int[efficient + 1];
        for (int i = 0; i <= efficient; i++) {
            efficientIndices[i] = -1;
        }
        efficientIndices[1] = 0;
        counter = 0;
        appIndices = new HashMap<Integer, Integer>();
        usageCounters = new ArrayList<Integer>();
        indexToId = new ArrayList<Integer>();
    }

    // Public method to track when an app has been used
    public void incrementCount(int id) {
        // Is app already tracked?
        if (appIndices.containsKey(id)) {
            increment(id);
        } else {
            newEntry(id);
        }
    }

    // In case we need to add new app and corresponding counter
    private void newEntry(int id) {
        if (id == 0) return;
        appIndices.put(id, counter);
        usageCounters.add(1);
        indexToId.add(id);
        counter++;
    }

    // Efficient counter increment that keeps usageValues sorted
    private void increment(int id) {
        if (id == 0) return;
        int index = appIndices.get(id);
        int value = usageCounters.get(index);
        int increment = index;
        // If value is 'efficient', we find increment index in O(1)
        // We also update the efficient indices
        if (value <= efficient) {
            increment = efficientIndices[value];
            if (value + 1 <= efficient && efficientIndices[value + 1] == -1) {
                efficientIndices[value + 1] = increment;
            }
            efficientIndices[value]++;
        } else {
            // Similar to bubble sort but good for our use case
            while (increment > 0 && usageCounters.get(increment - 1) == value) {
                increment--;
            }
        }
        if (index == increment) {
            usageCounters.set(index, value + 1);
        } else {
            // Need to swap/increment values in all 3 data structures
            usageCounters.set(increment, usageCounters.get(increment) + 1);
            appIndices.put(id, increment);
            int swap_id = indexToId.get(increment);
            appIndices.put(swap_id, index);
            indexToId.set(index, swap_id);
            indexToId.set(increment, id);
        }
    }

    // Return the number of uses of any specific app by its id
    public int numUses(int id) {
        if (appIndices.containsKey(id)) {
            return usageCounters.get(appIndices.get(id));
        } else {
            return 0;
        }
    }

    // Return map of n most used apps and their use numbers
    public HashMap<Integer, Integer> nMostUsed(int n) {
        HashMap<Integer, Integer> mostUsed = new HashMap<Integer, Integer>();
        int m = indexToId.size();
        if (m < n) n = m;
        for (int i = 0; i < n; i++) {
            mostUsed.put(indexToId.get(i), usageCounters.get(i));
        }
        return mostUsed;
    }

    // Return an ordered array of n most used apps (non-increasing)
    public int[] nIntsMostUsed(int n) {
        int m = indexToId.size();
        if (m < n) n = m;
        int[] mostUsed = new int[n];
        for (int i = 0; i < n; i++) {
            mostUsed[i] = indexToId.get(i);
        }
        return mostUsed;
    }
}