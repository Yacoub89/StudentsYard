package com.listeners;

import java.util.List;

import com.model.Book;

public interface RecentAddedBookChangedListener {
	public void dataLoaded(List<Book> bookList);

}
