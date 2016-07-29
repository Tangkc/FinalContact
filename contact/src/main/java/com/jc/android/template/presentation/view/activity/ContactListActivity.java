/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.jc.android.template.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Window;
import android.widget.SectionIndexer;

import com.jc.android.base.presentation.view.activity.BaseActivity;
import com.jc.android.module.contact.R;


/**
 * Activity that shows a list of Users.
 */
public class ContactListActivity extends BaseActivity implements SectionIndexer {

	public static Intent getCallingIntent(Context context) {
		return new Intent(context, ContactListActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);

		DataBindingUtil.setContentView(this, R.layout.contact_list_activity);
	}

	@Override
	public Object[] getSections() {
		return new Object[0];
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}
}
