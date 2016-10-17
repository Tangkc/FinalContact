package com.jc.android.contact.presentation.view.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.activity.ContactCenterActivity;
import com.jc.android.contact.presentation.view.activity.ContentBuilder;
import com.jc.android.contact.presentation.view.fragment.ContactTreeFragment;
import com.jc.android.module.contact.R;
import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
public class SelectableItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private CheckBox nodeSelector;
    private RadioButton nodeRadio;
    private List<TreeNode> list = new ArrayList<>();


    public SelectableItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_item, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);


        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);
        nodeRadio = (RadioButton) view.findViewById(R.id.select_radio_button);
        //不是初始化选择
        if (!ContactCenterActivity.isInitSelected) {
            String[] selectedIds = ContactCenterActivity.selectedIds.split(",");
            for (String id : selectedIds) {
                if (TextUtils.isEmpty(id)) {
                    continue;
                }
                if (id.equals(String.valueOf(((IconTreeItemHolder.IconTreeItem) node.getValue()).id))) {
                    if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE && ContactCenterActivity.selected.size() > 0) {
                        break;
                    }
                    /// setValueToMap(node,);
                    node.setSelected(true);
                    setValueToMap(node, true);
                }
            }
        }
        //加单/多选判断 否则 RadioButton和CheckBox会使mNode发生很奇怪的现象
        if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE)
            nodeSelector.setChecked(node.isSelected());
        if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE)
            nodeRadio.setChecked(node.isSelected());
        return view;
    }


    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled && ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE ? View.VISIBLE : View.GONE);
        nodeRadio.setVisibility(editModeEnabled && ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE ? View.VISIBLE : View.GONE);
        //加单/多选判断 否则 RadioButton和CheckBox会使mNode发生很奇怪的现象
        if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE)
            nodeSelector.setChecked(mNode.isSelected());
        if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE)
            nodeRadio.setChecked(mNode.isSelected());
    }

    //把Node对应的Model放入Map中
    public void setValueToMap(TreeNode node, boolean isChecked) {
        for (int j = 0; j < ContactTreeFragment.listContact.size(); j++) {
            if (((IconTreeItemHolder.IconTreeItem) node.getValue()).id == ContactTreeFragment.listContact.get(j).getId()) {
                if (isChecked)
                    ContactCenterActivity.selected.put(ContactTreeFragment.listContact.get(j).getId(), ContactTreeFragment.listContact.get(j));
                else
                    ContactCenterActivity.selected.remove(ContactTreeFragment.listContact.get(j).getId());
            }
        }
    }
}
