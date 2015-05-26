package pl.edu.ug.aib.netify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.fragment.AddGroupFragment;
import pl.edu.ug.aib.netify.fragment.FriendsFragment;
import pl.edu.ug.aib.netify.fragment.LogoutFragment;
import pl.edu.ug.aib.netify.fragment.SearchGroupsFragment;
import pl.edu.ug.aib.netify.fragment.SearchUsersFragment;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment_;
import pl.edu.ug.aib.netify.navigationDrawer.DrawerHandler;
import pl.edu.ug.aib.netify.rest.RestHomeBackgroundTask;

@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity implements UserGroupsFragment.OnUserGroupsFragmentCommunicationListener,
        AddGroupFragment.OnAddGroupFragmentCommunicationListener,
        SearchGroupsFragment.OnSearchGroupsFragmentCommunicationListener,
        SearchUsersFragment.OnSearchUsersFragmentCommunicationListener,
        FriendsFragment.OnUserFriendsFragmentCommunicationListener,
        LogoutFragment.OnLogoutFragmentCommunicationListener
{

    @Pref
    UserPreferences_ preferences;

    public static final int INTENT_FIRST_SONG_ADDED = 1;
    public final String LOG_TAG = this.getClass().getSimpleName();
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
    }

    //Logout
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


}
