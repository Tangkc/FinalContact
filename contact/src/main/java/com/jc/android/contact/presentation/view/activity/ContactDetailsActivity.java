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
    public static final String INTENT_EXTRA_PARAM_ID = "org.android10.INTENT_PARAM_DISPLAYNAME";

    private long id;

    public static Intent getCallingIntent(Context context, long id) {
        Intent callingIntent = new Intent(context, ContactDetailsActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_ID, id);
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
            outState.putLong(INTENT_EXTRA_PARAM_ID, this.id);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.id = getIntent().getLongExtra(INTENT_EXTRA_PARAM_ID,0);
            addFragment(R.id.fl_fragment_demo, ContactDetailsFragment.newInstance(this.id), ContactDetailsFragment.TAG);
        } else {
            this.id = savedInstanceState.getLong(INTENT_EXTRA_PARAM_ID);
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
