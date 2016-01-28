package com.listeners;

import java.util.List;

import com.model.Book;
import com.model.Room;

public interface MyPostingListener {
	//public void dataLoadedRoom(List<Room> roomList);
	public void dataLoadedBook(List<Book> bookList);

}
