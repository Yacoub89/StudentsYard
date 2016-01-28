package com.model;

public class University {
	
	     private String uniName;
	     private String facName;
	     private String deptName;
	     private String courseCodes;
	     private String primaryKey;
	     
	     
	     //getters
	     public String getUniName(){
	    	 return uniName;
	    	 
	     }
		 public String getFacName() {
			 return facName;
			    	 
			     }
		 public String getDeptName(){
			 
			 return deptName;
			 
		 }
		 public String getCourseCode(){
			 return courseCodes;
		 }
		 
		 // seeters
		 
		 public void setUniName(String aName){
			 uniName = aName;
			 
		 }
		public void setFacName(String aName){
			facName = aName;
					 
				 }
		public void setDeptName(String aName){
			deptName = aName;
			 
		}
		public void setCourseCode(String aCode){
			courseCodes = aCode;
		}
		public String getPrimaryKey() {
			return primaryKey;
		}
		public void setPrimaryKey(String primaryKey) {
			this.primaryKey = primaryKey;
		}
		


}
