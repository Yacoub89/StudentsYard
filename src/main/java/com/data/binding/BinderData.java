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
import com.fragments.MyPostingsBookFragment;
import com.fragments.RecenltyAddedBooksFragment;
import com.model.Book;
import com.parse.ParseObject;

public class BinderData extends BaseAdapter {

	LayoutInflater inflater;
	ImageView thumb_image;
	List<ParseObject> book;
	ViewHolder holder;

	public BinderData(Activity act, List<ParseObject> bookList) {

		book = bookList;

		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BinderData(RecenltyAddedBooksFragment homeFragment, List<ParseObject> bookList2) {
		book = bookList2;

		inflater = (LayoutInflater) homeFragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BinderData(MyPostingsBookFragment bookPostingsFragment, List<ParseObject> bookList) {

		book = bookList;

		inflater = (LayoutInflater) bookPostingsFragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return book.size();
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

			holder.bookName = (TextView) vi.findViewById(R.id.bookName); // city
																			// name
			holder.bookPrice = (TextView) vi.findViewById(R.id.bookPrice); // city
																			// weather
																			// overview
			holder.bookCond = (TextView) vi.findViewById(R.id.bookCond); // city
																			// temperature
			holder.bookImage = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// image

			vi.setTag(holder);
		} else {

			holder = (ViewHolder) vi.getTag();
		}

		holder.bookName.setText(book.get(position).getString("BOOK_NAME"));
		holder.bookCond.setText(book.get(position).getString("BOOK_COBDITION"));
		holder.bookPrice.setText("$" + book.get(position).getString("BOOK_PRICE"));

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

		TextView bookName;
		TextView bookPrice;
		TextView bookCond;
		ImageView bookImage;
	}

}
