package com.studentsyard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.data.binding.BinderData;
import com.email.Mail;
import com.messages.Messages;
import com.model.Book;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xml.XmlHandler;

public class Explorer extends Activity {

	private TextView textView;

	ListView listView;
	List<String> list1;
	List<ParseObject> bookList;
	private UserLoginTask mAuthTask = null;
	XmlHandler parser;
	ArrayAdapter<String> adapter;

	//
	String prov;
	String city;
	String uniName;
	String facName;
	String dept;
	String courseC;
	int itemPosition = 0;
	private String globalString = "0";

	String email;
	SharedPreferences settings = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explorer);
		textView = (TextView) findViewById(R.id.summary);

		settings = getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);

		// b = getIntent().getExtras();

		if (email.equalsIgnoreCase("guest")) {
			// email = "guest";
			uniName = settings.getString("Uni", null);
			facName = settings.getString("Facu", null);
			dept = settings.getString("Dep", null);
			courseC = settings.getString("Code", null);

		}

		else {
			// email = b.getString("email");
			uniName = settings.getString("Uni", null);
			facName = settings.getString("Facu", null);
			dept = settings.getString("Dep", null);
			courseC = settings.getString("Code", null);

			//

		}

		bookList = new ArrayList<ParseObject>();
		list1 = new ArrayList<String>();

		if (courseC.equals(" "))
			textView.setText("Results for:\n" + uniName + " -> " + facName
					+ " -> " + dept);
		else if (dept.equals(" "))
			textView.setText("Results for:\n" + uniName + " -> " + facName);
		else if (facName.equals(" "))
			textView.setText("Results for:\n" + uniName);
		else
			textView.setText("Results for:\n" + uniName + " -> " + facName
					+ " -> " + dept + " -> " + courseC);

		// Get ListView object from xml
		listView = (ListView) findViewById(R.id.list);
		// registerForContextMenu(listView);
		// registerForContextMenu(getListView());


		ParseQuery<ParseObject> query = ParseQuery.getQuery("bookList");
		query.whereEqualTo("UNIVERSITY_NAME", uniName);
		query.whereEqualTo("FACULTY_NAME",facName );
        query.whereEqualTo("DEPARTMENT_NAME", dept);
        //query.whereEqualTo("COURSE_CODE",courseC.toLowerCase() );

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> book, ParseException e) {
				if (e == null) {

                    bookList = book;
                    collectBookList();
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(bookList.get(info.position).getString("BOOK_NAME"));
			String[] menuItems = getResources().getStringArray(R.array.menu);
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
					+ bookList.get(info.position).getString("BOOK_CONDITION")+ "\n"
					+ "Book Price: $"
					+ bookList.get(info.position).getString("BOOK_PRICE"));
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent, getResources()
					.getText(R.string.chooser_title)));

			return true;
		}
		return false;
	}

	private void collectBookList() {

		BinderData bindingData = new BinderData(this, bookList);

		listView.setAdapter(bindingData);
		registerForContextMenu(listView);

		if (!email.equals("guest")) {

			// ListView Item Click Listener
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// ListView Clicked item index
					itemPosition = position;

					// ListView Clicked item value
					String itemValue = (String) listView
							.getItemAtPosition(position);

					// call some other methods before that I guess...
					AlertDialog alertDialog = new AlertDialog.Builder(
							Explorer.this).create(); // Read Update
					alertDialog.setTitle(itemValue);
					alertDialog.setMessage("Interested?");
					alertDialog.setButton(Dialog.BUTTON_POSITIVE, "YES",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									mAuthTask = new UserLoginTask();
									mAuthTask
											.execute("2",bookList.get(itemPosition).getString("BOOK_NAME"),email,(String) listView.getItemAtPosition(itemPosition));
								}

							});

					alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									Toast.makeText(getApplicationContext(),
											"NO", Toast.LENGTH_LONG).show();
								}

							});

					alertDialog.show(); // <-- See This!

				}

			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.explorer, menu);
		return true;
	}

	public class UserLoginTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			Boolean temp = false;
			globalString = params[0];

	         if (params[0].equals("2")) {
				Mail m = new Mail("studentsyardautoemail@gmail.com", "Y12a34co");

				String[] toArr = { email };
				m.setTo(toArr);
				m.setFrom("studentsyardautoemail@gmail.com");
				m.setSubject("This is an automated message from Student's Yard regarding your post: "
                        + params[2]);
				m.setBody("A user with this email is interested in your book: "
						+ params[1]);

				try {
					if (m.send()) {

						temp = true;
					}

				} catch (Exception e) {
					Toast.makeText(Explorer.this,
							"There was a problem sending the email.",
							Toast.LENGTH_LONG).show();
					Log.e("MailApp", "Could not send email", e);
					e.printStackTrace();
				}
			}

			return temp;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			// showProgress(false);

			if (success) {
				finish();
			} else {
				;
			}
		}

		public void finish() {

     if (globalString.equals("2")) {

				Toast.makeText(
						getApplicationContext(),
						"Your request has been sent to."
								+ bookList.get(itemPosition).getString("EMAIL"),
						Toast.LENGTH_LONG).show();
			}

		}
	}

}
