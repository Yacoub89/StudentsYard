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

import com.listeners.RecentAddedRoomChangedListener;
import com.model.Book;
import com.model.Room;

public class XMLRecentAddedRoom extends AsyncTask<Object, String, Long> {
	private static final int TEN_SECONDS_IN_MILLISECONDS = 10000;
	private Activity activity;
	// private Context context;
	private ProgressDialog dialog;
	private String message;
	private HttpURLConnection connection;
	private boolean hasConnectionErrorOccurred = false;
	private boolean hasParsingErrorOccurred = false;
	// private String entries = "";
	private RecentAddedRoomChangedListener activityCallback;
	private List<Room> roomList;
	private Room room;
	private String text;

	public XMLRecentAddedRoom(RecentAddedRoomChangedListener activityCallback,
			Activity activity) {
		setActivity(activity);
		setActivityCallback(activityCallback);
	}

	@Override
	protected void onPreExecute() {

		roomList = new ArrayList<Room>();
		setDialog(new ProgressDialog(getActivity()));
		getDialog().setProgressStyle(ProgressDialog.STYLE_SPINNER);
		getDialog().setMessage("Retriving Data.");
		getDialog().setCancelable(false);
		getDialog().show();
	}

	protected Long doInBackground(Object... params) {
		// Thread.currentThread().setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND
		// + android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE );
		Long recordsFound = 0l;
		try {
			XmlPullParser receiveData = downloadXmlData(params[0].toString());
			recordsFound = tryParsingXmlData(receiveData);
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

	private Long tryParsingXmlData(XmlPullParser recieveData) {

		if (recieveData != null) {
			try {
				return processReceivedData(recieveData);
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

	private Long processReceivedData(XmlPullParser xmlData)
			throws XmlPullParserException, IOException {

		Long recordsFound = 0l;

		// int eventType = -9999;

		int eventType = xmlData.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tagname = xmlData.getName();
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if (tagname.equalsIgnoreCase("room")) {
					room = new Room();
				}
				break;

			case XmlPullParser.TEXT:
				text = xmlData.getText();
				break;

			case XmlPullParser.END_TAG:
				if (tagname.equalsIgnoreCase("room")) {
					roomList.add(room);
				} else if (tagname.equalsIgnoreCase("rmtitle")) {
					Log.i("Uni END Name is -->", "uniName");
					room.setTitle(text);
				} else if (tagname.equalsIgnoreCase("rmprice")) {
					Log.i("Uni END Name is -->", "password");
					room.setPrice(text);
				} else if (tagname.equalsIgnoreCase("rmlocation")) {
					Log.i("Uni END Name is -->", "password");
					room.setLocation(text);
				} else if (tagname.equalsIgnoreCase("email")) {
					Log.i("Uni END Name is -->", "token");
					room.setEmail(text);
				}
				break;

			default:
				break;
			}
			eventType = xmlData.next();
		}
		getConnection().disconnect();

		return recordsFound;
	}

	protected void onPostExecute(Long result) {
		// This is what is done after all is finished;
		dialog.dismiss();
		if (isHasConnectionErrorOccurred() || isHasParsingErrorOccurred()) {
			if (isHasConnectionErrorOccurred())
				Toast.makeText(getActivity().getApplicationContext(),
						"Connection error occurred: " + getMessage(),
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity().getApplicationContext(),
						"Parsing error occurred: " + getMessage(),
						Toast.LENGTH_LONG).show();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "It worked: " + getEntries(), Toast.LENGTH_LONG).show();
			// getActivityCallback().dataLoaded(getEntries());
			if (!roomList.get(0).getTitle().equals("fail")) {
				getActivityCallback().dataLoaded(roomList);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"No new room recetlly added!", Toast.LENGTH_LONG)
						.show();
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

	// private String getEntries() {
	// return entries;
	// }
	//
	// private void setEntries(String entries) {
	// this.entries = entries;
	// }
	//
	// private void appendEntries(String entry) {
	// entries = entries + entry;
	// }

	private RecentAddedRoomChangedListener getActivityCallback() {
		return activityCallback;
	}

	private void setActivityCallback(
			RecentAddedRoomChangedListener activityCallback2) {
		this.activityCallback = activityCallback2;
	}

}
