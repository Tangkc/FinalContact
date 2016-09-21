package com.jc.android.demo.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jc.android.app.contact.R;
import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.component.config.presentation.view.activity.ConfigActivity;
import com.jc.android.contact.presentation.view.activity.ContactCenterActivity;
import com.jc.android.contact.presentation.view.activity.ContactListActivity;
import com.jc.android.contact.presentation.view.activity.ContentBuilder;
import com.jc.android.widget.presentation.view.activity.BackActivity;

public class MenuActivity extends BackActivity {

    int code = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new ContentBuilder(MenuActivity.this)
                        .page(ContentBuilder.PAGE_USER_LIST)
                        .intent();
                ActivityNavigator.to(ContactListActivity.class, intent);
            }
        });

        findViewById(R.id.single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new ContentBuilder(MenuActivity.this)
                        .range(1, 1)
                        .type(ContentBuilder.VIEW_TYPE_SINGLE)
                        .page(ContentBuilder.PAGE_USER_LIST)
                        .selected(ids)
                        .list("1,2,3,4,5", "孙贺,张立刚,吴雪,刚哥,杨吉")
                        .intent();
                startActivityForResult(intent, code);
            }
        });

        findViewById(R.id.multiple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new ContentBuilder(MenuActivity.this)
                        .range(1, -1)
                        .type(ContentBuilder.VIEW_TYPE_MULTIPLE)
                        .page(ContentBuilder.PAGE_USER_LIST)
                        .selected(ids)
                        .title("噜啦啦啦")
                        .intent();
                startActivityForResult(intent, code);
            }
        });
    }

    String ids = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == code) {
            if (resultCode == ContactCenterActivity.SELECTED_CONFIRM) {
                if (data==null) {
                    return;
                }

                String ids = data.getStringExtra(ContactCenterActivity.SELECTED_IDS);
                String names = data.getStringExtra(ContactCenterActivity.SELECTED_NAMES);

                this.ids = ids;

                Toast.makeText(App.context(), ids + " and " + names, Toast.LENGTH_SHORT).show();

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
