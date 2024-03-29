package com.example.deepsee;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.example.deepsee.auto_suggest.AlgoStruct;

import java.util.HashMap;
import java.util.List;
import java.io.*;

// This class handles the storage, reading, and management of app-local files.
// Use it to get or set data structures and effectively memoize them for use later.
// Ideally one instance of this is created at a given time.
// The get and set functions can be used without the object but ensure
public class StorageManager {

    //public Matrices matrices;   // For Alex. Replace "Matrices" with the object you use to store stuff.
    public HashMap categories;
    public List<ApplicationInfo> apps;      // For Fahim

    private Context context;

    public StorageManager(Context context) {
        //this.matrices = null;
        this.categories = null;
        this.apps = null;
        this.context = context;
    }

    // APPS LIST FOR APP DRAWER:
    private List<ApplicationInfo> getApps() {
        this.apps = readObjectFromFile("apps.dat");
        return this.apps;
    }

    private void setApps(List<ApplicationInfo> apps) {
        writeObjectToFile("apps.dat", apps);
    }

    // CATEGORIES:
    private HashMap getCategories() {
        this.categories = readObjectFromFile("categories.dat");
        return this.categories;
    }

    private void setCategories(HashMap categories) {
        writeObjectToFile("categories.dat", categories);
    }

    // MATRICES:
    //If file does not exist yet, need to handle it
//    private Matrices getMatrices() {
//        this.matrices = readObjectFromFile("matrices.dat");
//        return this.matrices;
//    }

//    private void setMatrices(Matrices matrices) {
//        writeObjectToFile("matrices.dat", matrices);
//    }

    // General reader and writer helper functions. They return null if the read or write failed
    // for any reason.
    private <T> T readObjectFromFile(String filename) {
        try (FileInputStream fis = this.context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            // bad bad unchecked cast:
            return (T) ois.readObject();
        } catch (Exception e) {
            // Handle file not found or failed to read
            System.out.println( "Error reading from file " + filename + e.toString());
            return null;
        }
    }

    private <T> void writeObjectToFile(String filename, T object) {
        try (FileOutputStream fos = this.context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        } catch (Exception e) {
            // Handle failed to write
            System.out.println( "Error writing to file " + filename + e.toString());
        }
    }
}