package com.jc.android.demo.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jc.android.app.contact.R;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.contact.presentation.view.activity.ContactListActivity;
import com.jc.android.contact.presentation.view.activity.ContentBuilder;
import com.jc.android.widget.presentation.view.activity.BackActivity;

public class MenuActivity extends BackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new ContentBuilder(MenuActivity.this)
                        .range(0, -1)
                        .page(ContentBuilder.PAGE_USER_LIST)
                        .intent();
                ActivityNavigator.to(ContactListActivity.class, intent);
            }
        });

        findViewById(R.id.single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new ContentBuilder(MenuActivity.this)
                        .range(0, -1)
                        .type(ContentBuilder.VIEW_TYPE_SINGLE)
                        .page(ContentBuilder.PAGE_USER_LIST)
                        .intent();
                ActivityNavigator.to(ContactListActivity.class, intent);
            }
        });

        findViewById(R.id.multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new ContentBuilder(MenuActivity.this)
                        .range(0, -1)
                        .type(ContentBuilder.VIEW_TYPE_MULTIPLE)
                        .page(ContentBuilder.PAGE_USER_TREE)
                        .intent();
                ActivityNavigator.to(ContactListActivity.class, intent);
            }
        });
    }
}
