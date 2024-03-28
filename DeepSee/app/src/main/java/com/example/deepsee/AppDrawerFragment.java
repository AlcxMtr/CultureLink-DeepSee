package com.example.deepsee;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import com.example.deepsee.app_drawer.CategoriesAdapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AppDrawerFragment extends Fragment {

    private View binding;
    private HashMap categories;
    private List<ApplicationInfo> apps;
    private PackageManager pm;

    public AppDrawerFragment(HashMap<Integer, List<ApplicationInfo>> categories, List<ApplicationInfo> apps, PackageManager pm) {
        super();
        this.categories = categories;
        this.pm = pm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflater.inflate(R.layout.fragment_app_drawer, container, false);
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupAppRecyclerView();
    }

    private void setupAppRecyclerView() {
        // Find RecyclerView within AppDrawerLayout and initialize it
        RecyclerView appRecyclerView = binding.findViewById(R.id.categories_recycler);
        CategoriesAdapter adapter = new CategoriesAdapter(categories, pm, appRecyclerView);
        appRecyclerView.setAdapter(adapter);
        appRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

}