package com.listeners;

import java.util.List;

import com.model.Room;

public interface RecentAddedRoomChangedListener {
	public void dataLoaded(List<Room> roomList);
}
