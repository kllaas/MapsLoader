package com.klimchuk.mapsloader.progress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.LocalBroadcastManager;

import com.klimchuk.mapsloader.R;
import com.klimchuk.mapsloader.data.Download;
import com.klimchuk.mapsloader.data.Region;
import com.klimchuk.mapsloader.main.MainActivity;
import com.klimchuk.mapsloader.progress.ActivityBinding.ProgressFinishing;
import com.klimchuk.mapsloader.services.DownloadService;

import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal.REGION;
import static com.klimchuk.mapsloader.progress.FragmentProgress.MESSAGE_PROGRESS;

/**
 * Created by alexey on 04.05.17.
 */

public class ProgressPresenter implements ProgressContract.Presenter {

    private Region downloadRegion;

    private ProgressContract.View mView;

    private ProgressFinishing finishingCallback;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MESSAGE_PROGRESS)) {

                Download download = intent.getParcelableExtra("download");
                mView.setProgress(download.getProgress());
                if (download.getProgress() == 100) {

                    downloadRegion.setLoadingState(Region.LoadingState.LOADED);
                    finishingCallback.onLoadingFinished();

                    showStorageData();
                } else {

                    String progress = String.format(mView.getActivityContext().getString(R.string.progress_state),
                            download.getCurrentFileSize(), download.getTotalFileSize());

                    mView.setProgressText(progress);
                }
            }
        }
    };

    ProgressPresenter(ProgressContract.View mView, Region downloadRegion) {
        this.mView = mView;
        this.downloadRegion = downloadRegion;

        if (downloadRegion == null) {
            showStorageData();
            return;
        }

        registerReceiver();
        startDownloading(downloadRegion);

        finishingCallback = ((MainActivity) mView.getActivityContext());
    }

    private void startDownloading(Region downloadRegion) {

        Intent intent = new Intent(mView.getActivityContext(), DownloadService.class);
        intent.putExtra(REGION, downloadRegion.getFileName());
        mView.getActivityContext().startService(intent);

        mView.setProgressTitle(R.string.title_downloading);
    }

    private void registerReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(mView.getActivityContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void showStorageData() {
        mView.setProgressTitle(R.string.device_memory);

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

        long bytesTotal = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();

        long megTotal = bytesTotal / 1048576;
        long megAvailable = bytesAvailable / 1048576;

        int progress = (int) (100 - (float) megAvailable / megTotal * 100);

        mView.setProgress(progress);
        mView.setProgressText(String.format(mView.getActivityContext().getString(R.string.stoarage_state), (int) megAvailable));
    }

}
