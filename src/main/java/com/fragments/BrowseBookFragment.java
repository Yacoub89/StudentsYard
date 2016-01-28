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

import com.studentsyard.Explorer;
import com.studentsyard.R;
import com.messages.Messages;
import com.model.University;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xml.XmlHandler;

public class BrowseBookFragment extends Fragment implements OnClickListener {

	//private UserLoginTask mAuthTask = null;
	private String globalString = "0";
	XmlHandler parser;

	List<University> universitiesList = null;
	List<University> facultyList;

	private Spinner spinner1, spinner2, spinner3, spinner4;
	List<String> list1;
	List<String> list2;
	List<String> list3;
	List<String> list4;
	// ArrayAdapter<Universities> dataAdaptertemp;
	ArrayAdapter<String> dataAdapter1;
	ArrayAdapter<String> dataAdapter2;
	ArrayAdapter<String> dataAdapter3;
	ArrayAdapter<String> dataAdapter4;
	String email;
	String prov;
	String city;
	String uniName;
	String facName;
	String dept;
	String courseC;

	Button explore_btn;

	SharedPreferences settings = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_looking_for_abook,
				container, false);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setHomeButtonEnabled(true);

		settings = getActivity().getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);


		explore_btn = (Button) rootView.findViewById(R.id.rm_advertise_btn);
		explore_btn.setOnClickListener(this);

		spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
		spinner2 = (Spinner) rootView.findViewById(R.id.Spinner2);
		spinner3 = (Spinner) rootView.findViewById(R.id.Spinner3);
		spinner4 = (Spinner) rootView.findViewById(R.id.Spinner04);

		universitiesList = new ArrayList<University>();
		list1 = new ArrayList<String>();
		list2 = new ArrayList<String>();
		list3 = new ArrayList<String>();
		list4 = new ArrayList<String>();

		// addItemsOnSpinner();
		addListenerOnSpinnerItemSelection();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UniversityList");
		//query.whereEqualTo("playerName", "Dan Stemkoski");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> uniList, ParseException e) {
				if (e == null) {

					addItemsOnSpinner(uniList);

					// Log.d("score", "Retrieved " + uniList.size() + " scores");
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});

		return rootView;
	}

	public void onClick(View v) {
		if (v == explore_btn) {
			explore();
		}
	}

	public void explore() {

		Intent secondIntent = new Intent(getActivity(), Explorer.class);
		SharedPreferences.Editor editor = settings.edit();
		if (spinner1.getSelectedItem().toString() != null)
			editor.putString("Uni", spinner1.getSelectedItem().toString());
		else
			editor.putString("Uni", " ");
		if (spinner2.getSelectedItem().toString() != null)
			editor.putString("Facu", spinner2.getSelectedItem().toString());
		else
			editor.putString("Facu", " ");
		if (spinner3.getSelectedItem().toString() != null)
			editor.putString("Dep", spinner3.getSelectedItem().toString());
		else
			editor.putString("Facu", " ");
		if (spinner4.getSelectedItem().toString() != null)
			editor.putString("Code", spinner4.getSelectedItem().toString());
		else
			editor.putString("Code", " ");

		// secondIntent.putExtras(b);
		editor.commit();
		startActivity(secondIntent);

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

			// your code here
		});

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				emptyList3();
				addItemsOnSpinner3(parentView.getItemAtPosition(position)
						.toString());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

			// your code here
		});

		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {

				emptyList4();
				addItemsOnSpinner4(parentView.getItemAtPosition(position)
						.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

			// your code here
		});
	}

	private void emptyList2() {
		universitiesList.removeAll(universitiesList);

		list2.removeAll(list2);
		spinner2.setAdapter(null);
	}

	private void emptyList3() {
		universitiesList.removeAll(universitiesList);
		list3.removeAll(list3);
		spinner3.setAdapter(null);
	}

	private void emptyList4() {
		universitiesList.removeAll(universitiesList);
		list4.removeAll(list4);
		spinner4.setAdapter(null);
	}

	private void addItemsOnSpinner(List<ParseObject> uniList) {
		list1.add(" ");


		for (int i = 0; i < uniList.size(); i++) {
			// Log.i("adding", universitiesList.get(i).getUniName());
			list1.add(uniList.get(i).getString("UNIVERSITY_NAME"));
		}

		dataAdapter1 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, list1);
		dataAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter1);
	}

	private void fillSpinner2(List<ParseObject> facList) {

		list2.add(" ");
        for (int i = 0; i < facList.size(); i++) {
            //Log.i("adding", facList.get(i).getFacName());
            list2.add(facList.get(i).getString("FACULTY_NAME"));
        }

		dataAdapter2 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, list2);
		dataAdapter2
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter2);

	}

	private void addItemsOnSpinner2(String UName) {

        if(!UName.equals(" ")){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UniversityList");
            query.whereEqualTo("UNIVERSITY_NAME", UName);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> uniList, ParseException e) {
                    if (e == null) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("FacultyList");
                        query.whereEqualTo("UNIVERSITY_NAME_FK", uniList.get(0).getString("UNIVERSITY_ID_PRIMARY_KEY"));
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> facList, ParseException e) {
                                if (e == null) {
                                    fillSpinner2(facList);
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

	private void fillSpinner3(List<ParseObject> depList) {

		list3.add(" ");
        for (int i = 0; i < depList.size(); i++) {
            //Log.i("adding", universitiesList.get(i).getDeptName());
            list3.add(depList.get(i).getString("DEPARTMENT_NAME"));
        }

		dataAdapter3 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, list3);
		dataAdapter3
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(dataAdapter3);

	}

	private void addItemsOnSpinner3(String dName) {

        if(!dName.equals(" ")){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("FacultyList");
            query.whereEqualTo("FACULTY_NAME", dName);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> facList, ParseException e) {
                    if (e == null) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("DepartmentList");
                        query.whereEqualTo("FACULTY_NAME_FK", facList.get(0).getString("FACULTY_ID_PRIMARY_KEY"));
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> depList, ParseException e) {
                                if (e == null) {
                                    fillSpinner3(depList);
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

	private void fillSpinner4(List<ParseObject> courseList) {

		list4.add(" ");
        for (int i = 0; i < courseList.size(); i++) {
            //Log.i("adding", universitiesList.get(i).getCourseCode());
            list4.add(courseList.get(i).getString("COURSE_NAME"));
        }

		dataAdapter4 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, list4);
		dataAdapter4
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner4.setAdapter(dataAdapter4);

	}

	private void addItemsOnSpinner4(String dName) {
        if(!dName.equals(" ")){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DepartmentList");
            query.whereEqualTo("DEPARTMENT_NAME", dName);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> depList, ParseException e) {
                    if (e == null) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("CourseList");
                        query.whereEqualTo("DEPARTMENT_FK", depList.get(0).getString("DEPARTMENT_ID_PRIMARY_KEY"));
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> courseList, ParseException e) {
                                if (e == null) {
                                    fillSpinner4(courseList);
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
}
