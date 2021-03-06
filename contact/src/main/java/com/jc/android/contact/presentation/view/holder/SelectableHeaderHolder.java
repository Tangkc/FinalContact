package com.jc.android.contact.presentation.view.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
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
public class SelectableHeaderHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private PrintView arrowView;
    private CheckBox nodeSelector;
 //   List<TreeNode> list = new ArrayList<>();


    public SelectableHeaderHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_header, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.GONE);
        }

        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);

        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setSelected(isChecked);
                setNodeSelector(node, isChecked);

            }
        });
      //  setParentIs(node);
        nodeSelector.setChecked(node.isSelected());


        return view;
    }

//    public void setParentIs(TreeNode node) {
//        list = node.getChildren();
//        if (!list.isEmpty()) {
//            int trueNum = list.size();
//            int Num = 0;
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).isSelected())
//                    Num++;
//            }
//            if (trueNum == Num) {
//                node.setSelected(true);
//              setParentIs(node);
//            }
//        }
//    }

    /**
     * 递归遍历出整个树的人员
     *
     * @param treeNode
     * @param isSelect
     */
    public void setNodeSelector(TreeNode treeNode, boolean isSelect) {
        for (TreeNode n : treeNode.getChildren()) {
            getTreeView().selectNode(n, isSelect);
            if (!n.getChildren().isEmpty())
                setNodeSelector(n, isSelect);
            else if (n.getChildren().isEmpty()) {
                for (int j = 0; j < ContactTreeFragment.listContact.size(); j++) {
                    if (((IconTreeItemHolder.IconTreeItem) n.getValue()).id == ContactTreeFragment.listContact.get(j).getId()) {
                        if (isSelect)
                            ContactCenterActivity.selected.put(ContactTreeFragment.listContact.get(j).getId(), ContactTreeFragment.listContact.get(j));
                        else
                            ContactCenterActivity.selected.remove(ContactTreeFragment.listContact.get(j).getId());
                    }
                }
            }
        }
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled && ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE ? View.VISIBLE : View.GONE);
        nodeSelector.setChecked(mNode.isSelected());
    }

}
