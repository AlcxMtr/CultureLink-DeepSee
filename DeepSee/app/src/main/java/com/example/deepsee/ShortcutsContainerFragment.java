package com.example.deepsee;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * A fragment representing a container that can contain AppContainers, or buttons for settings.
 * It is deflatable.
 */
public class ShortcutsContainerFragment extends Fragment {

    // List holding AppContainers that allow us to have app shortcuts
    private List<ShortcutContainer> launchables;
    private PackageManager pm;
    RecyclerView launchablesContainer;
    View binding;
    boolean visible;

    public ShortcutsContainerFragment (List<ShortcutContainer> launchables, PackageManager pm) {
        super();
        this.launchables = launchables;
        this.pm = pm;
        this.visible = true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Populate the list of Launchables
        populateLaunchables();

        // Set up the dropdown button
        System.out.println("Shortcut created");
    }

    public void toggleVisibility(){
        this.visible = !visible;
        this.binding.findViewById(R.id.shortcut_layout).setVisibility(visible?View.VISIBLE : View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Shortcut binder");
        this.binding = inflater.inflate(R.layout.shortcuts_fragment, container, false);
        toggleVisibility();
        setupShortcutsRecyclerView(binding);
        // Use the inflated rootView as needed
        ViewGroup.LayoutParams params = launchablesContainer.getLayoutParams();
        launchablesContainer.setLayoutParams(params);
        return binding;
    }

    private void setupShortcutsRecyclerView(View binding) {
        this.launchablesContainer = binding.findViewById(R.id.launchablesContainer);
        // Find RecyclerView within AppDrawerLayout and initialize it
        RecyclerView appRecyclerView = launchablesContainer;
        ShortcutsAdapter adapter = new ShortcutsAdapter();
        appRecyclerView.setAdapter(adapter);
        appRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
    private void populateLaunchables() {
        // Everyone put your buttons and things here.
        // Format them as AppContainers.
        return;
    }
}

