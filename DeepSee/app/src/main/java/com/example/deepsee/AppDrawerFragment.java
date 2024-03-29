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
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.deepsee.app_drawer.CategoriesAdapter;
import com.example.deepsee.app_drawer.CategoryContainer;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AppDrawerFragment extends Fragment {

    public static boolean deleteMode = false;
    private View binding;
    private final HashMap<Integer, List<ApplicationInfo>> categories;
    private final PackageManager pm;
    private RecyclerView appRecyclerView;

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
        appRecyclerView = binding.findViewById(R.id.categories_recycler);
        CategoriesAdapter adapter = new CategoriesAdapter(categories, pm, appRecyclerView, this);
        appRecyclerView.setAdapter(adapter);
        appRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    //Sets toolbar to have correct information
    public static void setDrawerMode(boolean mode, AppDrawerFragment v){
        if (mode){
            v.getView().findViewById(R.id.undo_uninstalling_button).setVisibility(View.VISIBLE);
            v.getView().findViewById(R.id.cancel_uninstalling_button).setVisibility(View.VISIBLE);
            v.getView().findViewById(R.id.done_uninstalling_button).setVisibility(View.VISIBLE);
            ((TextView)v.getView().findViewById(R.id.drawer_fragment_title)).setText("Delete Mode");
        }
        else{
            v.getView().findViewById(R.id.undo_uninstalling_button).setVisibility(View.GONE);
            v.getView().findViewById(R.id.cancel_uninstalling_button).setVisibility(View.GONE);
            v.getView().findViewById(R.id.done_uninstalling_button).setVisibility(View.GONE);
            ((TextView)v.getView().findViewById(R.id.drawer_fragment_title)).setText("All Apps");
        }
    }

}