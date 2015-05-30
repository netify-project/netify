package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.SelectUserListAdapter;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;

@EFragment(R.layout.fragment_remove_members)
public class RemoveMembersFragment extends DialogFragment {

    @FragmentArg
    int userId;
    @FragmentArg
    UserList groupMembers;
    @FragmentArg
    MemberGroupDataList memberGroupDataList;
    @ViewById
    ListView membersList;
    @ViewById
    Button removeMembersButton;
    @ViewById
    ProgressBar progressBar;
    @Bean
    SelectUserListAdapter adapter;
    OnRemoveMembersFragmentCommunicationListener listener;
    public RemoveMembersFragment(){}

    @AfterViews
    void init(){
        getDialog().setTitle(R.string.remove_members);
        membersList.setAdapter(adapter);
        adapter.update(groupMembers);
        //remove current user from the list (can't remove oneself)
        adapter.removeUser(userId);
    }

    @Click
    void removeMembersButtonClicked(){
        ArrayList<User> selectedUsers = adapter.getSelectedUsers();
        //find membergroupdata for each selected user
        ArrayList<MemberGroupData> memberGroupDatas = new ArrayList<>();
        MemberGroupData memberGroupData;
        for(User user : selectedUsers) {
            memberGroupData = memberGroupDataList.getMemberGroupDataByUserId(Integer.toString(user.id));
            if(memberGroupData!=null) memberGroupDatas.add(memberGroupData);
        }
        if(!memberGroupDatas.isEmpty()) listener.removeGroupMembers(memberGroupDatas, selectedUsers);
        dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnRemoveMembersFragmentCommunicationListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnRemoveMembersFragmentCommunicationListener");
        }
    }

    public interface OnRemoveMembersFragmentCommunicationListener{
        public void removeGroupMembers(ArrayList<MemberGroupData> memberGroupDatas, ArrayList<User> removedUsers);
    }
}
