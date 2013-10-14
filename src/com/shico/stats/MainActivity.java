package com.shico.stats;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.shico.stats.adapters.MenuAdapter;
import com.shico.stats.settings.SettingsFragment;

public class MainActivity extends Activity {
	public final static String ARG_MENU_ITEM_IDX = "menu.item.idx";
	public final static String ARG_MENU_CHART_ITEM_IDX = "menu.chart.item.idx";
	public final static String ARG_MENU_CHART_ITEM_NAME = "menu.chart.item.name";
	public final static String ARG_LAST_SELECTED_CHILD_ITEM = "last.selected.child.item";
	public final static String ARG_LAST_SELECTED_CHILD_POS = "last.selected.child.pos";
	
	// Chart ids should correspond to their position inside the charts-list
	public final static int CHANNELS_FRAGMENT_ID = 0;
	public final static int MOVIES_FRAGMENT_ID = 1;
	public final static int PROGRAMS_FRAGMENT_ID = 2;
	public final static int WIDGETS_FRAGMENT_ID = 3;
	
	public final static int SETTINGS_FRAGMENT_ID = 101;
	public final static int ABOUT_FRAGMENT_ID = 102;
	public final static int HELP_FRAGMENT_ID = 103;
	
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mMenuDrawer;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerMenuTitle;
	private CharSequence mTitle;
	private String[] mDrawerMenuItems;
	
	private int lastSelectedGroupPosition;
	private int lastSelectedChildPosition;
	private String lastSelectedChildItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

		mTitle = mDrawerMenuTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuDrawer = (ExpandableListView) findViewById(R.id.menu_drawer);
		mDrawerMenuItems = getResources().getStringArray(R.array.menu_items);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
		mMenuDrawer.setAdapter(new MenuAdapter(this));
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			mMenuDrawer.setIndicatorBounds(25, 45);
		} else {
			mMenuDrawer.setIndicatorBoundsRelative(25, 45);
		}
		MenuItemClickListener menuItemClickListener = new MenuItemClickListener();
		mMenuDrawer.setOnChildClickListener(menuItemClickListener);
		mMenuDrawer.setOnGroupClickListener(menuItemClickListener);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerMenuTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			mMenuDrawer.performItemClick(mMenuDrawer, 3, mMenuDrawer.getItemIdAtPosition(3));
		}else{
			lastSelectedGroupPosition = savedInstanceState.getInt(ARG_MENU_ITEM_IDX);
			lastSelectedChildItem = savedInstanceState.getString(ARG_MENU_CHART_ITEM_NAME);
			lastSelectedChildPosition = savedInstanceState.getInt(ARG_MENU_CHART_ITEM_IDX);
			if(lastSelectedGroupPosition == MenuAdapter.CHARTS_MENU_IDX){
				// Do nothing. The ChartContainerFragment and ChartFragment takes care of restoring the state.
			}else{
//				mMenuDrawer.performItemClick(mMenuDrawer, 
//						lastSelectedGroupPosition, 
//						mMenuDrawer.getItemIdAtPosition(lastSelectedGroupPosition));				
			}
		}
	}

	private ChartContainerFragment chartContainerFragment;
	private void setChartContainerFragment(String chart, int idx){
		chartContainerFragment = new ChartContainerFragment();
		Bundle args = new Bundle();
		args.putString(ARG_MENU_CHART_ITEM_NAME, chart);
		args.putInt(ARG_MENU_CHART_ITEM_IDX, idx);
		chartContainerFragment.setArguments(args);
		FragmentTransaction ft = getFragmentManager().beginTransaction().replace(R.id.container_frame, chartContainerFragment);
		ft.commit();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(ARG_MENU_ITEM_IDX, lastSelectedGroupPosition);
		outState.putString(ARG_MENU_CHART_ITEM_NAME, lastSelectedChildItem);
		outState.putInt(ARG_MENU_CHART_ITEM_IDX, lastSelectedChildPosition);
		
		super.onSaveInstanceState(outState);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/* The click listner for ListView in the navigation drawer */
	private class MenuItemClickListener implements
			ExpandableListView.OnChildClickListener,
			ExpandableListView.OnGroupClickListener {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			String[] chartItems = parent.getContext().getResources()
					.getStringArray(R.array.menu_chart_items);
			String chartName = chartItems[childPosition];

			// update selected item and title, then close the drawer
			mMenuDrawer.setItemChecked(childPosition + groupPosition + 1, true);
			mDrawerLayout.closeDrawer(mMenuDrawer);

			Bundle args = new Bundle();
			args.putInt(ARG_MENU_ITEM_IDX, groupPosition);
			args.putString(ARG_MENU_CHART_ITEM_NAME, chartName);
			setChartContainerFragment(chartName, childPosition);


			lastSelectedGroupPosition = groupPosition;
			lastSelectedChildItem = chartName;
			lastSelectedChildPosition = childPosition;
			setTitle(chartName);
			getActionBar().setIcon(R.drawable.ic_barchart);
			
			return true;
		}

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			switch (groupPosition) {
			case MenuAdapter.CHARTS_MENU_IDX:
				if (mMenuDrawer.isGroupExpanded(groupPosition)) {
					mMenuDrawer.collapseGroup(groupPosition);
				} else {
					mMenuDrawer.expandGroup(groupPosition);
				}
				mMenuDrawer.setItemChecked(groupPosition, true);
				return true;
			case MenuAdapter.SETTINGS_MENU_IDX:
				getFragmentManager().beginTransaction().replace(R.id.container_frame, new SettingsFragment()).commit();
				break;
			case MenuAdapter.HELP_MENU_IDX:
				getFragmentManager().beginTransaction().replace(R.id.container_frame, new HelpFragment()).commit();
				break;
			case MenuAdapter.ABOUT_MENU_IDX:
				getFragmentManager().beginTransaction().replace(R.id.container_frame, new AboutFragment()).commit();
				break;
			default:
				throw new IllegalArgumentException("No such menu item.");
			}
			
			// update selected item and title, then close the drawer
			mMenuDrawer.collapseGroup(MenuAdapter.CHARTS_MENU_IDX);
			mMenuDrawer.setItemChecked(groupPosition, true);
			if (groupPosition != MenuAdapter.CHARTS_MENU_IDX) {
				mDrawerLayout.closeDrawer(mMenuDrawer);
			}

			lastSelectedGroupPosition = groupPosition;
			setTitle(mDrawerMenuItems[groupPosition]);
			
			return true;
		}
	}
	
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		switch(lastSelectedGroupPosition){
		case 1:
			getActionBar().setIcon(R.drawable.ic_settings);
			break;
		case 2:
			getActionBar().setIcon(R.drawable.ic_help);
			break;
		case 3:
			mTitle = mTitle + " "+ getResources().getString(R.string.app_name);
		default:
			getActionBar().setIcon(R.drawable.ic_launcher);
			break;
		}
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}	
	
	
}
