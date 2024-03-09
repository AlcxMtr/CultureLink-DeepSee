package com.example.deepsee;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import android.content.pm.ApplicationInfo;
import com.example.deepsee.app_drawer.AppsAdapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AppDrawerFragment extends Fragment {

    private View binding;

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

    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(binding.getContext().LAYOUT_INFLATER_SERVICE);
        binding = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(binding);
    }

    private void setupAppRecyclerView() {
        final PackageManager pm = requireActivity().getPackageManager();

        // Get Package List:
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_META_DATA);

        // Remove System Packages from the list before drawing:
        apps.removeIf(packageInfo ->
                (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);

        // Create and draw the app drawer as a recyclerView:
        RecyclerView appRecyclerView = binding.findViewById(R.id.apps_recycler);
        AppsAdapter adapter = new AppsAdapter(apps, pm);
        appRecyclerView.setAdapter(adapter);
        appRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 5));
    }
}