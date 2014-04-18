package com.comp3111.achievement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new STAT_Fragment();
		case 1:
			return new P_ACH_Fragment();
		case 2:
			return new J_ACH_Fragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
