package com.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.email.Mail;
import com.studentsyard.R;
import com.studentsyard.Summary;
import com.messages.Messages;
import com.model.Address;
import com.model.Room;
import com.parse.ParseObject;
import com.xml.XmlHandler;

public class AdvertiseRoomFragment extends Fragment implements OnClickListener {

	XmlHandler parser;
	private ProgressDialog dialog;
	private String globalString = "0";

	private Button advertiseBtn;
	private UserLoginTask mAuthTask = null;

	private String mRoomTitle;
	private String mRoomLocation;
	private String mRoomPrice;
	private String mRoomDesc;

	// UI references.
	private EditText mRoomTitleView;
	private EditText mRoomLocationView;
	private EditText mRoomPriceView;
	private EditText mRoomDescView;

	String email;
	SharedPreferences settings = null;
	Address address;

	public AdvertiseRoomFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_register_room,
				container, false);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		settings = getActivity().getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);

		advertiseBtn = (Button) rootView.findViewById(R.id.rm_advertise_btn);
		advertiseBtn.setOnClickListener(this);

		// Set up the login form.
		mRoomTitleView = (EditText) rootView.findViewById(R.id.adv_title);
		mRoomLocationView = (EditText) rootView.findViewById(R.id.adv_location);
		mRoomPriceView = (EditText) rootView.findViewById(R.id.adv_price);
		mRoomDescView = (EditText) rootView.findViewById(R.id.adv_desc);

		return rootView;
	}

	public void onClick(View v) {
		if (v == advertiseBtn) {
			if (attemptToAdvertise()) {

				mAuthTask = new UserLoginTask();
				mAuthTask.execute("1");
			}
		}
	}

	public Boolean attemptToAdvertise() {

		Boolean cond = false;

		if (!mRoomTitleView.getText().toString().isEmpty()
				&& !mRoomLocationView.getText().toString().isEmpty()
				&& !mRoomPriceView.getText().toString().isEmpty()
				&& !mRoomDescView.getText().toString().isEmpty()) {
			cond = true;
		}

		// Reset errors.
		mRoomTitleView.setError(null);
		mRoomLocationView.setError(null);
		mRoomPriceView.setError(null);
		mRoomDescView.setError(null);

		// Store values at the time of the login attempt.
		mRoomTitle = mRoomTitleView.getText().toString();
		mRoomLocation = mRoomLocationView.getText().toString();
		mRoomPrice = mRoomPriceView.getText().toString();
		mRoomDesc = mRoomDescView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mRoomTitle)) {
			mRoomTitleView.setError(getString(R.string.error_field_required));
			focusView = mRoomTitleView;
			cancel = true;
			cond = false;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mRoomLocation)) {
			mRoomLocationView
					.setError(getString(R.string.error_field_required));
			focusView = mRoomLocationView;
			cancel = true;
			cond = false;
		}

		// Check for a valid name.

		if (TextUtils.isEmpty(mRoomPrice)) {
			mRoomPriceView.setError(getString(R.string.error_field_required));
			focusView = mRoomPriceView;
			cancel = true;
			cond = false;
		}

		if (TextUtils.isEmpty(mRoomDesc)) {
			mRoomDescView.setError(getString(R.string.error_field_required));
			focusView = mRoomDescView;
			cancel = true;
			cond = false;
		}

		if (cancel) {
			// There was an error; don't attempt to Register and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// mLoginStatusMessageView.setText(R.string.register_progress_signing_in);
		}

		return cond;
	}

	public class UserLoginTask extends AsyncTask<String, Void, Boolean> {
		private void setDialog(ProgressDialog aDialog) {
			dialog = aDialog;
		}

		private ProgressDialog getDialog() {
			return dialog;
		}

		// @Override
		protected void onPreExecute() {

			setDialog(new ProgressDialog(getActivity()));
			getDialog().setProgressStyle(ProgressDialog.STYLE_SPINNER);
			getDialog().setMessage("Advertising Room");
			getDialog().setCancelable(false);

			getDialog().show();

		}

		protected Boolean doInBackground(String... params) {
			Boolean temp = true;
			globalString = params[0];

			if (params[0].equals("1")) {
				try {
					parser = new XmlHandler();

					// have to finish
					address = parser
							.getAddress("http://maps.googleapis.com/maps/api/geocode/xml?address="
									+ mRoomLocationView.getText().toString()
											.replace(" ", "%20")
									+ "&sensor=false");

					if (address != null) {
						// sendEmail();
						temp = true;
					} else {
						temp = false;
					}

				} catch (Exception e) {
					temp = false;
				}

			} else {

					sendEmail();
			}

			return temp;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			dialog.dismiss();

			if (success) {
				finish();
			} else {
				Toast.makeText(
						getActivity(),
						"Failed to Post room."
								+ "\nE-mail and title name already exist.",
						Toast.LENGTH_SHORT).show();
			}
		}

		public void finish() {
			if (globalString.equals("1")) {


                registerRoom();
			} else
				changeActivity();
		}
	}

    public void registerRoom(){

        ParseObject book = new ParseObject("rooms");
        book.put("DESCRIPTION", mRoomDescView.getText().toString());
        book.put("EMAIL", email);
        book.put("TITLE", mRoomTitleView.getText().toString());

        book.put("LOCATION", address.getAddress());
        book.put("CITY",address.getCity());
        book.put("PROVINCE", address.getProv());
        book.put("PRICE", mRoomPriceView.getText().toString());
        book.saveInBackground();

        	mAuthTask = new UserLoginTask();
        	mAuthTask.execute("2");
    }

	public void sendEmail() {
		Mail m = new Mail("studentsyardautoemail@gmail.com", "Y12a34co");
        m.getPasswordAuthentication();
		String[] toArr = { email };
		m.setTo(toArr);
		m.setFrom("studentsyardautoemail@gmail.com");
		m.setSubject("Title: " + mRoomTitleView.getText().toString());
		m.setBody("Here is your Advertisement's details:\n" + "Title: "
                + mRoomTitleView.getText().toString() + "\nPrice: "
                + mRoomPriceView.getText().toString() + "\nLocation: "
                + address.getAddress() + "\nDescription: "
                + mRoomDescView.getText().toString());
		//
		try {
			if (m.send()) {

				// Toast.makeText(getActivity(), "E-mail was sent.",
				// Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(),
					"There was a problem sending the email.", Toast.LENGTH_LONG)
					.show();
			Log.e("MailApp", "Could not send email", e);
			e.printStackTrace();
		}
	}

	public void changeActivity() {
		Toast.makeText(getActivity(), "Posted Successfully.",
				Toast.LENGTH_SHORT).show();
		Intent secondIntent = new Intent(getActivity(), Summary.class);

		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("type", 2);
		editor.putString("rmTitle", mRoomTitleView.getText().toString());
		editor.putString("rmLoc", address.getAddress());
		editor.putString("rmPrice", mRoomPriceView.getText().toString());
		editor.putString("rmDesc", mRoomDescView.getText().toString());
		editor.commit();

		secondIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(secondIntent, 0);
		// finish();
	}
}
