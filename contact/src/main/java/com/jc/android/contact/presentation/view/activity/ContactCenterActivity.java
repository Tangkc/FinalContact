package com.jc.android.contact.presentation.view.activity;

import android.os.Bundle;
import android.view.Menu;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.fragment.ContactListFragment;
import com.jc.android.module.contact.R;
import com.jc.android.widget.presentation.view.activity.BackActivity;
import com.jc.android.widget.presentation.view.widget.ActivityUtils;

import java.util.HashMap;
import java.util.Map;

public class ContactCenterActivity extends BackActivity {

    private Map<Long, ContactModel> selected = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_contact_center);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new ContactListFragment(), R.id.container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_center, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
