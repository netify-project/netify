package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.ProfileActivity_;
import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.UserListAdapter;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.eventListener.OnEditTextFocusChangeListener;

@EFragment(R.layout.fragment_searchusers)
public class SearchUsersFragment extends Fragment {

    @ViewById
    LinearLayout searchLayout;
    @ViewById
    LinearLayout searchResultsLayout;
    @ViewById
    EditText searchInput;
    @ViewById
    ImageButton searchButton;
    @ViewById
    TextView searchResultsHeader;
    @ViewById
    TextView noResultsInfo;
    @ViewById
    ListView friendList;
    @Bean
    UserListAdapter adapter;
    UserList searchedUsers;

    OnSearchUsersFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        friendList.setAdapter(adapter);
        //Adds highlighting when focused and sets focus on edittext
        searchInput.setOnFocusChangeListener(new OnEditTextFocusChangeListener());
        searchInput.requestFocus();
    }

    public void setSearchedUsers(UserList users) {
        this.searchedUsers = users;
        //updates ListAdapter and header with current count
        adapter.update(users);
        searchResultsHeader.setText(String.format("%s (%d)",getString(R.string.search_results), adapter.getCount()));
        //set whole results block to visible
        searchResultsLayout.setVisibility(View.VISIBLE);
        //shows information if there are no results
        if(adapter.getCount() == 0) noResultsInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            listener = (OnSearchUsersFragmentCommunicationListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnSearchFriendsFragmentCommunicationListener");
        }
    }

    @Click
    void searchButtonClicked(){
        String query = searchInput.getText().toString().trim();
        if(query.isEmpty()) return;
        listener.searchForUsers(query);
        noResultsInfo.setVisibility(View.GONE);

    }

    public interface OnSearchUsersFragmentCommunicationListener {
        void searchForUsers(String query);
    }

    @ItemClick
    void friendListItemClicked(User user){
        ProfileActivity_.intent(this).user(user).start();
    }


}
