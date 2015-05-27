package pl.edu.ug.aib.netify.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.edu.ug.aib.netify.fragment.FriendsFragment;
import pl.edu.ug.aib.netify.fragment.FriendsFragment_;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment_;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    Fragment profileFragment;
    UserGroupsFragment groupsFragment;
    FriendsFragment friendsFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        profileFragment = new FriendsFragment_();
        groupsFragment = new UserGroupsFragment_();
        friendsFragment = new FriendsFragment_();
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Profile fragment
                return profileFragment;
            case 1:
                // Groups list fragment
                return groupsFragment;
            case 2:
                // Friend list fragment
                return friendsFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

    public Fragment getProfileFragment() {
        return profileFragment;
    }

    public UserGroupsFragment getGroupsFragment() {
        return groupsFragment;
    }

    public FriendsFragment getFriendsFragment() {
        return friendsFragment;
    }
}
