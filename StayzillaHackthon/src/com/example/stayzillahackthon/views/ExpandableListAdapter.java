package com.example.stayzillahackthon.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stayzillahackthon.R;
import com.example.stayzillahackthon.model.Hotel;
import com.example.stayzillahackthon.model.Room;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	Context mContext;
	LayoutInflater mInflater;
	ArrayList<Hotel> parents = new ArrayList<Hotel>();
	CustomExpandableListview listView;
	ImageLoader mImageLoader;

	public ExpandableListAdapter(Context context,
			CustomExpandableListview customExpandableListView) {
		mContext = context;
		listView = customExpandableListView;
		mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader = ImageLoader.getInstance();
	}

	public void setList(ArrayList<Hotel> list) {
		parents.clear();
		parents.addAll(list);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return parents.get(groupPosition).rooms.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		Room child = parents.get(groupPosition).rooms.get(childPosition);
		ChildViewHolder childHolder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.child_view, null);
			childHolder = new ChildViewHolder();
			childHolder.discount = (TextView) convertView
					.findViewById(R.id.discount_value);
			childHolder.noOfRooms = (TextView) convertView
					.findViewById(R.id.text_NoOfRooms);
			childHolder.rate = (TextView) convertView
					.findViewById(R.id.rate_value);
			childHolder.roomType = (TextView) convertView
					.findViewById(R.id.room_type);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildViewHolder) convertView.getTag();
		}

		setChildView(childHolder, child);
		// convertView.setFocusable(false);
		//
		// // TextView childName = (TextView) convertView
		// // .findViewById(R.id.childName);
		//
		// // childName.setText(child.name);
		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// // TODO show child details
		// }
		// });

		return convertView;
	}

	private void setChildView(ChildViewHolder childHolder, Room child) {

		childHolder.discount.setText(child.rdiscount);
		childHolder.noOfRooms.setText(child.totalnoofrooms);
		childHolder.rate.setText(child.rate9);
		childHolder.roomType.setText(child.rtype);
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = 0;
		if (parents.get(groupPosition).rooms != null)
			size = parents.get(groupPosition).rooms.size();
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parents.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parents.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Hotel parentObjoect = parents.get(groupPosition);

		Log.i("", "Child count "+parentObjoect.rooms.size());
		ParentViewHolder parentHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.expandable_parent_view,
					null);
			parentHolder = new ParentViewHolder();
			parentHolder.address = (TextView) convertView
					.findViewById(R.id.text_address);
			parentHolder.city = (TextView) convertView
					.findViewById(R.id.text_city);
			parentHolder.hotelName = (TextView) convertView
					.findViewById(R.id.text_HotelName);
			parentHolder.rating = (TextView) convertView
					.findViewById(R.id.text_rating);
			parentHolder.hotelImage = (ImageView) convertView
					.findViewById(R.id.hotelImage);

			convertView.setTag(parentHolder);
		} else {
			parentHolder = (ParentViewHolder) convertView.getTag();
		}
//		convertView.setFocusable(false);
//		convertView.setClickable(true);
		setParentView(parentHolder, parentObjoect);
		return convertView;
	}

	private void setParentView(final ParentViewHolder parentHolder,
			final Hotel parentObjoect) {
		parentHolder.address.setText(parentObjoect.address.trim());
		parentHolder.city.setText(parentObjoect.city);
		parentHolder.hotelName.setText(parentObjoect.displayName);
		parentHolder.rating.setText(parentObjoect.starRating);

		if (!TextUtils.isEmpty(parentObjoect.imageURL)) {
			parentHolder.hotelImage.setTag("" + parentObjoect.id);
			parentHolder.hotelImage.setImageResource(R.drawable.hotel);

			mImageLoader.displayImage(parentObjoect.imageURL,
					parentHolder.hotelImage, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							if (((ImageView) view).getTag().equals(
									"" + parentObjoect.id)) {
								((ImageView) view).setImageBitmap(loadedImage);
								// ((ImageView)
								// view).setImageBitmap(loadedImage);
							} else {
								parentHolder.hotelImage
										.setImageResource(R.drawable.hotel);
							}
						}
					});
		} else {
			parentHolder.hotelImage.setImageResource(R.drawable.hotel);
		}
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	static class ChildViewHolder {
		TextView roomType;
		TextView noOfRooms;
		TextView discount;
		TextView rate;
	}

	static class ParentViewHolder {
		TextView hotelName;
		ImageView hotelImage;
		TextView city;
		TextView address;
		TextView rating;
	}

}
