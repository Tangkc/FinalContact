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
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jc.android.contact.presentation.view.widget.AutoLoadImageView;
import com.jc.android.module.contact.R;
import com.jc.android.contact.presentation.model.ContactModel;

import lombok.Getter;

public class SortGroupMemberAdapter extends BaseAdapter implements SectionIndexer {
    @Getter
    private List<ContactModel> list = new ArrayList<>();
    private Context mContext;

    public SortGroupMemberAdapter(Context mContext, @NonNull List<ContactModel> list) {
        this.mContext = mContext;
        this.list.addAll(list);
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

    // TODO: 2016/7/29 头像加载未完成
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final ContactModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.row_contact, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.autoLoadImageView = (SimpleDraweeView) view.findViewById(R.id.row_contact_imageView2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(this.list.get(position).getDisplayName());
        if (this.list.get(position).getPhoto() != null) {
            Uri uri = Uri.parse(this.list.get(position).getPhoto());
            viewHolder.autoLoadImageView.setImageURI(uri);
        } else {
            viewHolder.autoLoadImageView.setImageURI(Uri.parse("res://drawable/" + R.mipmap.no_pic));
        }
        return view;

    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        SimpleDraweeView autoLoadImageView;

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