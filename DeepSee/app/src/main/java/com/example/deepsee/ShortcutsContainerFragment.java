package com.example.deepsee;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.deepsee.app_drawer.AppContainer;
import com.example.deepsee.placeholder.PlaceholderContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A fragment representing a container that can contain AppContainers, or buttons for settings.
 * It is deflatable.
 */
public class ShortcutsContainerFragment extends Fragment {

    // List holding AppContainers that allow us to have app shortcuts
    private List<AppContainer> launchables;
    private PackageManager pm;
    RecyclerView launchablesContainer;
    View binding;

    public ShortcutsContainerFragment (List<AppContainer> launchables, PackageManager pm) {
        super();
        this.launchables = launchables;
        this.pm = pm;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Populate the list of Launchables
        populateLaunchables();

        // Set up the dropdown button
        this.launchablesContainer = view.findViewById(R.id.launchablesContainer);
//        setVisibility();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = inflater.inflate(R.layout.shortcuts_fragment, container, false);
        System.out.println("Called binder");
        // Use the inflated rootView as needed
        ViewGroup.LayoutParams params = launchablesContainer.getLayoutParams();
        launchablesContainer.setLayoutParams(params);
        return binding;
    }


    public void setVisibility() {
        int newVisibility = (this.launchablesContainer.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
        this.launchablesContainer.setVisibility(newVisibility);

        // Adjust RecyclerView height based on visibility
        ViewGroup.LayoutParams params = launchablesContainer.getLayoutParams();
        params.height = newVisibility == View.VISIBLE ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;
        launchablesContainer.setLayoutParams(params);
    }

    private void populateLaunchables() {
        // Everyone put your buttons and things here.
        // Format them as AppContainers.
        return;
    }
}

