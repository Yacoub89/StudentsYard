package com.studentsyard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.messages.Messages;

public class Summary extends Activity {

	TextView text;
	String email;
	SharedPreferences settings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		text = (TextView) findViewById(R.id.summary);
		Bundle b = getIntent().getExtras();

		settings = getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);

		int type = settings.getInt("type", 0);

		if (type == 1) {
			text.setText("An E-mail has been sent to " + email
					+ " with the following details:\n" + "University name: "
					+ settings.getString("uniName", null) + "\nFaculty name: "
					+ settings.getString("facName", null)
					+ "\nDepartment Name: "
					+ settings.getString("depName", null) + "\nCourse Code: "
					+ settings.getString("courseC", null) + "\nBook Name: "
					+ settings.getString("bookName", null)
					+ "\nBook Condition: "
					+ settings.getString("bookCond", null) + "\nBook Price: $"
					+ settings.getString("bookPrice", null));
		} else {
			text.setText("An E-mail has been sent to " + email
					+ " with the following details:\n" + "Title: "
					+ settings.getString("rmTitle", null) + "\nPrice: $"
					+ settings.getString("rmPrice", null) + "\nLocation: "
					+ settings.getString("rmLoc", null) + "\nDescription: "
					+ settings.getString("rmDesc", null));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.summary, menu);
		return true;
	}

	public void continue_button(View view) {

		Continue();
	}

	private void Continue() {
		Intent secondIntent = new Intent(this, MainActivity.class);
		secondIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(secondIntent, 0);
		finish();
	}
}
