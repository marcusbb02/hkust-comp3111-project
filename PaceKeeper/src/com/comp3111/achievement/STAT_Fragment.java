package com.comp3111.achievement;

import android.os.Bundle;
import com.comp3111.pacekeeper.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class STAT_Fragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_stat, container, false);
		
		return rootView;
	}
}