package com.example.stayzillahackthon.views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;

import com.example.stayzillahackthon.R;
import com.example.stayzillahackthon.model.Hotel;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CustomExpandableListview extends RelativeLayout {

	ExpandableListView expListView;
	ExpandableListAdapter mAdapter;
	ArrayList<Hotel> parents = new ArrayList<Hotel>();
	protected int lastExpandedGroupPosition;
	Context mContext;
	ImageLoader mImageLoader;

	public CustomExpandableListview(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initializeView(context);
	}

	public CustomExpandableListview(Context context) {
		super(context);
		initializeView(context);
	}

	public CustomExpandableListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView(context);
	}

	private void initializeView(Context context) {
		mContext = context;
		expListView = new ExpandableListView(context);
		expListView.setGroupIndicator(null);
		// expListView.setCacheColorHint(android.R.color.transparent);
		expListView.setSelector(android.R.color.transparent);

		// expListView.setChildDivider(mContext.getResources().getDrawable(
		// R.drawable.rating_group_child_no_corner_backgnd));
		// expListView.setDividerHeight(WooqerUtility.getInstance().getDpToPixel(
		// mContext, 1));
		expListView.setChildDivider(mContext.getResources().getDrawable(
				R.drawable.rating_child_divider));
		expListView.setDivider(mContext.getResources().getDrawable(
				R.color.white));
		// expListView.setDivider(null);

		expListView.setDividerHeight(10);

		// expListView.setOnGroupClickListener(new OnGroupClickListener() {
		//
		// @Override
		// public boolean onGroupClick(ExpandableListView parent, View v,
		// int groupPosition, long id) {
		// // Implement this method to scroll to the correct position as
		// // this doesn't
		// // happen automatically if we override onGroupExpand() as above
		// parent.smoothScrollToPosition(groupPosition);
		//
		// // Need default behaviour here otherwise group does not get
		// // expanded/collapsed
		// // on click
		// // if (parent.isGroupExpanded(groupPosition)) {
		// // parent.collapseGroup(groupPosition);
		// // } else {
		// parent.expandGroup(groupPosition);
		//
		// // }
		//
		// return true;
		// }
		// });

		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(
						mContext,
						"Child clicked group pos" + groupPosition
								+ " child pos " + childPosition,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// collapse the old expanded group, if not the same
				// as new group to expand
				// if (groupPosition != lastExpandedGroupPosition) {
				// expListView.collapseGroup(lastExpandedGroupPosition);
				// }
				lastExpandedGroupPosition = groupPosition;
			}
		});

		this.addView(expListView);
	}

	public void setData(ArrayList<Hotel> hotels) {

		parents.addAll(hotels);

		mAdapter = new ExpandableListAdapter(mContext, this);
		mAdapter.setList(parents);
		expListView.setAdapter(mAdapter);

	}

}
