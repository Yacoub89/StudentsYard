package com.xml;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.listeners.IDataChangedListener;
import com.model.Login;
import com.model.Registration;

public class DownloadXMLAsyncTask extends AsyncTask<Object, String, Long> {
	private static final int TEN_SECONDS_IN_MILLISECONDS = 10000;
	private Activity activity;
	// private Context context;
	private ProgressDialog dialog;
	private String message;
	private HttpURLConnection connection;
	private boolean hasConnectionErrorOccurred = false;
	private boolean hasParsingErrorOccurred = false;
	// private String entries = "";
	private IDataChangedListener activityCallback;

	private Login login;
	private List<Login> loginList;
	private Registration Register;
	private List<Registration> RegisterList;
	private String text;

	public DownloadXMLAsyncTask(IDataChangedListener activityCallback,
			Activity activity) {
		setActivity(activity);
		setActivityCallback(activityCallback);

	}

	@Override
	protected void onPreExecute() {
		RegisterList = new ArrayList<Registration>();
		loginList = new ArrayList<Login>();
		setDialog(new ProgressDialog(getActivity()));
		getDialog().setProgressStyle(ProgressDialog.STYLE_SPINNER);
		getDialog().setMessage("Loging In.");
		getDialog().setCancelable(false);
		getDialog().show();
	}

	protected Long doInBackground(Object... params) {
		Long recordsFound = 0l;
		try {
			XmlPullParser receiveData = downloadXmlData(params[0].toString());
			recordsFound = tryParsingXmlData(receiveData, params[1].toString());
		} catch (Exception exception) {
			setMessage("Connection Problem " + exception.toString());
			Log.i("esp", exception.getStackTrace().toString());
			setHasConnectionErrorOccurred(true);
		}
		return recordsFound;
	}

	public XmlPullParser downloadXmlData(String url) {

		XmlPullParser temp = null;
		try {

			URL xmlUrl = new URL(url);
			XmlPullParser receivedDataParser = XmlPullParserFactory
					.newInstance().newPullParser();
			setConnection((HttpURLConnection) xmlUrl.openConnection());
			getConnection().setConnectTimeout(TEN_SECONDS_IN_MILLISECONDS);
			getConnection().setReadTimeout(TEN_SECONDS_IN_MILLISECONDS);
			receivedDataParser.setInput(getConnection().getInputStream(), null);
			// Log.i("esp", "got Data");
			temp = receivedDataParser;
		} catch (XmlPullParserException e) {
			setMessage("XmlPullParserException");
			setHasParsingErrorOccurred(true);
		} catch (ConnectException e) {
			setMessage("ConnectException");
			setHasConnectionErrorOccurred(true);
		} catch (IOException e) {
			setMessage("IOException");
			setHasConnectionErrorOccurred(true);
		}
		return temp;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}

	private Long tryParsingXmlData(XmlPullParser recieveData, String string) {

		if (recieveData != null) {
			try {
				return processReceivedData(recieveData, string);
			} catch (XmlPullParserException e) {
				setMessage("XmlPullParserException");
				setHasParsingErrorOccurred(true);
			} catch (IOException e) {
				setMessage("IOException");
				setHasParsingErrorOccurred(true);
			}
		}
		return 0l;
	}

	private Long processReceivedData(XmlPullParser xmlData, String string)
			throws XmlPullParserException, IOException {

		Long recordsFound = 0l;
		if (string.equals("1")) {

			// int eventType = -9999;

			int eventType = xmlData.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = xmlData.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (tagname.equalsIgnoreCase("login")) {
						login = new Login();
					}
					break;

				case XmlPullParser.TEXT:
					text = xmlData.getText();
					break;

				case XmlPullParser.END_TAG:
					if (tagname.equalsIgnoreCase("login")) {
						loginList.add(login);
					} else if (tagname.equalsIgnoreCase("email")) {
						Log.i("Uni END Name is -->", "email");
						login.setEmail(text);
					} else if (tagname.equalsIgnoreCase("password")) {
						Log.i("Uni END Name is -->", "password");
						login.setPassword(text);
					} else if (tagname.equalsIgnoreCase("name")) {
						Log.i("Uni END Name is -->", "name");
						login.setName(text);
					} else if (tagname.equalsIgnoreCase("phone")) {
						Log.i("Uni END Name is -->", "phone");
						login.setPhone(text);
					} else if (tagname.equalsIgnoreCase("token")) {
						Log.i("Uni END Name is -->", "token");
						login.setToken(text);
					}

					break;

				default:
					break;
				}
				eventType = xmlData.next();
			}
			getConnection().disconnect();
			// return recordsFound;
		} else if (string.endsWith("2")) {

			// Long recordsFound = 0l;

			int eventType = xmlData.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = xmlData.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (tagname.equalsIgnoreCase("registration")) {
						// Log.i("Uni start Name is -->", "registration");
						Register = new Registration();
					}
					break;

				case XmlPullParser.TEXT:
					text = xmlData.getText();
					break;

				case XmlPullParser.END_TAG:
					if (tagname.equalsIgnoreCase("registration")) {
						// Log.i("Uni END Name is -->", "registration");
						RegisterList.add(Register);
					}

					else if (tagname.equalsIgnoreCase("name")) {
						Log.i("Uni END Name is -->", "name");

						Register.setName(text);
					} else if (tagname.equalsIgnoreCase("email")) {
						Log.i("Uni END Name is -->", "email");
						Register.setEmail(text);
					} else if (tagname.equalsIgnoreCase("password")) {
						Log.i("Uni END Name is -->", "password");
						Register.setPassword(text);
					} else if (tagname.equalsIgnoreCase("phone")) {
						Log.i("Uni END Name is -->", "phone");
						Register.setPhone(text);
					}

					break;

				default:
					break;
				}
				eventType = xmlData.next();
			}
			getConnection().disconnect();
		}

		return recordsFound;
	}

	protected void onPostExecute(Long result) {
		// This is what is done after all is finished;
		dialog.dismiss();
		if (isHasConnectionErrorOccurred() || isHasParsingErrorOccurred()) {
			if (isHasConnectionErrorOccurred())
				Toast.makeText(
						getActivity().getApplicationContext(),
						"Connection error occurred: Problem Connecting to Server.",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity().getApplicationContext(),
						"Parsing error occurred: " + getMessage(),
						Toast.LENGTH_LONG).show();
		} else {
			if (loginList.size() != 0) {
				getActivityCallback().dataLoaded(loginList.get(0));
			} else if (RegisterList.size() != 0) {
				getActivityCallback().dataLoaded(RegisterList.get(0));
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"No such User exists", Toast.LENGTH_LONG).show();
			}

		}
	}

	private Activity getActivity() {
		return activity;
	}

	private void setActivity(Activity activity) {
		this.activity = activity;
	}

	private ProgressDialog getDialog() {
		return dialog;
	}

	private void setDialog(ProgressDialog dialog) {
		this.dialog = dialog;
	}

	private String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}

	private HttpURLConnection getConnection() {
		return connection;
	}

	private void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}

	private boolean isHasConnectionErrorOccurred() {
		return hasConnectionErrorOccurred;
	}

	private void setHasConnectionErrorOccurred(
			boolean hasConnectionErrorOccurred) {
		this.hasConnectionErrorOccurred = hasConnectionErrorOccurred;
	}

	private boolean isHasParsingErrorOccurred() {
		return hasParsingErrorOccurred;
	}

	private void setHasParsingErrorOccurred(boolean hasParsingErrorOccurred) {
		this.hasParsingErrorOccurred = hasParsingErrorOccurred;
	}

	private IDataChangedListener getActivityCallback() {
		return activityCallback;
	}

	private void setActivityCallback(IDataChangedListener activityCallback) {
		this.activityCallback = activityCallback;
	}

}
