package com.api_github_users.get_data;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.api_github_users.R;
import com.api_github_users.list_ui.User;
import com.api_github_users.list_ui.UsersAdapter;
import com.api_github_users.utils.AppConst;

import java.util.ArrayList;

public class GetDataActivity extends Activity implements GetDataModel.Observer {

    private static final String TAG = "GetDataActivity";
    private static final String TAG_WORKER = "TAG_WORKER";

    private GetDataModel mGetDataModel;

    private ProgressBar startPb;
    private ListView usersLv;
    private LinearLayout errorLl;
    private UsersAdapter adapter;

    public static boolean status = true;

    private ArrayList<User> userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_data_activity);

        initImageLoader();
        init();
        checkDataModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGetDataModel.unregisterObserver(this);

        if (isFinishing()) {
            mGetDataModel.stopGetData();
        }
    }

    @Override
    public void onGetDataStarted(final GetDataModel getDataModel) {
        showProgress(true);
        status = false;
    }

    @Override
    public void onGetDataSucceeded(final GetDataModel getDataModel) {
        updateLv(getDataModel.getUsersGetData());
        status = true;
    }

    @Override
    public void onGetDataFailed(final GetDataModel getDataModel) {
        showProgress(false);
        status = true;
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AppConst.PARCELABLE_ARRAY_LIST, userArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userArrayList.addAll(savedInstanceState.<User>getParcelableArrayList(AppConst.PARCELABLE_ARRAY_LIST));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(540, 960) // device screen dimensions (testing)
                .diskCacheExtraOptions(540, 960, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection(View view) {
        showProgress(isOnline());
    }

    private void defLoadingMode() {
        startPb.setVisibility(View.GONE);
        usersLv.setVisibility(View.VISIBLE);
        errorLl.setVisibility(View.GONE);
    }

    private void showProgress(final boolean show) {
        startPb.setVisibility(show ? View.VISIBLE : View.GONE);
        usersLv.setVisibility(show ? View.VISIBLE : View.GONE);
        errorLl.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    private void init() {
        startPb = (ProgressBar) findViewById(R.id.startPb);
        usersLv = (ListView) findViewById(R.id.usersLv);
        errorLl = (LinearLayout) findViewById(R.id.errorLl);

        adapter = new UsersAdapter(userArrayList, this);
        usersLv.setAdapter(adapter);
    }

    private void checkDataModel() {
        final GetDataWorkerFragment retainedWorkerFragment =
                (GetDataWorkerFragment) getFragmentManager().findFragmentByTag(TAG_WORKER);

        if (retainedWorkerFragment != null) {
            mGetDataModel = retainedWorkerFragment.getDataModel();
        } else {
            final GetDataWorkerFragment workerFragment = new GetDataWorkerFragment();

            getFragmentManager().beginTransaction()
                    .add(workerFragment, TAG_WORKER)
                    .commit();

            mGetDataModel = workerFragment.getDataModel();
            startLoading(0);
        }

        mGetDataModel.registerObserver(this);
    }

    public void updateLv(ArrayList<User> arrayList) {
        if (userArrayList.size() > 0) {
            int lastItem = userArrayList.size() - 1;
            if (userArrayList.get(lastItem).getUserId() == AppConst.PB_ZERO) {
                userArrayList.remove(lastItem);
                userArrayList.addAll(arrayList);
                adapter.notifyDataSetChanged();
                defLoadingMode();
            } else {
                userArrayList.addAll(arrayList);
                adapter.notifyDataSetChanged();
                defLoadingMode();
            }
        } else {
            userArrayList.addAll(arrayList);
            adapter.notifyDataSetChanged();
            defLoadingMode();
        }
    }

    public void startLoading(int sinceValue) {
        if (isOnline()) {
            mGetDataModel.getData(sinceValue);
        } else {
            showProgress(false);
        }
    }
}