package com.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.data.binding.BinderRoomData;
import com.studentsyard.R;
import com.listeners.MyPostingListenerRoom;
import com.messages.Messages;
import com.model.Room;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xml.XMLMyPostingRoom;
import com.xml.XmlHandler;

public class MyPostingsRoomFragment extends Fragment {

	SharedPreferences settings = null;
	private String email;

	ListView listView;
	List<String> list1;

	// private UserLoginTask mAuthTask = null;
	XmlHandler parser;
	ArrayAdapter<String> adapter;
	private Menu optionsMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.room_posting_fragment,
				container, false);

		settings = getActivity().getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);

		listView = (ListView) rootView.findViewById(R.id.roomList);
		list1 = new ArrayList<String>();

		if (isNetworkAvailable()) {

			ParseQuery<ParseObject> query = ParseQuery.getQuery("rooms");
			query.whereEqualTo("EMAIL", email);
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> bookList2, ParseException e) {
					if (e == null) {
						//bookList=bookList2;
						addListRoom(bookList2);
						//addListBook(bookList2);

						// Log.d("score", "Retrieved " + uniList.size() + " scores");
					} else {
						Log.d("score", "Error: " + e.getMessage());
					}
				}
			});
		} else {
			showDialog();
		}
		setHasOptionsMenu(true);
		return rootView;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
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

	 @Override
	 public void onCreateContextMenu(ContextMenu menu, View
	 v,ContextMenu.ContextMenuInfo menuInfo) {
	 super.onCreateContextMenu(menu, v, menuInfo);

	 if (v.getId() == R.id.roomList) {
	 AdapterView.AdapterContextMenuInfo info =
	 (AdapterView.AdapterContextMenuInfo) menuInfo;
	 //menu.setHeaderTitle(roomList.get(info.position).getBookName());
	 String[] menuItems = getResources().getStringArray(R.array.posting_menu);
	 for (int i = 0; i < menuItems.length; i++) {
	 menu.add(Menu.NONE, i, i, menuItems[i]);
	 }
	 }

	 }

	 @Override
	 public boolean onContextItemSelected(MenuItem item) {
	 AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	 .getMenuInfo();

	 switch (item.getItemId()) {
	 case 0:
	 Intent sendIntent = new Intent();
	 sendIntent.setAction(Intent.ACTION_SEND);
	 // sendIntent.putExtra(Intent.EXTRA_TEXT, "Book Name: "
	 // + bookList.get(info.position).getBookName() + "\n "
	 // + "Book Condition: "
	 // + bookList.get(info.position).getBookCondition() + "\n"
	 // + "Book Price: $"
	 // + bookList.get(info.position).getBookPrice());
	 sendIntent.setType("text/plain");
	 startActivity(Intent.createChooser(sendIntent, getResources()
	 .getText(R.string.chooser_title)));

	 return true;
	 }
	 return false;
	 }


	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		this.optionsMenu = menu;

		inflater.inflate(R.menu.airport_menu, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.airport_menuRefresh:
			setRefreshActionButtonState(true);

			if (isNetworkAvailable()) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("rooms");
				query.whereEqualTo("EMAIL", email);
				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> bookList2, ParseException e) {
						if (e == null) {
							//bookList=bookList2;
							addListRoom(bookList2);
							//addListBook(bookList2);

							// Log.d("score", "Retrieved " + uniList.size() + " scores");
						} else {
							Log.d("score", "Error: " + e.getMessage());
						}
					}
				});
			} else {
				showDialog();
			}
			setRefreshActionButtonState(false);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showDialog() {

		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
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

	private void addListRoom(List<ParseObject>  roomList) {

		if (roomList != null) {

			BinderRoomData bindingData = new BinderRoomData(this, roomList);
			// listView.setAdapter(adapter);
			listView.setAdapter(bindingData);
		}

	}


}
