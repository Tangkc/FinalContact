/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.jc.android.template.presentation.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.module.contact.R;
import com.jc.android.template.presentation.DemoDetailsBinding;
import com.jc.android.template.presentation.viewmodel.DemoDetailsViewModel;


/**
 * Fragment that shows details of a certain user.
 */
public class DemoDetailsFragment extends BaseFragment<DemoDetailsViewModel, DemoDetailsBinding> {

	public final static String TAG = DemoDetailsFragment.class.getSimpleName();

	private static final String ARGUMENT_KEY_USER_ID = "org.android10.ARGUMENT_USER_ID";

	private int userId;

	public DemoDetailsFragment() {
		super();

	}

	public static DemoDetailsFragment newInstance(int userId) {
		DemoDetailsFragment demoDetailsFragment = new DemoDetailsFragment();

		Bundle argumentsBundle = new Bundle();
		argumentsBundle.putInt(ARGUMENT_KEY_USER_ID, userId);
		demoDetailsFragment.setArguments(argumentsBundle);

		return demoDetailsFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		setViewModel(new DemoDetailsViewModel());
		setBinding(DataBindingUtil.<DemoDetailsBinding>inflate(inflater, R.layout.fragment_demo_details, container, false));
		getBinding().setViewModel(getViewModel());

		return getBinding().getRoot();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.initialize();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initialize() {
		this.userId = getArguments().getInt(ARGUMENT_KEY_USER_ID);

		getViewModel().loadUserDetailsCommand(userId);
	}

	@Override
	public Context getContext() {
		return getActivity().getApplicationContext();
	}
}
