package com.klimchuk.mapsloader.progress;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.klimchuk.mapsloader.R;
import com.klimchuk.mapsloader.data.Region;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alexey on 09.05.17.
 */

public class FragmentProgress extends Fragment implements ProgressContract.View {

    public static final String KEY_FILE = "file_name";
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final String KEY_REGION = "region";

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_progress)
    TextView tvProgress;

    @BindView(R.id.tv_progress_title)
    TextView tvTitle;

    private ProgressContract.Presenter mPresenter;

    /**
     * @param region - Region to download. Put null if want to show storage data.
     */
    public static FragmentProgress newInstance(Region region) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_REGION, region);

        FragmentProgress fragment = new FragmentProgress();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_bar, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Region downloadRegion = (Region) getArguments().getSerializable(KEY_REGION);

        mPresenter = new ProgressPresenter(this, downloadRegion);
    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }

    @Override
    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void setProgressText(String text) {
        tvProgress.setText(text);
    }

    @Override
    public void setProgressTitle(int text) {
        tvTitle.setText(getString(text));
    }
}
