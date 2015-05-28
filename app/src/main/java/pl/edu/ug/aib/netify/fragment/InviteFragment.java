package pl.edu.ug.aib.netify.fragment;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.InviteListAdapter;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.InviteDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;

@EFragment(R.layout.fragment_invites)
public class InviteFragment extends Fragment {


    @ViewById
    ProgressBar progressBar;
    @ViewById
    ListView invitesList;
    @ViewById
    TextView noInvitesInfo;

    @ViewById
    TextView title;
    @Bean
    InviteListAdapter adapter;
    InviteDataList userInvite;
    InviteData inviteData;
    @InstanceState
    User user;

    OnUserInvitesFragmentCommunicationListener listener;


    @AfterViews
    void init(){
        invitesList.setAdapter(adapter);
    }

    public void setUserInvite(InviteDataList userInvite) {
        this.userInvite = userInvite;
        adapter.update(userInvite);
        progressBar.setVisibility(View.GONE);
        if(adapter.getCount() == 0) noInvitesInfo.setVisibility(View.VISIBLE);
    }
    //remove invite from list after accepting or deleting invite
    public void removeInvite(InviteData inviteData){
        adapter.remove(inviteData);
        if(adapter.getCount() == 0) noInvitesInfo.setVisibility(View.VISIBLE);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            listener = (OnUserInvitesFragmentCommunicationListener)activity;
            listener.getUserInvitesList();
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnUserInvitesFragmentCommunicationListener");
        }
    }

    public interface OnUserInvitesFragmentCommunicationListener {
        void getUserInvitesList();
    }



}
