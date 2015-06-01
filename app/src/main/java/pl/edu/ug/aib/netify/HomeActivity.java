package pl.edu.ug.aib.netify;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.InviteDataList;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.fragment.AddGroupFragment;
import pl.edu.ug.aib.netify.fragment.FriendsFragment;
import pl.edu.ug.aib.netify.fragment.GroupFragment;
import pl.edu.ug.aib.netify.fragment.InviteFragment;
import pl.edu.ug.aib.netify.fragment.InviteFriendsFragment;
import pl.edu.ug.aib.netify.fragment.InviteFriendsFragment_;
import pl.edu.ug.aib.netify.fragment.LogoutFragment;
import pl.edu.ug.aib.netify.fragment.RemoveMembersFragment;
import pl.edu.ug.aib.netify.fragment.RemoveMembersFragment_;
import pl.edu.ug.aib.netify.fragment.SearchGroupsFragment;
import pl.edu.ug.aib.netify.fragment.SearchUsersFragment;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment;
import pl.edu.ug.aib.netify.itemView.InviteListItemView;
import pl.edu.ug.aib.netify.itemView.UserListItemView;
import pl.edu.ug.aib.netify.navigationDrawer.DrawerHandler;
import pl.edu.ug.aib.netify.rest.RestHomeBackgroundTask;

@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity implements UserGroupsFragment.OnUserGroupsFragmentCommunicationListener,
        AddGroupFragment.OnAddGroupFragmentCommunicationListener,
        SearchGroupsFragment.OnSearchGroupsFragmentCommunicationListener,
        SearchUsersFragment.OnSearchUsersFragmentCommunicationListener,
        FriendsFragment.OnUserFriendsFragmentCommunicationListener,
        LogoutFragment.OnLogoutFragmentCommunicationListener,
        InviteFragment.OnUserInvitesFragmentCommunicationListener,
        InviteListItemView.OnInviteListItemViewCommunicationListener,
        GroupFragment.OnGroupFragmentCommunicationListener,
        InviteFriendsFragment.OnInviteFriendsFragmentCommunicationListener,
        RemoveMembersFragment.OnRemoveMembersFragmentCommunicationListener

{

    @Pref
    UserPreferences_ preferences;

    public static final int INTENT_FIRST_SONG_ADDED = 1;
    public final String LOG_TAG = this.getClass().getSimpleName();
    public static final String INVITE_DIALOG_FRAGMENT_TAG = "inviteDialog";
    public static final String REMOVE_DIALOG_FRAGMENT_TAG = "removeDialog";
    @Bean
    DrawerHandler drawerHandler;
    @Bean
    @NonConfigurationInstance
    RestHomeBackgroundTask restBackgroundTask;

    @AfterViews
    void init() {
        drawerHandler.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerHandler.getDrawerToggle().syncState();
    }

    @OptionsItem(android.R.id.home)
    public boolean drawerToggleSelected(MenuItem item) {
        return drawerHandler.drawerToggleSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }

    public void showError(Exception e){
        //display error message
        Log.d("HomeActivity", e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    //UserGroupFragment communication
    @Override
    public void getUserGroupList() {
        restBackgroundTask.getUserGroups(Integer.toString(preferences.id().get()));
    }
    public void onUserGroupListDownloaded(GroupDataList groupDataList){
        try {
            UserGroupsFragment fragment = (UserGroupsFragment)drawerHandler.getCurrentFragment();
            fragment.setUserGroups(groupDataList);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of UserGroupsFragment");
        }
    }

    //InvitesFragment communication
    @Override
    public void getUserInvitesList() {
        restBackgroundTask.getUserInvites(Integer.toString(preferences.id().get()), preferences.sessionId().get());
    }
    public void onUserInvitesListDownloaded(InviteDataList inviteDataList){
        try {
            InviteFragment fragment = (InviteFragment)drawerHandler.getCurrentFragment();
            fragment.setUserInvite(inviteDataList);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of InviteFragment");
        }
    }


    //AddGroupFragment communication
    @Override
    public void addNewGroup(GroupData groupData, SongData firstSong) {
        restBackgroundTask.addNewGroup(Integer.toString(preferences.id().get()), preferences.sessionId().get(), groupData, firstSong);
    }

    @Override
    public void addFirstSong(SongData songData) {
        YoutubeSearchActivity_.intent(this).parentSongData(songData).startForResult(INTENT_FIRST_SONG_ADDED);
    }

    @OnActivityResult(INTENT_FIRST_SONG_ADDED)
    void onFirstSongAdded(int result, @OnActivityResult.Extra SongData songData){
        if(result == RESULT_OK){
            try {
                AddGroupFragment fragment = (AddGroupFragment)drawerHandler.getCurrentFragment();
                fragment.setFirstSong(songData);
            }
            catch (ClassCastException e){
                Log.d(this.getClass().getSimpleName(), "Fragment must be instance of UserGroupsFragment");
            }
        }
    }
    public void onNewGroupAdded(GroupData groupData){
        //open new group playlist
        GroupActivity_.intent(this).groupData(groupData).start();
        //clears addgroup screen in case of clicking back button
        drawerHandler.showDefaultScreen();
    }

    //SearchGroupFragment communication
    @Override
    public void searchForGroups(String query) {
        restBackgroundTask.searchGroups(query);
    }
    public void onSearchGroupCompleted(GroupDataList groupDataList){
        try {
            SearchGroupsFragment fragment = (SearchGroupsFragment)drawerHandler.getCurrentFragment();
            fragment.setSearchedGroups(groupDataList);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of UserGroupsFragment");
        }
    }

    //SearchUsersFragment communication
    @Override
    public void searchForUsers(String query) {
        restBackgroundTask.searchUsers(query, preferences.sessionId().get());
    }

    public void onSearchUsersCompleted(UserList userList){
        try {
            SearchUsersFragment fragment = (SearchUsersFragment)drawerHandler.getCurrentFragment();
            //delete current user if on the list
            userList.deleteUser(preferences.id().get());
            fragment.setSearchedUsers(userList);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of SearchUsersFragment");
        }
    }

    //FriendsFragment communication
    @Override
    public void getUserFriendsList() {
        restBackgroundTask.getUserFriends(Integer.toString(preferences.id().get()), preferences.sessionId().get());
    }

    public void onUserFriendsListDownloaded(UserList userFriendsList){
        try {
            FriendsFragment fragment = (FriendsFragment)drawerHandler.getCurrentFragment();
            fragment.setUserFriends(userFriendsList);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of FriendsFragment");
        }
        try {
            InviteFriendsFragment fragment = (InviteFriendsFragment)getSupportFragmentManager().findFragmentByTag(INVITE_DIALOG_FRAGMENT_TAG);
            if(fragment != null) fragment.setUsersList(userFriendsList);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of InviteFriendsFragment");
        }
    }


    //InviteListItemView communication
    @Override
    public void acceptInvite(InviteData invite) {
        //Toast.makeText(this, "Invite accepted", Toast.LENGTH_LONG).show();
        //if no groupId then it is friend invite, else it is group invite
        if(invite.groupId == null) restBackgroundTask.addFriendData(preferences.sessionId().get() ,invite);
        else restBackgroundTask.addMemberToGroup(preferences.sessionId().get(), invite);
    }

    @Override
    public void deleteInvite(InviteData invite) {
        //Toast.makeText(this, "Invite set to delete", Toast.LENGTH_LONG).show();
        restBackgroundTask.deleteInvite(preferences.sessionId().get(), invite);
    }

    public void onAcceptFriendInviteSuccess(InviteData inviteData) {
        onInviteSuccess(inviteData, getString(R.string.add_friend_successful) + " " + inviteData.fullName);
    }
    public void onAcceptGroupInviteSuccess(InviteData inviteData) {
        onInviteSuccess(inviteData, getString(R.string.join_group_successful));
    }
    public void onDeleteInviteSuccess(InviteData inviteData) {
        onInviteSuccess(inviteData, null);
    }
    private void onInviteSuccess(InviteData inviteData, String message){
        //Display toast with appropriate message
        if(message != null) Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        try {
            InviteFragment fragment = (InviteFragment)drawerHandler.getCurrentFragment();
            //remove the invite from the list
            fragment.removeInvite(inviteData);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of InviteFragment");
        }
    }
    //InviteFriendsFragment communication
    @Override
    public void sendGroupInvites(ArrayList<InviteData> inviteDatas) {
        try {
            GroupFragment fragment = (GroupFragment)drawerHandler.getCurrentFragment();
            ///show progressBar indicating that invites are being processed
            fragment.onSendInvitesStart();
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
        }
        restBackgroundTask.postGroupInvites(inviteDatas, preferences.sessionId().get());
    }
    public void onSendGroupInvitesSuccess(){
        try {
            GroupFragment fragment = (GroupFragment)drawerHandler.getCurrentFragment();
            //show that processing is finished
            fragment.onSendInvitesFinish();
            Toast.makeText(this, getString(R.string.invites_sent), Toast.LENGTH_LONG).show();
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
        }
    }

    //RemoveMembersFragment communication
    @Override
    public void removeGroupMembers(ArrayList<MemberGroupData> memberGroupDatas, ArrayList<User> removedUsers){
        try {
            GroupFragment fragment = (GroupFragment)drawerHandler.getCurrentFragment();
            ///show progressBar indicating that removals are being processed (the same method as send invites)
            fragment.onSendInvitesStart();
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
        }
        restBackgroundTask.removeGroupMembers(memberGroupDatas, removedUsers, preferences.sessionId().get());
    }
    public void onRemoveMultipleGroupMembersSuccess(ArrayList<User> removedUsers){
        try {
            GroupFragment fragment = (GroupFragment)drawerHandler.getCurrentFragment();
            //show that processing is finished and remove users from listview adapter
            fragment.onRemoveMembersFinish(removedUsers);
            Toast.makeText(this, getString(R.string.users_removed), Toast.LENGTH_LONG).show();
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
        }
    }


    //Logout communication
    @Override
    public void logout(){
        restBackgroundTask.logout(preferences.sessionId().get());
    }
    public void onLogout(Boolean success){
        if(success) {
            preferences.id().put(0);
            preferences.sessionId().put("");
            preferences.email().put("");
            preferences.password().put("");
            preferences.firstName().put("");
            preferences.lastName().put("");
            preferences.displayName().put("");
            LoginActivity_.intent(this).start();
            finish();
        }
    }

    //GroupFragment communication
    @Override
    public void getGroupMembers(GroupData groupData) {
        restBackgroundTask.getGroupMembers(preferences.sessionId().get(), groupData);
    }

    @Override
    public void joinGroup(MemberGroupData memberGroupData) {
        restBackgroundTask.addGroupMember(memberGroupData, preferences.sessionId().get());
    }

    @Override
    public void leaveGroup(MemberGroupData memberGroupData) {
        restBackgroundTask.removeGroupMember(memberGroupData, preferences.sessionId().get());
    }

    @Override
    public void inviteUsers(GroupData group, UserList userList) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment previous = getSupportFragmentManager().findFragmentByTag(INVITE_DIALOG_FRAGMENT_TAG);
        if (previous != null) {
            ft.remove(previous);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = InviteFriendsFragment_.builder()
                .group(group).groupMembers(userList).userId(preferences.id().get()).build();
        newFragment.show(ft, INVITE_DIALOG_FRAGMENT_TAG);
    }

    @Override
    public void removeMembers(MemberGroupDataList memberGroupDataList, UserList groupMembers) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment previous = getSupportFragmentManager().findFragmentByTag(REMOVE_DIALOG_FRAGMENT_TAG);
        if (previous != null) {
            ft.remove(previous);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = RemoveMembersFragment_.builder()
                .memberGroupDataList(memberGroupDataList).groupMembers(groupMembers)
                .userId(preferences.id().get()).build();
        newFragment.show(ft, REMOVE_DIALOG_FRAGMENT_TAG);
    }

    @Override
    public void updateGroup(GroupData groupData) {
        restBackgroundTask.updateGroupData(groupData, preferences.sessionId().get());
    }

    public void onGetGroupMembersSuccess(UserList userList, MemberGroupDataList memberGroupDataList){
        try {
            GroupFragment fragment = (GroupFragment)drawerHandler.getCurrentFragment();
            //push group members to the fragment
            fragment.setGroupMembers(userList, memberGroupDataList);
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
        }
    }

    public void onAddGroupMemberSuccess(MemberGroupData memberGroupData){
        try {
            GroupFragment fragment = (GroupFragment)drawerHandler.getCurrentFragment();
            //push new group member to the fragment
            fragment.setNewGroupMember(memberGroupData, getCurrentUser());
        }
        catch (ClassCastException e){
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
        }
    }
    private User getCurrentUser(){
        User currentUser = new User();
        currentUser.id = preferences.id().get();
        currentUser.firstName = preferences.firstName().get();
        currentUser.lastName = preferences.lastName().get();
        currentUser.email = preferences.email().get();
        currentUser.displayName = preferences.displayName().get();
        return currentUser;
    }
    public void onRemoveGroupMemberSuccess(MemberGroupData memberGroupData){
        //if user left group, redirect to default screen
        if(memberGroupData.userId.equals(Integer.toString(preferences.id().get()))) drawerHandler.showDefaultScreen();
        else {
            try {
                GroupFragment fragment = (GroupFragment) drawerHandler.getCurrentFragment();
                //push group member to the fragment in order to remove him
                fragment.removeGroupMember(memberGroupData);
            } catch (ClassCastException e) {
                Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
            }
        }
    }
    public void onUpdateGroupSuccess(){
        try {
            GroupFragment fragment = (GroupFragment) drawerHandler.getCurrentFragment();
            //push group member to the fragment in order to remove him
            fragment.groupStatusUpdated();
        } catch (ClassCastException e) {
            Log.d(this.getClass().getSimpleName(), "Fragment must be instance of GroupFragment");
        }
    }
    @Override
    public void launchGroupFragment(GroupData groupData) {
        drawerHandler.setGroupFragment(groupData, preferences.id().get());
    }
    //if back pressed on GroupFragment, it opens default screen
    @Override
    public void onBackPressed(){
        try {
            GroupFragment fragment = (GroupFragment) drawerHandler.getCurrentFragment();
            //push group member to the fragment in order to remove him
            drawerHandler.showDefaultScreen();
        } catch (ClassCastException e) {
            super.onBackPressed();
        }

    }
}
