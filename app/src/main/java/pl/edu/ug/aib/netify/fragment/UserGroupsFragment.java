package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.GroupActivity_;
import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.GroupListAdapter;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;

@EFragment(R.layout.fragment_usergroups)
public class UserGroupsFragment extends Fragment {

    @ViewById
    ProgressBar progressBar;
    @ViewById
    ListView groupList;
    @ViewById
    TextView noGroupsInfo;
    @Bean
    GroupListAdapter adapter;
    GroupDataList userGroups;

    OnUserGroupsFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        groupList.setAdapter(adapter);
    }

    public void setUserGroups(GroupDataList userGroups) {
        this.userGroups = userGroups;
        adapter.update(userGroups);
        progressBar.setVisibility(View.GONE);
        if(adapter.getCount() == 0) noGroupsInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            listener = (OnUserGroupsFragmentCommunicationListener)activity;
            listener.getUserGroupList();
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnUserGroupsFragmentCommunicationListener");
        }
    }

    public interface OnUserGroupsFragmentCommunicationListener {
        void getUserGroupList();
    }
    @ItemClick
    void groupListItemClicked(GroupData group){
        GroupActivity_.intent(this).groupId(group.id).start();
    }
}
