package com.studentsyard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.listeners.IDataChangedListener;
import com.messages.Messages;
import com.model.Login;
import com.model.Registration;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.xml.DownloadXMLAsyncTask;
import com.xml.XmlHandler;

import java.util.List;

public class LoginActivity extends Activity implements
		OnClickListener {

	// Bundle bundle = new Bundle();
	DownloadXMLAsyncTask xmlDowanload;
	private Button buttonSignin;
	private Button btnGuset;

	// private Context context;
    private ProgressDialog dialog;


	// Values for email and password at the time of the login attempt.
	private String mEmail = null;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	@SuppressWarnings("unused")
	private View mLoginFormView;

	XmlHandler parser;

	// shared prefeences

	SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		// final ActionBar actionBar = getActionBar();


        Iconify.with(new FontAwesomeModule());


        //Parse.enableLocalDatastore(getApplicationContext());
        //lDUibF86RImCUrNjo7EpjKfMrikaIRDPBOgwBwfV

       // Parse.initialize( getApplicationContext(), "0VmO9KjV0hrEnabdvwyKGDs4pATtfItSmyI885aO", "SOk2GiscBAM9DSD0fOSGO8LB7NikPohrHiHlJMtZ");

		// pref
		settings = getSharedPreferences(Messages.PREFS_NAME, 0);

		buttonSignin = (Button) findViewById(R.id.sign_in_button);
		buttonSignin.setOnClickListener(this);

		btnGuset = (Button) findViewById(R.id.guest_button);
		btnGuset.setOnClickListener(this);

		mEmailView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);

		mLoginFormView = findViewById(R.id.login_form);
	}

	// Check screen orientation or screen rotate event here
	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	//
	// // Checks the orientation of the screen for landscape and portrait
	// if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	// Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	// } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
	// Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	// }
	// }

	public void onClick(View v) {
		if (v == buttonSignin) {
			attemptLogin();
		} else if (v == btnGuset) {
			
			loginAsGuest();
			
		}
	}

	private void loginAsGuest() {
		Intent secondIntent = new Intent(this, MainActivity.class);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("email", "guest");
		editor.commit();
		secondIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(secondIntent, 0);
		finish();
		
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_Register:
			Intent secondIntent = new Intent(this, RegisterActivity.class);
			startActivityForResult(secondIntent, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

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
        } /*else if (!mEmail.contains("@") && !mEmail.contains(".")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {


            /*dialog = new ProgressDialog(getApplicationContext());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loging in...");
            dialog.setCancelable(false);
            dialog.show();*/

            dialog = ProgressDialog.show(this, "Log in",
                    "Logging in...", true);

            new Thread(new Runnable() {
                @Override
                public void run()
                {
                                ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null) {
                                            Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();

                                            Intent secondIntent = new Intent(getApplicationContext(), MainActivity.class);

                                            // SharedPreferences myPrefs
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putString("name", user.getUsername());
                                            editor.putString("email", user.getEmail());
                                           // editor.putString("token", "");
                                            editor.commit();

                                            // / end of preference
                                            secondIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivityForResult(secondIntent, 0);
                                            finish();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

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
}
