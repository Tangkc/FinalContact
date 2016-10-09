package com.jc.android.contact.presentation.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jc.android.contact.presentation.view.activity.ContactCenterActivity;
import com.jc.android.contact.presentation.view.activity.ContentBuilder;
import com.jc.android.contact.presentation.view.widget.AutoLoadImageView;
import com.jc.android.module.contact.R;
import com.jc.android.contact.presentation.model.ContactModel;

import lombok.Getter;
import retrofit2.http.POST;

public class SortGroupMemberAdapter extends BaseAdapter implements SectionIndexer {

    private List<ContactModel> list = new ArrayList<>();
    private Context mContext;

    public SortGroupMemberAdapter(Context mContext, @NonNull List<ContactModel> list) {
        this.mContext = mContext;
        updateListView(list);
    }

    public void updateListView(List<ContactModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        final ContactModel model = list.get(position);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.row_contact, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.update(position, model);

        return view;

    }

    public void onItemSelected(int pos) {
        ContactModel model = list.get(pos);
        if (ContactCenterActivity.selected.get(model.getId())==null) {
            if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE) {
                ContactCenterActivity.selected.clear();
            }
            ContactCenterActivity.selected.put(model.getId(), model);
        } else if (ContactCenterActivity.viewType != ContentBuilder.VIEW_TYPE_SINGLE) {
            ContactCenterActivity.selected.remove(model.getId());
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        public RadioButton radioButton;
        public CheckBox checkBox;
        public TextView tvLetter;
        public TextView tvTitle;
        public SimpleDraweeView autoLoadImageView;

        public ViewHolder(View view) {
            radioButton = (RadioButton) view.findViewById(R.id.radio_button);
            checkBox = (CheckBox) view.findViewById(R.id.check_box);
            tvTitle = (TextView) view.findViewById(R.id.title);
            tvLetter = (TextView) view.findViewById(R.id.catalog);
            autoLoadImageView = (SimpleDraweeView) view.findViewById(R.id.row_contact_imageView2);
        }

        public void update(final int position, ContactModel model) {
            if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE) {
                radioButton.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.GONE);

            } else if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE) {
                radioButton.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);

            } else {
                radioButton.setVisibility(View.GONE);
                checkBox.setVisibility(View.GONE);

            }

            boolean isSelected = ContactCenterActivity.selected.get(model.getId())!=null;
            radioButton.setChecked(isSelected);
            checkBox.setChecked(isSelected);

            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(model.getSortLetters());
            } else {
                tvLetter.setVisibility(View.GONE);
            }
            tvTitle.setText(model.getDisplayName());
            if (model.getPhoto() != null) {
                Uri uri = Uri.parse(model.getPhoto());
                autoLoadImageView.setImageURI(uri);
            } else {
                autoLoadImageView.setImageURI(Uri.parse("res://drawable/" + R.mipmap.no_pic));
            }
        }

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}