/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.jc.android.contact.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jc.android.base.presentation.view.activity.BaseActivity;
import com.jc.android.module.contact.R;
import com.jc.android.contact.presentation.view.fragment.ContactDetailsFragment;

/**
 * Activity that shows details of a certain user.
 */
public class ContactDetailsActivity extends BaseActivity {
    public static final String INTENT_EXTRA_PARAM_DISPLAYNAME = "org.android10.INTENT_PARAM_DISPLAYNAME";
    public static final String INTENT_EXTRA_PARAM_MOBILE = "org.android10.INTENT_PARAM_MOBILE";
    public static final String INTENT_EXTRA_PARAM_PHOTO = "org.android10.INTENT_PARAM_PHOTO";

    private String displayName;
    private String mobile;
    private String photo;

    public static Intent getCallingIntent(Context context, String displayName, String mobile, String photo) {
        Intent callingIntent = new Intent(context, ContactDetailsActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_DISPLAYNAME, displayName);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_MOBILE, mobile);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_PHOTO, photo);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        DataBindingUtil.setContentView(this, R.layout.activity_contact_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("通讯录");
        }
        this.initializeActivity(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INTENT_EXTRA_PARAM_DISPLAYNAME, this.displayName);
            outState.putString(INTENT_EXTRA_PARAM_MOBILE, this.mobile);
            outState.putString(INTENT_EXTRA_PARAM_PHOTO, this.photo);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.displayName = getIntent().getStringExtra(INTENT_EXTRA_PARAM_DISPLAYNAME);
            this.mobile = getIntent().getStringExtra(INTENT_EXTRA_PARAM_MOBILE);
            this.photo = getIntent().getStringExtra(INTENT_EXTRA_PARAM_PHOTO);
            addFragment(R.id.fl_fragment_demo, ContactDetailsFragment.newInstance(this.displayName, this.mobile, this.photo), ContactDetailsFragment.TAG);
        } else {
            this.displayName = savedInstanceState.getString(INTENT_EXTRA_PARAM_DISPLAYNAME);
            this.mobile = savedInstanceState.getString(INTENT_EXTRA_PARAM_MOBILE);
            this.photo = savedInstanceState.getString(INTENT_EXTRA_PARAM_PHOTO);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
