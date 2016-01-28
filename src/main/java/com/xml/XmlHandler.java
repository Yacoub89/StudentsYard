package com.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.messages.Messages;
import com.model.Address;
import com.model.Book;
import com.model.Login;
import com.model.Registration;
import com.model.Room;
import com.model.University;

import android.util.Log;

public class XmlHandler extends DefaultHandler {
	
	
 // static final String KEY_UNIVERSITIES = "Universities"; // parent node
	
  static final String KEY_UNIVERSITY = "University"; // parent node
static final String KEY_UNI_NAME = "UniName";
static final String KEY_FACULTIES = "Faculties";
static final String KEY_FACULTY_NAME = "FacName";
static final String KEY_DEPARTMENTS = "Departments";
static final String KEY_DEPART_NAME = "DeptName";
static final String KEY_COURSE_CODE = "CourseCodes";
static final String KEY_COURSE_CODE_NAME = "Code";
		
// variables
	private Room room;
	private Book book;
	private University uni;
	private Login login;
	private Registration Register;
	 //private String tempVal;
    private University tempUni;
    private String text;
    private Address address;
	
	// Lists
    private List<Address> provList;
	private List<Room> roomList;
	private List<Book> bookList;
	private List<University> uniList;
	private List<Login> loginList;
	private List<Registration> RegisterList;
	private List<University> universities;
	private List<String> universitiesString;
	private List<String> facuString;
	    
	    public XmlHandler() {
	    	provList = new ArrayList<Address>();
	    	roomList = new ArrayList<Room>();
	    	bookList = new ArrayList<Book>();
	    	uniList = new ArrayList<University>();
	    	loginList = new ArrayList<Login>();
	    	RegisterList = new ArrayList<Registration>();
	    	universities = new ArrayList<University>();
	    	universitiesString = new ArrayList<String>();
	    	facuString = new ArrayList<String>();
	    }
	    
	    public List<University> getUniversities() {
	        return universities;
	    }
	    
	    public List<Login> login(String url){
	    	XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 
	           // parser.setInput(new StringReader(url));
	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("login")) {	                    	
	                    	login = new Login();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("login")) {
	                    	loginList.add(login);
	                    } 
	                    else if(tagname.equalsIgnoreCase("email")){
	                    	Log.i("Uni END Name is -->", "email");
	                    	login.setEmail(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("password")){
	                    	Log.i("Uni END Name is -->", "password");
	                    	login.setPassword(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("name")){
	                    	Log.i("Uni END Name is -->", "name");
	                    	login.setName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("phone")){
	                    	Log.i("Uni END Name is -->", "phone");
	                    	login.setPhone(text);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return loginList;
	    }
	    
	    public List<Registration> register(String url){
	    	XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 
	           // parser.setInput(new StringReader(url));
	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("registration")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	Register = new Registration();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("registration")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	RegisterList.add(Register);
	                    } 

	                    else if(tagname.equalsIgnoreCase("name")){
	                    	Log.i("Uni END Name is -->", "name");

	                    	Register.setName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("email")){
	                    	Log.i("Uni END Name is -->", "email");
	                    	Register.setEmail(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("password")){
	                    	Log.i("Uni END Name is -->", "password");
	                    	Register.setPassword(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("phone")){
	                    	Log.i("Uni END Name is -->", "phone");
	                    	Register.setPhone(text);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return RegisterList;
	    }
	    
	    public List<University> parseTest(InputStream is) {
	        XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;
	        try {
	            factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	 
	            parser.setInput(is, null);
	 
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase(KEY_UNIVERSITY)) {	                    	
	                    	//Log.i("tagname start Name is -->", tagname);
	                    	Log.i("Uni start Name is -->", KEY_UNIVERSITY);
	                    	//tempUni = new Universities();
	                    }
	                    else if(tagname.equalsIgnoreCase(KEY_FACULTIES)){
	                    	Log.i("Uni start Name is -->", KEY_FACULTIES);
	                    	//tempUni = new Universities();
	                    }
	                    else if(tagname.equalsIgnoreCase(KEY_DEPARTMENTS)){
	                    	//tempUni = new Universities();
	                    }
	                    else if(tagname.equalsIgnoreCase(KEY_COURSE_CODE)){
	                    	//tempUni = new Universities();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    //Log.i("text is -->", text);
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase(KEY_UNIVERSITY)) {
	                        // add employee object to list
	                    	
//	                    	Log.i("Uni END Name is -->", KEY_UNI_NAME);
//	                    	Log.i("tagname end Name is -->", tagname);
	                    	Log.i("Uni END Name is -->", KEY_UNIVERSITY);
//	                    	tempUni.setUniName(text);
//	                    	universities.add(tempUni);
	                    } 

	                    else if(tagname.equalsIgnoreCase(KEY_UNI_NAME)){
	                    	Log.i("Uni END Name is -->", KEY_UNI_NAME);
	                    	//Log.i("added text is -->", text);
	                    	tempUni = new University();
	                    	tempUni.setUniName(text);
	                    	universities.add(tempUni);
	                    	}
	                    else if(tagname.equalsIgnoreCase(KEY_FACULTY_NAME)){
	                    	Log.i("Uni END Name is -->", KEY_FACULTY_NAME);

	                    	tempUni = new University();
	                    	tempUni.setUniName(text);
	                    	universities.add(tempUni);
	                    	}
	                    else if(tagname.equalsIgnoreCase(KEY_DEPART_NAME)){
	                    	Log.i("Uni END Name is -->", KEY_DEPART_NAME);

	                    	tempUni = new University();
	                    	tempUni.setUniName(text);
	                    	universities.add(tempUni);
	                    	}
	                    else if(tagname.equalsIgnoreCase(KEY_COURSE_CODE_NAME)){
	                    	Log.i("Uni END Name is -->", KEY_COURSE_CODE_NAME);

	                    	tempUni = new University();
	                    	tempUni.setUniName(text);
	                    	universities.add(tempUni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        return universities;
	    }
	    
	    
	    
	    
	    public List<String> parse(InputStream is) {
	        XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;
	        universitiesString.add(" ");
	        try {
	            factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	 
	            parser.setInput(is, null);
	 
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase(KEY_UNIVERSITY)) {
	                        // create a new instance of employee
	                    	
	                    	//Log.i("tagname start Name is -->", tagname);
	                    	Log.i("Uni start Name is -->", KEY_UNIVERSITY);
	                    	tempUni = new University();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    Log.i("text is -->", text);
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase(KEY_UNIVERSITY)) {
	                        // add employee object to list
	                    	
//	                    	Log.i("Uni END Name is -->", KEY_UNI_NAME);
//	                    	Log.i("tagname end Name is -->", tagname);
	                    	Log.i("Uni END Name is -->", KEY_UNIVERSITY);
//	                    	tempUni.setUniName(text);
	                    	universities.add(tempUni);
	                    } 

	                    else if(tagname.equalsIgnoreCase(KEY_UNI_NAME)){
	                    	Log.i("Uni END Name is -->", KEY_UNI_NAME);

	                    	universitiesString.add(text);
	                    }
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        return universitiesString;
	    }
	    
	    public List<String> parseFaculties(InputStream is) {
	        XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;
	        facuString.add(" ");
	        try {
	            factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	 
	            parser.setInput(is, null);
	 
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                	if(tagname.equalsIgnoreCase(KEY_FACULTIES))
	                    {
	                    	Log.i("Uni start Name is -->", KEY_FACULTIES);	
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    Log.i("text is -->", text);
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                	if(tagname.equalsIgnoreCase(KEY_FACULTIES))
	                    {
	                    	
	                    }
	                    else if(tagname.equalsIgnoreCase(KEY_FACULTY_NAME)){
	                    	Log.i("Uni END Name is -->", KEY_FACULTY_NAME);

	                    	facuString.add(text);
	                    }

	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	        } catch (XmlPullParserException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        return facuString;
	    }

		public List<University> getUniList(String url) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("University")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
//	                    	uni = new Universities();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("University")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	//uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("UniName")){
	                    	Log.i("Uni END Name is -->", "UniName");
	                    	uni = new University();
	                    	uni.setUniName(text);
	                    	uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return uniList;
		}

		public  String getUniId(String url) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("University")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	uni = new University();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("University")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("Id")){
	                    	Log.i("Uni END Name is -->", "Id");
	                    	uni = new University();
	                    	uni.setPrimaryKey(text);
	                    	uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return uniList.get(0).getPrimaryKey();
		}

		public List<University> getFacultyList(String url) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	
	        uniList.removeAll(uniList);
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("Faculty")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
//	                    	uni = new Universities();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("Faculty")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	//uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("FacName")){
	                    	Log.i("Uni END Name is -->", "FacName");
	                    	uni = new University();
	                    	uni.setFacName(text);
	                    	uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return uniList;
		}

		public String getFacId(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("Faculty")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	uni = new University();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("Faculty")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("Id")){
	                    	Log.i("Uni END Name is -->", "Id");
	                    	uni = new University();
	                    	uni.setPrimaryKey(text);
	                    	uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return uniList.get(0).getPrimaryKey();
		}

		public List<University> getDepartmentList(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	
	        uniList.removeAll(uniList);
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("Department")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
//	                    	uni = new Universities();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("Department")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	//uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("DeptName")){
	                    	Log.i("Uni END Name is -->", "DeptName");
	                    	uni = new University();
	                    	uni.setDeptName(text);
	                    	uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return uniList;
		}

		public List<University> getCourseList(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	
	        uniList.removeAll(uniList);
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("Courses")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
//	                    	uni = new Universities();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("Courses")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	//uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("course")){
	                    	Log.i("Uni END Name is -->", "course");
	                    	uni = new University();
	                    	uni.setCourseCode(text);
	                    	uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return uniList;
		}

		public String getDeptId(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("Department")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	uni = new University();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("Department")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("Id")){
	                    	Log.i("Uni END Name is -->", "Id");
	                    	uni = new University();
	                    	uni.setPrimaryKey(text);
	                    	uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return uniList.get(0).getPrimaryKey();
		}

		public Book registerBook(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("registration")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	book = new Book();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("registration")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	bookList.add(book);
	                    } 

	                    else if(tagname.equalsIgnoreCase("uniName")){
	                    	Log.i("Uni END Name is -->", "uniName");
	                    	book.setUniversityName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("facName")){
	                    	Log.i("Uni END Name is -->", "facName");
	                    	book.setFacualtyName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("depName")){
	                    	Log.i("Uni END Name is -->", "depName");
	                    	book.setDepartmentName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("courseCode")){
	                    	Log.i("Uni END Name is -->", "courseCode");
	                    	book.setCourseCode(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("bookName")){
	                    	Log.i("Uni END Name is -->", "bookName");
	                    	book.setBookName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("bookCond")){
	                    	Log.i("Uni END Name is -->", "bookCond");
	                    	book.setBookCondition(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("bookPrice")){
	                    	Log.i("Uni END Name is -->", "bookPrice");
	                    	book.setBookPrice(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("email")){
	                    	Log.i("Uni END Name is -->", "email");
	                    	book.setEmail(text);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return book; 
		}

		public List<Book> getBookList(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("book")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	book = new Book();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("book")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	bookList.add(book);
	                    } 

	                    else if(tagname.equalsIgnoreCase("uniName")){
	                    	Log.i("Uni END Name is -->", "uniName");
	                    	book.setUniversityName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("facName")){
	                    	Log.i("Uni END Name is -->", "facName");
	                    	book.setFacualtyName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("depName")){
	                    	Log.i("Uni END Name is -->", "depName");
	                    	book.setDepartmentName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("courseCode")){
	                    	Log.i("Uni END Name is -->", "courseCode");
	                    	book.setCourseCode(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("bookName")){
	                    	Log.i("Uni END Name is -->", "bookName");
	                    	book.setBookName(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("bookCond")){
	                    	Log.i("Uni END Name is -->", "bookCond");
	                    	book.setBookCondition(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("bookPrice")){
	                    	Log.i("Uni END Name is -->", "bookPrice");
	                    	book.setBookPrice(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("email")){
	                    	Log.i("Uni END Name is -->", "email");
	                    	book.setEmail(text);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return bookList; 
		}

		public Login getToken(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	
	        Login user = null;
	        //uniList.removeAll(uniList);
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("login")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	 user  = new Login();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("login")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	//uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("email")){
	                    	Log.i("Uni END Name is -->", "email");
	                    	user.setEmail(text);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return user;
			
		}

		public Room registerRoom(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("registration")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	room = new Room();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("registration")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	roomList.add(room);
	                    } 

	                    else if(tagname.equalsIgnoreCase("rmtitle")){
	                    	//Log.i("Uni END Name is -->", "uniName");
	                    	room.setTitle(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("rmlocation")){
	                    	//Log.i("Uni END Name is -->", "facName");
	                    	
	                    	room.setLocation(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("rmprice")){
	                    	//Log.i("Uni END Name is -->", "depName");
	                    	room.setPrice(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("rmdesc")){
	                    //	Log.i("Uni END Name is -->", "courseCode");
	                    	room.setDescription(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("email")){
	                    	Log.i("Uni END Name is -->", "email");
	                    	room.setEmail(text);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return room; 
		}

		public Address getAddress(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	
	        String[] temp;
	        String[] spaceTemp;
	        
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("result")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	address = new Address();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                	if(tagname.equalsIgnoreCase("formatted_address")){
	                    	//Log.i("Uni END Name is -->", "uniName");
	                    	address.setAddress(text);
	                    	temp = text.split(Messages.COMMA_DELIM);
	                    	spaceTemp = temp[1].split(Messages.SPACE_DELIM);
	                    	address.setCity(temp[0]);
	                    	address.setPostalCode(spaceTemp[2]+spaceTemp[3]);
	                    	address.setProv(spaceTemp[1]);
	                    	
	                    	}	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return address; 
		}

		public List<Address> getProvList(String url) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("province")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
//	                    	uni = new Universities();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("province")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	//uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("provName")){
	                    	Log.i("Uni END Name is -->", "provName");
	                    	address = new Address();
	                    	address.setProv(text);
	                    	provList.add(address);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return provList;
		}

		public String getProvId(String url) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("province")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	address = new Address();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("province")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	provList.add(address);
	                    } 

	                    else if(tagname.equalsIgnoreCase("id")){
	                    	Log.i("Uni END Name is -->", "id");
	                    	//uni = new University();
	                    	address.setPrimaryKey(text);
	                    	//uniList.add(uni);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return provList.get(0).getPrimaryKey();
		}

		public List<Address> getCityList(String url) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(url);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("city")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
//	                    	uni = new Universities();
	                    	provList = new ArrayList<Address>();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("city")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	//uniList.add(uni);
	                    } 

	                    else if(tagname.equalsIgnoreCase("cityName")){
	                    	Log.i("Uni END Name is -->", "cityName");
	                    	address = new Address();
	                    	address.setCity(text);
	                    	provList.add(address);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return provList;
		}

		public List<Room> getRoomList(String string) {
			XmlPullParserFactory factory = null;
	        XmlPullParser parser = null;	    	
	    	try{
	    		
	    		URL url2 = new URL(string);
	    		factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(url2.openStream(), null); 

	            
	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("room")) {	                    	
	                    	//Log.i("Uni start Name is -->", "registration");
	                    	room = new Room();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("room")) {
	                    	//Log.i("Uni END Name is -->", "registration");
	                    	roomList.add(room);
	                    } 

	                    else if(tagname.equalsIgnoreCase("rmTitle")){
	                    	Log.i("Uni END Name is -->", "rmTitle");
	                    	room.setTitle(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("rmPrice")){
	                    	Log.i("Uni END Name is -->", "rmPrice");
	                    	room.setPrice(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("rmLocation")){
	                    	Log.i("Uni END Name is -->", "rmLocation");
	                    	room.setLocation(text);
	                    	}
	                    else if(tagname.equalsIgnoreCase("email")){
	                    	Log.i("Uni END Name is -->", "email");
	                    	room.setEmail(text);
	                    	}
	           
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }
	 
	    		
	    		}catch (XmlPullParserException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	return roomList; 
		}
	    
}
