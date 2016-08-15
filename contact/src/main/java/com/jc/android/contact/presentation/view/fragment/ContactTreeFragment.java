package com.jc.android.contact.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.data.entity.Dept;
import com.jc.android.contact.domain.interactor.GetDeptContactList;
import com.jc.android.contact.presentation.ContactTreeBinding;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.activity.ContactDetailsActivity;
import com.jc.android.contact.presentation.view.holder.IconTreeItemHolder;
import com.jc.android.contact.presentation.viewmodel.ContactListViewModel;
import com.jc.android.contact.presentation.viewmodel.ProcessErrorSubscriber;
import com.jc.android.module.contact.R;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that shows a list of Users.
 */
public class ContactTreeFragment extends BaseFragment<ContactListViewModel, ContactTreeBinding> {

    public final static String TAG = ContactTreeFragment.class.getSimpleName();
    private AndroidTreeView tView;
    private TreeNode root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setViewModel(new ContactListViewModel());
        setBinding(DataBindingUtil.<ContactTreeBinding>inflate(inflater, R.layout.fragment_contact_tree, container, true));
        getBinding().setViewModel(getViewModel());

        View rootView = getBinding().getRoot();

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GetDeptContactList getDeptContactList = new GetDeptContactList(App.context());
        getDeptContactList.setId("1");
        getDeptContactList.execute(new ProcessErrorSubscriber<List<Dept>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(List<Dept> depts) {
                Log.d(TAG, "onNext: " + depts.toString());
                ViewGroup containerView = getBinding().container;
                root = TreeNode.root();

                List<TreeNode> nodes = assembleTree(depts, 0);
                root.addChildren(nodes);

//                tView.addNode(root,nodes.get(0));

                tView = new AndroidTreeView(getActivity(), root);

                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                tView.setDefaultViewHolder(IconTreeItemHolder.class);
                tView.setDefaultNodeClickListener(nodeClickListener);
                tView.setDefaultNodeLongClickListener(nodeLongClickListener);

                containerView.addView(tView.getView());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }



    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            if(item.type==1) {
                Intent intent = ContactDetailsActivity.getCallingIntent(App.instance().getCurrentActivity(), item.id);
                ActivityNavigator.to(ContactDetailsActivity.class, intent);
            }
        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
//            Toast.makeText(getActivity(), "Long click: " + item.text, Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    private List<TreeNode> assembleTree(List<Dept> depts, long parentId) {
        List<TreeNode> nodes = new ArrayList<>();
        if (depts.size() == 0) {
            return nodes;
        }

        for (int i = depts.size() - 1; i > -1; i--) {
            Dept dept = depts.get(i);
            if (dept.getParentId() == parentId) {
                TreeNode child = new TreeNode(new IconTreeItemHolder.IconTreeItem(dept.getDeptType() == 1 ? R.string.ic_hotel : R.string.ic_flag, dept.getName(), dept.getId(),0));
                nodes.add(child);
                depts.remove(i);

                //add person
                if(dept.getUserList()!=null){
                    for (Contact contact:dept.getUserList()
                         ) {
                        TreeNode person = new TreeNode(new IconTreeItemHolder.IconTreeItem( R.string.ic_person, contact.getDisplayName(), contact.getId(),1));
                        child.addChild(person);
                    }
                }
            }
        }

        for (TreeNode node : nodes) {
            node.addChildren(assembleTree(depts, ((IconTreeItemHolder.IconTreeItem)node.getValue()).id));
        }
        return nodes;
    }
}
