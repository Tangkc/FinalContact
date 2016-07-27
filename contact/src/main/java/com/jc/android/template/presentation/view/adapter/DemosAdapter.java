/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.jc.android.template.presentation.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jc.android.module.contact.R;
import com.jc.android.template.presentation.RowUserBinding;
import com.jc.android.template.presentation.model.DemoModel;

import java.util.Collection;
import java.util.List;


/**
 * Adaptar that manages a collection of {@link DemoModel}.
 */
public class DemosAdapter extends RecyclerView.Adapter<DemosAdapter.UserViewHolder> {

	private RowUserBinding rowUserBinding;

	public interface OnItemClickListener {
		void onUserItemClicked(DemoModel demoModel);
	}

	private List<DemoModel> usersCollection;
	private final LayoutInflater layoutInflater;

	private OnItemClickListener onItemClickListener;

	public DemosAdapter(Context context, Collection<DemoModel> usersCollection) {
		this.validateUsersCollection(usersCollection);
		this.layoutInflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.usersCollection = (List<DemoModel>) usersCollection;
	}

	@Override
	public int getItemCount() {
		return (this.usersCollection != null) ? this.usersCollection.size() : 0;
	}

	@Override
	public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		rowUserBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_demo, parent, false);
		UserViewHolder userViewHolder = new UserViewHolder(rowUserBinding);
		return userViewHolder;
	}

	@Override
	public void onBindViewHolder(UserViewHolder holder, final int position) {
		final DemoModel demoModel = this.usersCollection.get(position);
		holder.textViewTitle.setText(demoModel.getFullName());
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DemosAdapter.this.onItemClickListener != null) {
					DemosAdapter.this.onItemClickListener.onUserItemClicked(demoModel);
				}
			}
		});
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setUsersCollection(Collection<DemoModel> usersCollection) {
		this.validateUsersCollection(usersCollection);
		this.usersCollection = (List<DemoModel>) usersCollection;
		this.notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	private void validateUsersCollection(Collection<DemoModel> usersCollection) {
		if (usersCollection == null) {
			throw new IllegalArgumentException("The list cannot be null");
		}
	}

	static class UserViewHolder extends RecyclerView.ViewHolder {
		TextView textViewTitle;

		public UserViewHolder(RowUserBinding rowUserBinding) {
			super(rowUserBinding.getRoot());
			textViewTitle = rowUserBinding.title;
		}
	}
}
