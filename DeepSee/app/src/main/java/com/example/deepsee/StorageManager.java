package com.example.deepsee;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.deepsee.auto_suggest.AlgoStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

// This class handles the storage, reading, and management of app-local files.
// Use it to get or set data structures and effectively memoize them for use later.
// Ideally one instance of this is created at a given time.
// The get and set functions can be used without the object but ensure
public class StorageManager {

    public AlgoStruct algoStruct;
    public HashMap<Integer, List<ApplicationInfo>> categories;
    public List<ApplicationInfo> apps;      // For Fahim

    private Context context;

    public StorageManager(Context context) {
        this.algoStruct = null;
        this.categories = null;
        this.apps = null;
        this.context = context;
    }

    // APPS LIST FOR APP DRAWER:
    public List<ApplicationInfo> getApps() {
        this.apps = stringToApp((List<String>) readObjectFromFile("apps.dat"));
        return this.apps;
    }

    public void setApps(List<ApplicationInfo> apps) {
        this.apps = apps;
        writeObjectToFile("apps.dat", (Serializable) appToString(apps));
    }

    private List<ApplicationInfo> stringToApp(List<String> strings){
        if (strings != null) {
            List<ApplicationInfo> list = new LinkedList<>();
            for (String s : strings) {
                ApplicationInfo info = new ApplicationInfo();
                info.packageName = s;
                list.add(info);
            }
            return list;
        }
        else
            return null;
    }

    public List<String> appToString(List<ApplicationInfo> infos){
        List<String> strings = new LinkedList<>();
        for (ApplicationInfo info:infos){
            strings.add(info.packageName);
        }

        return strings;
    }

    // CATEGORIES:
    public HashMap<Integer, List<ApplicationInfo>> getCategories() {
        HashMap<Integer, List<String>> unconverted = (HashMap<Integer, List<String>>) readObjectFromFile("categories.dat");
        if (unconverted != null){
            this.categories = new HashMap<>();
            unconverted.forEach((key,val) -> this.categories.put(key, stringToApp(val)));
            return this.categories;
        }
        return null;
    }

    public void setCategories(HashMap<Integer, List<ApplicationInfo>> unconverted) {
        HashMap<Integer, List<String>> converted = new HashMap<>();
        unconverted.forEach((key,val) -> converted.put(key, appToString(val)));
        writeObjectToFile("categories.dat", converted);
    }

    // Structures containing data for app suggestion algorithm
    public AlgoStruct getAlgoStruct() {
        this.algoStruct = (AlgoStruct) readObjectFromFile("algoStruct.dat");
        return this.algoStruct;
    }

    public void setAlgoStruct(AlgoStruct struct) {
        this.algoStruct = struct;
        writeObjectToFile("algoStruct.dat", struct);
    }

    // General reader and writer helper functions. They return null if the read or write failed
    // for any reason.
    private Serializable readObjectFromFile(String filename) {
        try (FileInputStream fis = this.context.openFileInput(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // bad bad unchecked cast:
            Serializable obj = (Serializable) ois.readObject();
            ois.close();
            return obj;
        } catch (Exception e) {
            // Handle file not found or failed to read
            System.out.println( "Error reading from file " + filename + e.toString());
            return null;
        }
    }

    public void updateStorageManager(){
        PackageManager pm = context.getPackageManager();
        apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Remove System Packages from the list before drawing:
        apps.removeIf(packageInfo ->
                (pm.getLaunchIntentForPackage(packageInfo.packageName) == null));

        //Create dictionary from category number -> list of apps in said category
        categories = new HashMap<>();
        for (ApplicationInfo p : apps) {
            if (!categories.containsKey(p.category)) {
                categories.put(p.category, new ArrayList<ApplicationInfo>());
            }
            categories.get(p.category).add(p);
        }
        syncStorageManager();
    }

    public void syncStorageManager(){
        setAlgoStruct(algoStruct);
        setApps(apps);
        setCategories(categories);
        System.out.println("Updated file sys....-----------------------------------------------------------------------------\n\n\n");
    }

    private void writeObjectToFile(String filename, Serializable object) {
        try {
            FileOutputStream fos = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(object);
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println( "Error writing to file " + filename + e.toString());
        }
    }

}