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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.view.activity.BaseActivity;
import com.jc.android.module.contact.R;


/**
 * Activity that shows a list of Users.
 */
public class ContactListActivity extends BaseActivity {


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ContactListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        DataBindingUtil.setContentView(this, R.layout.contact_list_activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("通讯录");
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if(item.getItemId()==R.id.show_flatten){
//            ActivityNavigator.to();
        }
        else if(item.getItemId()==R.id.show_tree){
            ActivityNavigator.to(ContactTreeActivity.class);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
