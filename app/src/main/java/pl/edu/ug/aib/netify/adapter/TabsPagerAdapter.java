package pl.edu.ug.aib.netify.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.edu.ug.aib.netify.fragment.FriendsFragment;
import pl.edu.ug.aib.netify.fragment.FriendsFragment_;
import pl.edu.ug.aib.netify.fragment.ProfileFragment;
import pl.edu.ug.aib.netify.fragment.ProfileFragment_;
import pl.edu.ug.aib.netify.fragment.ProgressBarFragment_;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment;
import pl.edu.ug.aib.netify.fragment.UserGroupsFragment_;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    ProfileFragment profileFragment;
    UserGroupsFragment groupsFragment;
    FriendsFragment friendsFragment;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Profile fragment
                profileFragment = new ProfileFragment_();
                return profileFragment;
            case 1:
                // Groups list fragment
                groupsFragment = new UserGroupsFragment_();
                return groupsFragment;
            case 2:
                // Friend list fragment
                friendsFragment = new FriendsFragment_();
                return friendsFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

    public ProfileFragment getProfileFragment() {
        return profileFragment;
    }

    public UserGroupsFragment getGroupsFragment() {
        return groupsFragment;
    }

    public FriendsFragment getFriendsFragment() {
        return friendsFragment;
    }
}
