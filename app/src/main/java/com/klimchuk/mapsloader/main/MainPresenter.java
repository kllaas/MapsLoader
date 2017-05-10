package com.klimchuk.mapsloader.main;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.klimchuk.mapsloader.R;
import com.klimchuk.mapsloader.adapters.RecyclerAdapter;
import com.klimchuk.mapsloader.data.Region;
import com.klimchuk.mapsloader.data.StaticDataCache;
import com.klimchuk.mapsloader.progress.FragmentProgress;

import java.util.List;

/**
 * Created by alexey on 09.05.17.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    private RecyclerAdapter mAdapter;


    MainPresenter(MainContract.View view) {
        mView = view;

        loadRVAdapter();

        addProgressFragment(null);
    }

    private void addProgressFragment(Region region) {
        FragmentProgress fragment = FragmentProgress.newInstance(region);

        FragmentManager fm = ((AppCompatActivity) mView.getContext()).getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.progress_container, fragment)
                .commit();
    }


    private void loadRVAdapter() {
        List<Region> regions = StaticDataCache.getRegion().getNestedRegions();

        mAdapter = new RecyclerAdapter(regions, mView.getContext(), getOnItemClick(), getOnDownloadClick());

        mView.showRegions(mAdapter);
    }

    private RecyclerAdapter.OnItemClickListener getOnItemClick() {
        return region -> {
            if (region.getNestedRegions().size() != 0) {

                showRegions(region);
            }
        };
    }

    private RecyclerAdapter.OnItemClickListener getOnDownloadClick() {
        return region -> {
            FragmentProgress fragmentProgress = FragmentProgress.newInstance(region);

            FragmentManager fm = ((AppCompatActivity) mView.getContext()).getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.progress_container, fragmentProgress)
                    .commit();

            region.setLoadingState(Region.LoadingState.LOADING);
        };
    }

    @Override
    public void navigateBack() {

        Region topRegion = StaticDataCache.getRegion().getTopRegion();

        if (topRegion != null)
            showRegions(topRegion);
    }

    @Override
    public void onLoadingFinished() {
        mAdapter.notifyDataSetChanged();
    }

    private void showRegions(Region region) {
        StaticDataCache.setCurrentRegion(region);
        List<Region> regions = region.getNestedRegions();

        mAdapter = new RecyclerAdapter(regions, mView.getContext(), getOnItemClick(), getOnDownloadClick());

        mView.showRegions(mAdapter);
    }

}
