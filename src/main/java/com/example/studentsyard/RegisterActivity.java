package com.studentsyard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.listeners.IDataChangedListener;
import com.model.Login;
import com.model.Registration;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.xml.XmlHandler;

import java.util.List;

public class RegisterActivity extends Activity implements
		OnClickListener {

	// public static final String EXTRA_EMAIL =
	// "com.android.authenticatordemo.extra.EMAIL";

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mFName;
	private String mPhoneNum;

    private ProgressDialog dialog;
	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mFNameView;
	private EditText mPhoneNumView;
	@SuppressWarnings("unused")
	private View mLoginFormView;
	@SuppressWarnings("unused")
	private View mLoginStatusView;
	@SuppressWarnings("unused")
	private TextView mLoginStatusMessageView;

	private Button buttonRgister;

	XmlHandler parser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



		//Parse.enableLocalDatastore(this);

		//Parse.initialize(this, "0VmO9KjV0hrEnabdvwyKGDs4pATtfItSmyI885aO", "SOk2GiscBAM9DSD0fOSGO8LB7NikPohrHiHlJMtZ");

		setContentView(R.layout.activity_register);
		//final ActionBar actionBar = getActionBar();
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		//actionBar.setDisplayShowTitleEnabled(false);

		buttonRgister = (Button) findViewById(R.id.register_in_button);
		buttonRgister.setOnClickListener(this);

		// registerList = new ArrayList<Registration>();

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mFNameView = (EditText) findViewById(R.id.First_Name);
		mPhoneNumView = (EditText) findViewById(R.id.phone_num);

		mPasswordView = (EditText) findViewById(R.id.password);

		mLoginFormView = findViewById(R.id.login_form);
	}

	public void onClick(View v) {
		if (v == buttonRgister) {
			attemptRegister();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void attemptRegister() {

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPhoneNumView.setError(null);
		mFNameView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mPhoneNum = mPhoneNumView.getText().toString();
		mFName = mFNameView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@") && !mEmail.contains(".")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		// Check for a valid name.

		if (TextUtils.isEmpty(mFName)) {
			mFNameView.setError(getString(R.string.error_field_required));
			focusView = mFNameView;
			cancel = true;
		}

		// Check for a valid phone number.

		if (TextUtils.isEmpty(mPhoneNum)) {
			mPhoneNumView.setError(getString(R.string.error_field_required));
			focusView = mPhoneNumView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {


			dialog = ProgressDialog.show(this, "Register User",
					"Registering...", true);

            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    ParseUser user = new ParseUser();
                    user.put("email", mEmail);
                    user.put("password", mPassword);

                    user.put("username", mFName.toLowerCase());
                    user.put("phonenumber", mPhoneNum);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(com.parse.ParseException e) {

                            if (e == null) {
                                dialog.dismiss();
                                BackToLogin();

                            } else {
                                if (e.getMessage().contains("username")) {
                                    mFNameView.setError(e.getMessage());
                                    mFNameView.requestFocus();
                                    dialog.dismiss();
                                } else if (e.getMessage().contains("email")) {
                                    mEmailView.setError(e.getMessage());
                                    mEmailView.requestFocus();
                                    dialog.dismiss();
                                } else {
                                    mPhoneNumView.setError(e.getMessage());
                                    mPhoneNumView.requestFocus();
                                    dialog.dismiss();
                                }
                            }
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {


                        }
                    });
                }
            }).start();


		}
	}

	private void BackToLogin() {

		Toast.makeText(RegisterActivity.this, R.string.register_success,
				Toast.LENGTH_SHORT).show();

		Intent secondIntent = new Intent(getApplicationContext(),
				LoginActivity.class);
		secondIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(secondIntent);
		
	}
}
