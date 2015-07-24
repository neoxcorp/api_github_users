package com.api_github_users.get_data;

import android.app.Fragment;
import android.os.Bundle;

public class GetDataWorkerFragment extends Fragment {
	private final GetDataModel mGetDataModel;

	public GetDataWorkerFragment() {
		mGetDataModel = new GetDataModel();
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public GetDataModel getDataModel() {
		return mGetDataModel;
	}
}
