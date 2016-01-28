package com.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.email.Mail;
import com.studentsyard.R;
import com.studentsyard.Summary;
import com.messages.Messages;
import com.model.University;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.xml.XmlHandler;

public class AdvertiseBookFragment extends Fragment implements OnClickListener {
	private UserLoginTask mAuthTask = null;
	private String globalString = "0";

	List<University> universitiesList = null;
	List<University> facultyList;

	XmlHandler parser;
	private ProgressDialog dialog;

	private Button advertiseBtn;

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
	private String email;
	private String mBookName;
	private String mBookConditions;
	private String mBookPrice;

	// UI references.
	private EditText mBookNameView;
	private EditText mBookConditionsView;
	private EditText mBookPriceView;

	SharedPreferences settings = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_advertise_abook,
				container, false);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		settings = getActivity().getSharedPreferences(Messages.PREFS_NAME, 0);
		email = settings.getString("email", null);

		advertiseBtn = (Button) rootView.findViewById(R.id.advertise_btn);
		advertiseBtn.setOnClickListener(this);

		spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
		spinner2 = (Spinner) rootView.findViewById(R.id.Spinner2);
		spinner3 = (Spinner) rootView.findViewById(R.id.Spinner3);
		spinner4 = (Spinner) rootView.findViewById(R.id.Spinner01);

		universitiesList = new ArrayList<University>();
		list1 = new ArrayList<String>();
		list2 = new ArrayList<String>();
		list3 = new ArrayList<String>();
		list4 = new ArrayList<String>();

		mBookConditionsView = (EditText) rootView
				.findViewById(R.id.book_cond_text);
		mBookNameView = (EditText) rootView.findViewById(R.id.book_name_text);
		mBookPriceView = (EditText) rootView.findViewById(R.id.book_price_text);

		// addItemsOnSpinner();
		addListenerOnSpinnerItemSelection();

		//mAuthTask = new UserLoginTask();
		//mAuthTask.execute("1");

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

        Collections.sort(list1);

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

		Collections.sort(list2);
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



		//mAuthTask = new UserLoginTask();
		//mAuthTask.execute("2", UName);
	}

	private void fillSpinner3(List<ParseObject> depList) {

		list3.add(" ");
		for (int i = 0; i < depList.size(); i++) {
			//Log.i("adding", universitiesList.get(i).getDeptName());
			list3.add(depList.get(i).getString("DEPARTMENT_NAME"));
		}

		Collections.sort(list3);
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

		//mAuthTask = new UserLoginTask();
		//mAuthTask.execute("3", dName);
	}

	private void fillSpinner4(List<ParseObject> courseList) {

		list4.add(" ");
		for (int i = 0; i < courseList.size(); i++) {
			//Log.i("adding", universitiesList.get(i).getCourseCode());
			list4.add(courseList.get(i).getString("COURSE_NAME"));
		}

		Collections.sort(list4);
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

	// Async
	public class UserLoginTask extends AsyncTask<String, Void, Boolean> {

		private void setDialog(ProgressDialog aDialog) {
			dialog = aDialog;
		}

		private ProgressDialog getDialog() {
			return dialog;
		}

		// @Override
       /* protected void onPreExecute() {

			setDialog(new ProgressDialog(getActivity()));
			getDialog().setProgressStyle(ProgressDialog.STYLE_SPINNER);
			getDialog().setMessage("Advertising Book");
			getDialog().setCancelable(false);

			if (globalString.equals("6")) {
				getDialog().show();
			}

		}*/

		protected Boolean doInBackground(String... params) {
            Boolean temp = false;
            globalString = params[0];


            if (params[0].equals("6")) {
                dialog.dismiss();
                sendEmail();


            }

            return temp;
        }
	}

	public void advertise() {

		if (attemptToAdvertise()) {


            ParseObject book = new ParseObject("bookList");
            book.put("UNIVERSITY_NAME", spinner1.getSelectedItem().toString());
            book.put("FACULTY_NAME", spinner2.getSelectedItem().toString());
            book.put("DEPARTMENT_NAME", spinner3.getSelectedItem().toString());
            book.put("COURSE_CODE", spinner4.getSelectedItem().toString());

            book.put("BOOK_CONDITION", mBookConditionsView.getText().toString());
            book.put("BOOK_PRICE", mBookPriceView.getText().toString());
            book.put("BOOK_NAME", mBookNameView.getText().toString());
            book.put("EMAIL", email);
            book.saveInBackground();
            mAuthTask = new UserLoginTask();
            mAuthTask.execute("6");
            nextPage();

        } else {
            dialog.dismiss();
        }

	}

    public void nextPage()
    {
        Toast.makeText(getActivity(), "Posted Successfully.",
            Toast.LENGTH_SHORT).show();
        Intent secondIntent = new Intent(getActivity(), Summary.class);

        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("type", 1);
        editor.putString("uniName", spinner1.getSelectedItem()
                .toString());
        editor.putString("facName", spinner2.getSelectedItem()
                .toString());
        editor.putString("depName", spinner3.getSelectedItem()
                .toString());
        editor.putString("courseC", spinner4.getSelectedItem()
                .toString());
        editor.putString("bookName", mBookNameView.getText().toString());
        editor.putString("bookCond", mBookConditionsView.getText()
                .toString());
        editor.putString("bookPrice", mBookPriceView.getText()
                .toString());

        editor.commit();
        secondIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(secondIntent, 0);

    }
    public void sendEmail(){

        Mail m = new Mail("studentsyardautoemail@gmail.com", "Y12a34co");
        m.setSubject("Your book: " + mBookNameView.getText().toString());
        m.getPasswordAuthentication();

        String[] toArr = { email };
        m.setTo(toArr);
        m.setFrom("studentsyardautoemail@gmail.com");


        m.setBody("Here is your book's detail:\n" + "University name: "
                + spinner1.getSelectedItem().toString()
                + "\nFaculty name: "
                + spinner2.getSelectedItem().toString()
                + "\nDepartment Name: "
                + spinner3.getSelectedItem().toString()
                + "\nCourse Code: "
                + spinner4.getSelectedItem().toString()
                + "\nBook Name: " + mBookNameView.getText().toString()
                + "\nBook Condition: "
                + mBookConditionsView.getText().toString()
                + "\nBook Price: $"
                + mBookPriceView.getText().toString());


        try {
            if (m.send()) {


                // finish();
            }
            else
            {
                Toast.makeText(getActivity(), "Could not send email",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
//					Toast.makeText(getActivity(),
//							"There was a problem sending the email.",
//							Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
            e.printStackTrace();
        }
    }
	public Boolean attemptToAdvertise() {

		Boolean cond = false;

		if (!mBookNameView.getText().toString().isEmpty()
				&& !mBookPriceView.getText().toString().isEmpty()
				&& !mBookConditionsView.getText().toString().isEmpty()) {

			cond = true;
		}

		// Reset errors.
		mBookConditionsView.setError(null);
		mBookNameView.setError(null);
		mBookPriceView.setError(null);

		// Store values at the time of the login attempt.
		mBookName = mBookNameView.getText().toString();
		mBookPrice = mBookPriceView.getText().toString();
		mBookConditions = mBookConditionsView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mBookName)) {
			mBookNameView.setError(getString(R.string.error_field_required));
			focusView = mBookNameView;
			cancel = true;
			cond = false;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mBookPrice)) {
			mBookPriceView.setError(getString(R.string.error_field_required));
			focusView = mBookPriceView;
			cancel = true;
			cond = false;
		}

		// Check for a valid name.

		if (TextUtils.isEmpty(mBookConditions)) {
			mBookConditionsView
					.setError(getString(R.string.error_field_required));
			focusView = mBookConditionsView;
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

	@Override
	public void onClick(View v) {


        if (v == advertiseBtn) {
            dialog = ProgressDialog.show(getActivity(), "Advertise a book.",
                    "Posting...", true);
			advertise();
		}
	}
}
