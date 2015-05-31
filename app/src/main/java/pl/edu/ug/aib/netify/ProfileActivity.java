package pl.edu.ug.aib.netify;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.netify.adapter.TabsPagerAdapter;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.fragment.FriendsFragment;
import pl.edu.ug.aib.netify.fragment.ProfileFragment;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment;
import pl.edu.ug.aib.netify.rest.RestProfileBackgroundTask;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends ActionBarActivity implements ActionBar.TabListener,
        FriendsFragment.OnUserFriendsFragmentCommunicationListener,
        UserGroupsFragment.OnUserGroupsFragmentCommunicationListener,
        ProfileFragment.OnProfileFragmentCommunicationListener {
    @ViewById
    ViewPager pager;
    TabsPagerAdapter adapter;
    ActionBar actionBar;
    @Extra
    User user;
    @Bean
    @NonConfigurationInstance
    RestProfileBackgroundTask restBackgroundTask;
    @Pref
    UserPreferences_ preferences;


    @AfterViews
    void init() {
        // Initialization
        // Tab titles
        String[] tabs = { getString(R.string.title_profile), getString(R.string.title_groups), getString(R.string.title_friends) };
        actionBar = getSupportActionBar();
        adapter = new TabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        actionBar.setTitle(user.firstName + " " + user.lastName);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        restBackgroundTask.getUserSongs(Integer.toString(user.id), preferences.sessionId().get());
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void getUserFriendsList() {
        restBackgroundTask.getUserFriends(Integer.toString(user.id), preferences.sessionId().get());
    }

    public void onSendNewInviteSuccess(InviteData inviteData){
        Toast.makeText(this, "You have sent the invitation.", Toast.LENGTH_LONG).show();
    }

    public void onUserFriendsListDownloaded(UserList userList){
        adapter.getFriendsFragment().setUserFriends(userList);
    }
    @Override
    public void getUserGroupList() {
        restBackgroundTask.getUserGroups(Integer.toString(user.id));
    }

    @Override
    public void launchGroupFragment(GroupData groupData) {

    }

    @Override
    public void getUserSongs(){
        restBackgroundTask.getUserSongs(Integer.toString(user.id), preferences.sessionId().get());
    }

    public void onUserSongsListDownloaded(SongDataList songDataList){
        adapter.getProfileFragment().setUserSongsList(songDataList);
    }

    public void onUserGroupListDownloaded(GroupDataList groupDataList){
        adapter.getGroupsFragment().setUserGroups(groupDataList);
    }
    public void showError(Exception e){
        //display error message
        Log.d("ProfileActivity", e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    //ProfileFragment communication
    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void sendNewInvite(InviteData inviteData) {
        restBackgroundTask.sendNewInvite(Integer.toString(preferences.id().get()), preferences.sessionId().get(),
                preferences.firstName().get(), preferences.lastName().get(), inviteData);
    }

    @OptionsItem(android.R.id.home)
    void homeSelected(){
        finish();
    }
}
