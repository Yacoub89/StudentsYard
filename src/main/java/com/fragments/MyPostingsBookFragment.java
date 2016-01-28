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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.data.binding.BinderData;
import com.studentsyard.R;
import com.listeners.DeleteBookListener;
import com.listeners.MyPostingListener;
import com.messages.Messages;
import com.model.Book;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xml.XMLDeleteBook;
import com.xml.XMLMyPosting;
import com.xml.XmlHandler;

public class MyPostingsBookFragment extends Fragment  {

	SharedPreferences settings = null;
	private String email;

	ListView listView;
	List<String> list1;
	List<ParseObject>  bookList;

	// private UserLoginTask mAuthTask = null;
	XmlHandler parser;
	ArrayAdapter<String> adapter;
	private Menu optionsMenu;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.book_posting_fragment,
				container, false);

		settings = getActivity().getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);

		//bookList = new List<ParseObject>();
		listView = (ListView) rootView.findViewById(R.id.bookList);
		list1 = new ArrayList<String>();
		registerForContextMenu(listView);

		if (isNetworkAvailable()) {

			getBooks();
		} else {
			showDialog();
		}
		setHasOptionsMenu(true);
		return rootView;
	}


	public void getBooks(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("bookList");
		query.whereEqualTo("EMAIL", email);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> bookList2, ParseException e) {
				if (e == null) {
					bookList=bookList2;
					addListBook(bookList2);

					// Log.d("score", "Retrieved " + uniList.size() + " scores");
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
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
				getBooks();
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.bookList) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(bookList.get(info.position).getString("BOOK_NAME"));
			String[] menuItems = getResources().getStringArray(
					R.array.posting_menu);
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
			sendIntent.putExtra(Intent.EXTRA_TEXT, "Book Name: "
					+ bookList.get(info.position).getString("BOOK_NAME") + "\n "
					+ "Book Condition: "
					+ bookList.get(info.position).getString("BOOK_CONDITION") + "\n"
					+ "Book Price: $"
					+ bookList.get(info.position).getString("BOOK_PRICE"));
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent, getResources()
					.getText(R.string.chooser_title)));
			return true;
		case 1:

			/*String url = "http://"
					+ Messages.EMULATOR
					+ ":8080/studnetYard/deleteBook?email="
					+ email.replace(" ", "%20")
					+ "&name="
					+ bookList.get(info.position).getString("BOOK_NAME")
					.replace(" ", "%20");
			 new XMLDeleteBook(getActivity()).execute(url);*/

            ParseQuery<ParseObject> query = ParseQuery.getQuery("bookList");
            query.whereEqualTo("EMAIL", email);
            query.whereEqualTo("BOOK_NAME", bookList.get(info.position).getString("BOOK_NAME"));
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> book, ParseException e) {
                    if (e == null) {
                        for (ParseObject delete : book) {
                            delete.deleteInBackground();
                            Toast.makeText(getActivity().getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                            // update list
                            getBooks();
                            listView.invalidateViews();
                        }

                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });



			return true;
		case 2:
			Toast.makeText(getActivity().getApplicationContext(),
					"postings Edited...coming soon!", Toast.LENGTH_LONG).show();

			return true;
		}
		return false;
	}

	private void addListBook(List<ParseObject> bookList2) {
		//
		if (bookList2 != null) {

			BinderData bindingData = new BinderData(this, bookList2);
			// listView.setAdapter(adapter);
			listView.setAdapter(bindingData);
			
		}

	}


}
