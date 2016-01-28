package com.listeners;

import com.model.Login;
import com.model.Registration;

public interface IDataChangedListener {
	public void dataLoaded(String text);
	public void dataLoaded(Login login);
	public void dataLoaded(Registration register);
}
