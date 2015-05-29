package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.GroupActivity_;
import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.UserListAdapter;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.UserList;

@EFragment(R.layout.fragment_group)
public class GroupFragment extends Fragment {

    @FragmentArg
    GroupData group;

    @ViewById
    ListView groupMembersList;

    OnGroupFragmentCommunicationListener listener;

    UserList groupMembers;
    @Bean
    UserListAdapter adapter;

    @AfterViews
    void init(){
        groupMembersList.setAdapter(adapter);
    }
    //get group members after group data has been injected to fragment
    @AfterInject
    void onDataInjected(){
        listener.getGroupMembers(group);
    }

    public void setGroupMembers(UserList groupMembers) {
        this.groupMembers = groupMembers;
        adapter.update(groupMembers);
    }

    @Click
    void groupPlaylistButtonClicked(){
        GroupActivity_.intent(this).groupData(group).start();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnGroupFragmentCommunicationListener)activity;

        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnGroupFragmentCommunicationListener");
        }
    }

    public interface OnGroupFragmentCommunicationListener{
        public void getGroupMembers(GroupData groupData);
    }
}
