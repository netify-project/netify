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

import pl.edu.ug.aib.netify.GroupActivity_;
import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.GroupListAdapter;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.eventListener.OnEditTextFocusChangeListener;

@EFragment(R.layout.fragment_searchgroups)
public class SearchGroupsFragment extends Fragment {

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
    ListView groupList;
    @Bean
    GroupListAdapter adapter;
    GroupDataList searchedGroups;

    OnSearchGroupsFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        groupList.setAdapter(adapter);
        //Adds highlighting when focused and sets focus on edittext
        searchInput.setOnFocusChangeListener(new OnEditTextFocusChangeListener());
        searchInput.requestFocus();
    }

    public void setSearchedGroups(GroupDataList groups) {
        this.searchedGroups = groups;
        //updates ListAdapter and header with current count
        adapter.update(groups);
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

            listener = (OnSearchGroupsFragmentCommunicationListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnSearchGroupsFragmentCommunicationListener");
        }
    }

    @Click
    void searchButtonClicked(){
        String query = searchInput.getText().toString().trim();
        if(query.isEmpty()) return;
        listener.searchForGroups(query);
        noResultsInfo.setVisibility(View.GONE);

    }

    @ItemClick
    void groupListItemClicked(GroupData group){
        GroupActivity_.intent(this).groupId(group.id).start();
    }

    public interface OnSearchGroupsFragmentCommunicationListener {
        void searchForGroups(String query);
    }
}
