package com.fragments;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.data.binding.BinderData;
import com.studentsyard.R;
import com.listeners.RecentAddedBookChangedListener;
import com.messages.Messages;
import com.model.Book;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xml.XMLRecentAddedBook;
import com.xml.XmlHandler;

public class RecenltyAddedBooksFragment extends Fragment implements
		RecentAddedBookChangedListener {

	private TextView textView;
	private Menu optionsMenu;

	ListView listView;
	List<String> list1;
	List<Book> bookList;
	// private UserLoginTask mAuthTask = null;
	XmlHandler parser;
	ArrayAdapter<String> adapter;

	public RecenltyAddedBooksFragment() {
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		// textView = (TextView) rootView.findViewById(R.id.bookText);
		// textView.setText("Recently added books:\n\n");

		listView = (ListView) rootView.findViewById(R.id.bookList);
		list1 = new ArrayList<String>();
		bookList = new ArrayList<Book>();

		if (isNetworkAvailable()) {

			Calendar calendar = Calendar.getInstance();
			//Timestamp time = new Timestamp(calendar.getTimeInMillis());
			// time.setHours(time.getHours() - 3);
            Date date = calendar.getTime();

			ParseQuery<ParseObject> query = ParseQuery.getQuery("bookList");
			//query.whereEqualTo("createdAt", date);
			query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> bookList2, ParseException e) {
                    if (e == null) {

                        addList(bookList2);

                        // Log.d("score", "Retrieved " + uniList.size() + " scores");
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });

			/*String url = "http://" + Messages.EMULATOR
					+ ":8080/studnetYard/getBook?time=" + S.replace(" ", "%20");
			new XMLRecentAddedBook(this, getActivity()).execute(url);*/
		} else {
			showDialog();
		}
		setHasOptionsMenu(true);
		return rootView;
	}

	public void addList(List<ParseObject> bookList2) {

		if (bookList2 != null) {

			BinderData bindingData = new BinderData(this, bookList2);
			// listView.setAdapter(adapter);
			listView.setAdapter(bindingData);
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

	@SuppressLint("SimpleDateFormat")
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.airport_menuRefresh:
			setRefreshActionButtonState(true);

			if (isNetworkAvailable()) {
				Calendar calendar = Calendar.getInstance();
				//Timestamp time = new Timestamp(calendar.getTimeInMillis());
				// time.setHours(time.getHours() - 3);

//				String S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//						.format(time);

                Date date = calendar.getTime();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("bookList");
                //query.whereEqualTo("createdAt", S);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> bookList2, ParseException e) {
                        if (e == null) {

                            addList(bookList2);

                            // Log.d("score", "Retrieved " + uniList.size() + " scores");
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });
				/*String url = "http://" + Messages.EMULATOR
						+ ":8080/studnetYard/getBook?time="
						+ S.replace(" ", "%20");
				new XMLRecentAddedBook(this, getActivity()).execute(url);*/
			} else {
				showDialog();
			}
			setRefreshActionButtonState(false);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void dataLoaded(List<Book> bookList2) {
		//addList(bookList2);
	}
}
