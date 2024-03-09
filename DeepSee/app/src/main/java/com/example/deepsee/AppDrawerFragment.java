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
import com.example.deepsee.databinding.ActivityMainBinding;
import android.content.pm.ApplicationInfo;
import com.example.deepsee.app_drawer.AppsAdapter;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


public class AppDrawerFragment extends Fragment {

    private ActivityMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupAppRecyclerView();
    }

    private void setupAppRecyclerView() {
        final PackageManager pm = requireActivity().getPackageManager();

        // Get Package List:
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_META_DATA);

        // Remove System Packages from the list before drawing:
        apps.removeIf(packageInfo ->
                (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);

        // Create and draw the app drawer as a recyclerView:
        RecyclerView appRecyclerView = binding.getRoot().findViewById(R.id.recycler_view);
        AppsAdapter adapter = new AppsAdapter(apps, pm);
        appRecyclerView.setAdapter(adapter);
        appRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 5));
    }
}