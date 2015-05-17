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
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment_;
import pl.edu.ug.aib.netify.navigationDrawer.DrawerHandler;
import pl.edu.ug.aib.netify.rest.RestHomeBackgroundTask;

@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity implements UserGroupsFragment.OnUserGroupsFragmentCommunicationListener {

    @Pref
    UserPreferences_ preferences;

    public final String LOG_TAG = this.getClass().getSimpleName();
    @Bean
    DrawerHandler drawerHandler;
    @Bean
    @NonConfigurationInstance
    RestHomeBackgroundTask restBackgroundTask;

    @AfterViews
    void init() {
        drawerHandler.init();
        //Get current user's Group List
        //getUserGroupList(Integer.toString(preferences.id().get()));
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
}
