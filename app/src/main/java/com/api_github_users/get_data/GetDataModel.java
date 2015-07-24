package com.api_github_users.get_data;

import android.database.Observable;
import android.os.AsyncTask;
import android.util.Log;
import com.api_github_users.communicator.BackendCommunicator;
import com.api_github_users.communicator.CommunicatorFactory;
import com.api_github_users.list_ui.User;

import java.util.ArrayList;

public class GetDataModel {

	private static final String TAG = "GetDataModel";

	private ArrayList<User> usersGetData;

	private final GetDataObservable mObservable = new GetDataObservable();
	private GetDataTask mGetDataTask;
	private boolean mIsWorking;

	public GetDataModel() {
		Log.i(TAG, "new Instance");
	}

	public void getData(final int sinceId) {
		if (mIsWorking) {
			return;
		}

		usersGetData = new ArrayList<>();

		mObservable.notifyStarted();

		mIsWorking = true;
		mGetDataTask = new GetDataTask(sinceId);
		mGetDataTask.execute();
	}

	public void stopGetData() {
		if (mIsWorking) {
			mGetDataTask.cancel(true);
			mIsWorking = false;
			usersGetData = null;
		}
	}

	public void registerObserver(final Observer observer) {
		mObservable.registerObserver(observer);
		if (mIsWorking) {
			observer.onGetDataStarted(this);
		}
	}

	public void unregisterObserver(final Observer observer) {
		mObservable.unregisterObserver(observer);
	}

    public ArrayList<User> getUsersGetData() {
        return usersGetData;
    }

    public class GetDataTask extends AsyncTask<Void, Void, ArrayList<User>> {

		private int sinceId;

		public GetDataTask(int sinceId) {
			this.sinceId = sinceId;
		}

		@Override
		protected ArrayList<User> doInBackground(Void... params) {
			final BackendCommunicator communicator = CommunicatorFactory.createBackendCommunicator();
			try {
				return communicator.postGetData(sinceId);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<User> users) {
			super.onPostExecute(users);
			mIsWorking = false;

			if (users != null) {
                usersGetData.addAll(users);
				mObservable.notifySucceeded();
			} else {
				mObservable.notifyFailed();
			}
		}
	}

	public interface Observer {
		void onGetDataStarted(GetDataModel getDataModel);

		void onGetDataSucceeded(GetDataModel getDataModel);

		void onGetDataFailed(GetDataModel getDataModel);
	}

	private class GetDataObservable extends Observable<Observer> {
		public void notifyStarted() {
			for (final Observer observer : mObservers) {
				observer.onGetDataStarted(GetDataModel.this);
			}
		}

		public void notifySucceeded() {
			for (final Observer observer : mObservers) {
				observer.onGetDataSucceeded(GetDataModel.this);
			}
		}

		public void notifyFailed() {
			for (final Observer observer : mObservers) {
				observer.onGetDataFailed(GetDataModel.this);
			}
		}
	}
}