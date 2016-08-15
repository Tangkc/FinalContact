/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.jc.android.contact.presentation.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.module.contact.R;
import com.jc.android.contact.presentation.DemoDetailsBinding;
import com.jc.android.contact.presentation.viewmodel.ContactDetailsViewModel;


/**
 * Fragment that shows details of a certain user.
 */
public class ContactDetailsFragment extends BaseFragment<ContactDetailsViewModel, DemoDetailsBinding> {

	public final static String TAG = ContactDetailsFragment.class.getSimpleName();

	private static final String ARGUMENT_KEY_ID = "org.android10.ARGUMENT_DISPLAYNAME";

	private long id;

	public ContactDetailsFragment() {
		super();

	}

	public static ContactDetailsFragment newInstance(long id) {
		ContactDetailsFragment demoDetailsFragment = new ContactDetailsFragment();

		Bundle argumentsBundle = new Bundle();
		argumentsBundle.putLong(ARGUMENT_KEY_ID, id);
		demoDetailsFragment.setArguments(argumentsBundle);

		return demoDetailsFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		setViewModel(new ContactDetailsViewModel());
		setBinding(DataBindingUtil.<DemoDetailsBinding>inflate(inflater, R.layout.fragment_contact_details, container, false));
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
		this.id = getArguments().getLong(ARGUMENT_KEY_ID);


		getViewModel().loadUserDetailsCommand(id);
	}

	@Override
	public Context getContext() {
		return getActivity().getApplicationContext();
	}
}
