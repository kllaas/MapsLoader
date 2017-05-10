package com.klimchuk.mapsloader.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.klimchuk.mapsloader.R;
import com.klimchuk.mapsloader.progress.ActivityBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View, ActivityBinding.ProgressFinishing {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mPresenter = new MainPresenter(this);
    }

    @Override
    public void showRegions(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        mPresenter.navigateBack();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLoadingFinished() {
        mPresenter.onLoadingFinished();
    }
}
