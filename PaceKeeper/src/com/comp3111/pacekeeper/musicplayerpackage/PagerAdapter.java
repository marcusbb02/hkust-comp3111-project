/**
 * 
 */
package com.comp3111.pacekeeper.musicplayerpackage;

import java.util.List;
import android.support.v4.app.ListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * The <code>PagerAdapter</code> serves the fragments when paging.
 */
public class PagerAdapter extends FragmentPagerAdapter {

	private List<ListFragment> fragments;
	/**
	 * @param fm
	 * @param fragments
	 */
	public PagerAdapter(FragmentManager fm, List<ListFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public ListFragment getItem(int position) {
		return this.fragments.get(position);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.fragments.size();
	}
}
