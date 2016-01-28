package com.data.binding;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.studentsyard.R;
import com.studentsyard.R.drawable;
import com.studentsyard.R.id;
import com.studentsyard.R.layout;
import com.fragments.RecenltyAddedRoomFragment;
import com.fragments.MyPostingsRoomFragment;
import com.model.Room;
import com.parse.ParseObject;

public class BinderRoomData extends BaseAdapter {

	LayoutInflater inflater;
	ImageView thumb_image;
	List<ParseObject>  room;
	ViewHolder holder;

	public BinderRoomData(Activity act,List<ParseObject>  roomList) {

		room = roomList;

		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BinderRoomData(RecenltyAddedRoomFragment homeFragment,List<ParseObject>  roomList2) {
		room = roomList2;

		inflater = (LayoutInflater) homeFragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BinderRoomData(MyPostingsRoomFragment roomPostingsFragment,
						  List<ParseObject>  roomList2) {
		room = roomList2;

		inflater = (LayoutInflater) roomPostingsFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return room.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();

			holder.roomTitle = (TextView) vi.findViewById(R.id.bookName); // city
																			// name
			holder.roomPrice = (TextView) vi.findViewById(R.id.bookPrice); // city
																			// weather
																			// overview
			holder.roomLocation = (TextView) vi.findViewById(R.id.bookCond); // city
																				// temperature
			holder.bookImage = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// image

			vi.setTag(holder);
		} else {

			holder = (ViewHolder) vi.getTag();
		}

		holder.roomTitle.setText(room.get(position).getString("TITLE"));
		holder.roomLocation.setText(room.get(position).getString("LOCATION"));
		holder.roomPrice.setText("$" + room.get(position).getString("PRICE"));

		// Setting an image
		// String uri = "drawable/"+
		// weatherDataCollection.get(position).get(KEY_ICON);
		// int imageResource =
		// vi.getContext().getApplicationContext().getResources().getIdentifier(R.drawable.book,
		// null, vi.getContext().getApplicationContext().getPackageName());
		Drawable image = vi.getContext().getResources()
				.getDrawable(R.drawable.book);
		holder.bookImage.setImageDrawable(image);

		return vi;
	}

	static class ViewHolder {

		TextView roomTitle;
		TextView roomPrice;
		TextView roomLocation;
		ImageView bookImage;
	}

}
