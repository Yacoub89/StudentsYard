package com.studentsyard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fragments.AdvertiseBookFragment;
import com.fragments.AdvertiseRoomFragment;
import com.fragments.BrowseBookFragment;
import com.fragments.BrowseRoomFragment;
import com.fragments.MyPostingFragment;
import com.fragments.RecenltyAddedBooksFragment;
import com.fragments.RecenltyAddedRoomFragment;
import com.messages.Messages;
import com.model.Login;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.xml.XmlHandler;

import java.util.ArrayList;

import slidegmenu.model.NavDrawerItem;
import slidingmenu.adapter.NavDrawerListAdapter;



public class MainActivity extends Activity {

	private Menu optionsMenu;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private UserLoginTask mAuthTask = null;
	XmlHandler parser;

	// nav drawer title
	@SuppressWarnings("unused")
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private String email;
	private String token;
	private int num;
	SharedPreferences settings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);




		settings = getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);
		//token = settings.getString("token", null);



		/* slider */
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		if (email.equals("guest")) {
			addDrawers(1);
			num = 1;
		} else {
			addDrawers(2);
			num = 2;
		}
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		displayView(1);

	}

	public void showDialog() {

		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
				.create(); // Read Update
		alertDialog.setTitle("Warning!");
		alertDialog.setMessage("Please connect to Internet.");
		alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		alertDialog.show();
	}

	public void addDrawers(int i) {
		// Guest
		if (i == 1) {
			// adding nav drawer items to array
			// Home
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[0],
					Messages.icons[0]));
			// Home
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[1]));
			// Browse Book
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[2]));
			// Advertise Book
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[3],
					Messages.icons[1]));
			// Advertise Book
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[4]));

			// Browse room
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[5]));
			//
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[6]));

			// Settings
			// navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[7],
			// Messages.icons[2]));
			// Login
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles[7],
					Messages.icons[2]));
		} else if (i == 2) {

			// BOOKS
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[0],
					Messages.icons[0]));
			// Recently Added
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[1]));
			// Browse Book
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[2]));
			// Advertise book
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[3]));
			// ROOMS
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[4],
					Messages.icons[1]));
			// Recently Added
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[5]));
			// browse room
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[6]));
			// Advertise room
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[7]));

			// " "
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[8]));

			// My Postings
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[9],
					Messages.icons[3]));

			// Settings
			// navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[10],
			// Messages.icons[3]));

			// log out
			navDrawerItems.add(new NavDrawerItem(Messages.navMenuTitles2[10],
					Messages.icons[3]));
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			if (isNetworkAvailable()) {

				displayView(position);
			} else {
				showDialog();
			}
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {

		// Guest
		if (num == 1) {
			// update the main content by replacing fragments
			Fragment fragment = null;
			switch (position) {
			case 1:
				fragment = new RecenltyAddedBooksFragment();
				break;
			case 2:
				fragment = new BrowseBookFragment();
				break;
			case 4:
				fragment = new RecenltyAddedRoomFragment();
				break;
			case 5:
				fragment = new BrowseRoomFragment();
				break;
			// case 7:
			// Intent Intent = new Intent(this, SettingsActivity.class);
			// mDrawerLayout.closeDrawer(mDrawerList);
			// startActivityForResult(Intent, 0);
			// break;
			case 7:

				Login();

				break;
			default:
				break;
			}

			if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();

				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(Messages.navMenuTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			// error in creating fragment
			else {
				Log.e("MainActivity", "Error in creating fragment");
			}
		} else {
			// update the main content by replacing fragments
			Fragment fragment = null;
			switch (position) {
			case 1:
				fragment = new RecenltyAddedBooksFragment();
				break;
			case 2:
				fragment = new BrowseBookFragment();
				break;
			case 3:
				fragment = new AdvertiseBookFragment();
				break;
			case 5:
				fragment = new RecenltyAddedRoomFragment();
				break;
			case 6:
				fragment = new BrowseRoomFragment();
				break;
			case 7:
				fragment = new AdvertiseRoomFragment();
				break;
			case 9:
				Intent secondIntent2 = new Intent(this, MyPostingFragment.class);
				mDrawerLayout.closeDrawer(mDrawerList);
				startActivityForResult(secondIntent2, 0);
				break;
			// case 10:
			// Intent Intent = new Intent(this, SettingsActivity.class);
			// mDrawerLayout.closeDrawer(mDrawerList);
			// startActivityForResult(Intent, 0);
			// break;
			case 10:

				logout();
				break;
			default:
				break;
			}

			if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment).commit();

				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(Messages.navMenuTitles2[position]);
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				// error in creating fragment
				Log.e("MainActivity", "Error in creating fragment");
			}
		}
	}

	private void logout() {
		mAuthTask = new UserLoginTask();
		mAuthTask.execute("1");

	}

	private void Login() {
		Intent secondIntent = new Intent(this, LoginActivity.class);
		mDrawerLayout.closeDrawer(mDrawerList);
		startActivityForResult(secondIntent, 0);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	// @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.airport_menuRefresh);
			if (refreshItem != null) {
				if (refreshing) {
					refreshItem
							.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		@SuppressWarnings("unused")
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

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

	//
	public class UserLoginTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		private void setDialog(ProgressDialog dialog) {
			this.dialog = dialog;
		}

		private ProgressDialog getDialog() {
			return dialog;
		}

		@Override
		protected void onPreExecute() {

			setDialog(new ProgressDialog(MainActivity.this));
			getDialog().setProgressStyle(ProgressDialog.STYLE_SPINNER);
			getDialog().setMessage("loggin out.");
			getDialog().setCancelable(false);
			getDialog().show();
		}

		@SuppressLint("SimpleDateFormat")
		protected Boolean doInBackground(String... params) {
			Boolean temp = true;

			try {
				parser = new XmlHandler();

				Login user = parser.getToken("http://" + Messages.EMULATOR
						+ ":8080/studnetYard/logoff?token="
						+ token.replace(" ", "%20"));
				if (user != null) {
					temp = true;
				} else {
					temp = false;
				}

			} catch (Exception e) {
				temp = false;
			}

			return temp;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			dialog.dismiss();

			//if (success) {
				finish2();
			//} else {
			//	Toast.makeText(getApplicationContext(),
			//			"logged out unsuccessfuly", Toast.LENGTH_SHORT).show();
			//}
		}

		public void finish2() {

			Toast.makeText(getApplicationContext(), "logged out",
					Toast.LENGTH_SHORT).show();

			SharedPreferences.Editor editor = settings.edit();
			editor.putString("name", " ");
			editor.putString("email", " ");
			editor.putString("token", " ");
			editor.commit();
            ParseUser.getCurrentUser().logOut();

			Intent secondIntent = new Intent(getApplicationContext(),
					LoginActivity.class);
			secondIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(secondIntent, 0);
			finish();
		}
	}
}
