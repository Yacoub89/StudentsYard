package com.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.studentsyard.R;
import com.studentsyard.RoomExplorer;
import com.messages.Messages;
import com.model.Address;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xml.XmlHandler;

public class BrowseRoomFragment extends Fragment implements OnClickListener {


	private String globalString = "0";
	XmlHandler parser;
	private Spinner spinner1, spinner2;
	List<String> list1;
	List<String> list2;

	// ArrayAdapter<Universities> dataAdaptertemp;
	ArrayAdapter<String> dataAdapter1;
	ArrayAdapter<String> dataAdapter2;
	ArrayAdapter<Address> dataAdaptertemp;
	List<Address> provinceList = null;
	List<Address> cityList;

	String email;
	String prov;
	String city;
	SharedPreferences settings = null;
	Button explore_btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.browse_for_room, container,
				false);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setHomeButtonEnabled(true);

		settings = getActivity().getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);


		explore_btn = (Button) rootView.findViewById(R.id.rm_explore_btn);
		explore_btn.setOnClickListener(this);

		spinner1 = (Spinner) rootView.findViewById(R.id.rm_spinner1);
		spinner2 = (Spinner) rootView.findViewById(R.id.rm_Spinner2);

		list1 = new ArrayList<String>();
		list2 = new ArrayList<String>();

		// addItemsOnSpinner();
		addListenerOnSpinnerItemSelection();

		//mAuthTask = new UserLoginTask();
		//mAuthTask.execute("1");

		ParseQuery<ParseObject> query = ParseQuery.getQuery("ProvList");

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> ProvList, ParseException e) {
				if (e == null) {

					addItemsOnSpinner(ProvList);

					// Log.d("score", "Retrieved " + uniList.size() + " scores");
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});

		return rootView;
	}

	public void addListenerOnSpinnerItemSelection() {

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {

                emptyList2();
                addItemsOnSpinner2(parentView.getItemAtPosition(position)
                        .toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

	}

	private void emptyList2() {
		// universitiesList.removeAll(universitiesList);

		list2.removeAll(list2);
		spinner2.setAdapter(null);
	}

	private void addItemsOnSpinner2(String pName) {

        if (!pName.equals(" ")) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ProvList");
            query.whereEqualTo("PROVINCE", pName);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> ProvList, ParseException e) {
                    if (e == null) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("CityList");
                        query.whereEqualTo("PROVINCE_FK", ProvList.get(0).getString("PROVINCE_ID_PRIMARY_KEY"));
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> cityList, ParseException e) {
                                if (e == null) {
                                    fillSpinner2(cityList);
                                } else {
                                    Log.d("score", "Error: " + e.getMessage());
                                }
                            }
                        });

                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }

	private void addItemsOnSpinner(List<ParseObject> ProvList) {
		list1.add(" ");

        for (int i = 0; i < ProvList.size(); i++) {
            // Log.i("adding", universitiesList.get(i).getUniName());
            list1.add(ProvList.get(i).getString("PROVINCE"));
        }

		dataAdapter1 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, list1);
		dataAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter1);
	}

	private void fillSpinner2(List<ParseObject> cityList) {

		list2.add(" ");
        for (int i = 0; i < cityList.size(); i++) {
            // Log.i("adding", universitiesList.get(i).getUniName());
            list2.add(cityList.get(i).getString("CITY"));
        }
		dataAdapter2 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, list2);
		dataAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter2);

	}

	public void onClick(View v) {
		if (v == explore_btn) {
			explore();
		}
	}

	public void explore() {

		Intent secondIntent = new Intent(getActivity(), RoomExplorer.class);
		// Bundle b = new Bundle();
		SharedPreferences.Editor editor = settings.edit();
		// b.putString("email", email);
		if (spinner1.getSelectedItem().toString() != null)
			editor.putString("prov", spinner1.getSelectedItem().toString());
		else
			editor.putString("prov", " ");

		if (spinner2.getSelectedItem().toString() != null)
			editor.putString("city", spinner2.getSelectedItem().toString());
		else
			editor.putString("city", " ");

		// secondIntent.putExtras(b);
		// secondIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		editor.commit();
		startActivity(secondIntent);
		// getActivity().finish();

	}


}
