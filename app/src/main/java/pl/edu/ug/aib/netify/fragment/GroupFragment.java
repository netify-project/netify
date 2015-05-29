package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;

@EFragment(R.layout.fragment_group)
public class GroupFragment extends Fragment {

    @FragmentArg
    int userId;
    @FragmentArg
    GroupData group;

    @ViewById
    ImageView groupThumbnail;
    @ViewById
    TextView groupGenre;
    @ViewById
    TextView groupDescription;
    @ViewById
    LinearLayout radioGroupLayout;
    @ViewById
    RadioButton privateStatus;
    @ViewById
    RadioButton publicStatus;
    @ViewById
    LinearLayout buttonsLayout;
    @ViewById
    Button joinGroupButton;
    @ViewById
    Button leaveGroupButton;
    @ViewById
    Button inviteUserButton;
    @ViewById
    LinearLayout memberListLayout;
    @ViewById
    ListView groupMembersList;
    @ViewById
    LinearLayout progressBarLayout;

    OnGroupFragmentCommunicationListener listener;

    UserList groupMembers;
    //List of objects connecting users to the groups
    MemberGroupDataList membersOfTheGroupDataList;
    @Bean
    UserListAdapter adapter;

    //get group members after group data has been injected to fragment
    @AfterInject
    void onDataInjected(){
        listener.getGroupMembers(group);
    }

    @AfterViews
    void init(){
        groupMembersList.setAdapter(adapter);
        //Populate layout
        Picasso.with(getActivity()).load(group.thumbnail).placeholder(R.drawable.netifylogo).into(groupThumbnail);
        groupGenre.setText(String.format("%s: %s", getString(R.string.genre), group.genre));
        if(group.description != null) groupDescription.setText(String.format("%s: %s", getString(R.string.description), group.description));

    }


    public void setGroupMembers(UserList groupMembers, MemberGroupDataList memberGroupDataList) {
        this.groupMembers = groupMembers;
        this.membersOfTheGroupDataList = memberGroupDataList;
        adapter.update(groupMembers);
        updateLayoutVisibility();
    }
    public void setNewGroupMember(MemberGroupData memberGroupData, User addedUser){
        membersOfTheGroupDataList.records.add(memberGroupData);
        groupMembers.records.add(addedUser);
        adapter.update(groupMembers);
        updateLayoutVisibility();
    }
    public void removeGroupMember(MemberGroupData removedMemberGroupData){
        membersOfTheGroupDataList.records.remove(removedMemberGroupData);
        groupMembers.deleteUser(Integer.parseInt(removedMemberGroupData.userId));
        adapter.update(groupMembers);
        updateLayoutVisibility();
    }

    private void updateLayoutVisibility(){
        //check if info about group membership is present
        if(groupMembers != null){
            hideProgressBar();
            //find current user in membership data
            MemberGroupData userMemberGroupData = membersOfTheGroupDataList.getMemberGroupDataByUserId(Integer.toString(userId));
            //if is not a member, show join button
            if(userMemberGroupData == null) joinGroupButton.setVisibility(View.VISIBLE);
            else{
                //if is a member, show leave and invite button
                leaveGroupButton.setVisibility(View.VISIBLE);
                inviteUserButton.setVisibility(View.VISIBLE);
            }
            //show members list
            memberListLayout.setVisibility(View.VISIBLE);
            updateListViewHeight();
        }
        //check if current user is group founder
        if(userId == group.founder){
            //show radiobuttons to update group status
            radioGroupLayout.setVisibility(View.VISIBLE);
            //show only invite button
            leaveGroupButton.setVisibility(View.GONE);
            inviteUserButton.setVisibility(View.VISIBLE);
        }

    }
    private void updateListViewHeight(){
        //adjust listview height since in won't adjust automatically with scrollview as parent
        ViewGroup.LayoutParams memberListParams = groupMembersList.getLayoutParams();
        int itemHeight = (int)getResources().getDimension(R.dimen.user_list_item_height);
        memberListParams.height = (itemHeight+2) * adapter.getCount();
        groupMembersList.setLayoutParams(memberListParams);
    }
    //show progressbar in place of buttons
    private void showProgressBar(){
        progressBarLayout.setVisibility(View.VISIBLE);
        joinGroupButton.setVisibility(View.GONE);
        leaveGroupButton.setVisibility(View.GONE);
        inviteUserButton.setVisibility(View.GONE);
    }
    private void hideProgressBar(){
        progressBarLayout.setVisibility(View.GONE);
    }

    @Click
    void groupPlaylistButtonClicked(){
        GroupActivity_.intent(this).groupData(group).start();
    }
    @Click
    void joinGroupButtonClicked(){
        MemberGroupData newMemberGroupData = new MemberGroupData();
        newMemberGroupData.groupId = group.id;
        newMemberGroupData.userId = Integer.toString(userId);
        listener.joinGroup(newMemberGroupData);
        showProgressBar();
    }
    @Click
    void leaveGroupButtonClicked(){
        MemberGroupData currentUserMemberGroupData = membersOfTheGroupDataList.getMemberGroupDataByUserId(Integer.toString(userId));
        if(currentUserMemberGroupData != null) {
            listener.leaveGroup(currentUserMemberGroupData);
            showProgressBar();
        }
    }
    //Invite user to join the group
    @Click
    void inviteUserButtonClicked(){

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
        public void joinGroup(MemberGroupData memberGroupData);
        public void leaveGroup(MemberGroupData memberGroupData);
        public void inviteUser();
        public void removeMember(MemberGroupData memberGroupData);
    }
}