package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.eventListener.OnEditTextFocusChangeListener;

@EFragment(R.layout.fragment_addgroup)
public class AddGroupFragment extends Fragment {

    @ViewById
    TextView groupName;
    @ViewById
    TextView groupGenre;
    @ViewById
    TextView groupDescription;
    @ViewById
    RelativeLayout noSongLayout;
    @ViewById
    FrameLayout songAddedLayout;
    @ViewById
    ImageView firstSongThumbnail;
    @ViewById
    TextView firstSongTitle;
    @ViewById
    RadioButton radioPrivate;

    SongData firstSong;

    OnAddGroupFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        OnEditTextFocusChangeListener onEditTextFocusChangeListener = new OnEditTextFocusChangeListener();
        groupName.setOnFocusChangeListener(onEditTextFocusChangeListener);
        groupDescription.setOnFocusChangeListener(onEditTextFocusChangeListener);
        groupName.requestFocus();
    }

    public void setFirstSong(SongData firstSong) {
        this.firstSong = firstSong;
        //show panel with video thumbnail and title
        showSongAddedLayout();
    }
    private void showSongAddedLayout(){
        if(firstSong == null ) return;
        noSongLayout.setVisibility(View.GONE);
        songAddedLayout.setVisibility(View.VISIBLE);
        Picasso.with(getActivity()).load(firstSong.thumbnail).into(firstSongThumbnail);
        firstSongTitle.setText(firstSong.title);
    }
    private void hideSongAddedLayout(){
        songAddedLayout.setVisibility(View.GONE);
        noSongLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{

            listener = (OnAddGroupFragmentCommunicationListener)activity;

        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnAddGroupFragmentCommunicationListener");
        }
    }

    @Click
    void addFirstSongClicked(){
        //create empty SongData object to use existing communication between Group- and YoutubeSearchActivity
        SongData songData = new SongData();
        listener.addFirstSong(songData);
    }

    @Click
    void removeSongButtonClicked(){
        hideSongAddedLayout();
        firstSong = null;
    }
    @Click
    void addGroupButtonClicked(){
        //Validate required fields
        if(groupName.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.add_group_name), Toast.LENGTH_LONG).show();
            return;
        }
        if(firstSong == null){
            Toast.makeText(getActivity(), getString(R.string.no_song_added), Toast.LENGTH_LONG).show();
            return;
        }
        //Create new GroupData object and pass it to activity
        GroupData newGroup = new GroupData();
        newGroup.name = groupName.getText().toString().trim();
        newGroup.description = groupDescription.getText().toString().trim();
        newGroup.isPrivate = radioPrivate.isChecked();
        newGroup.genre = groupGenre.getText().toString().trim();
        listener.addNewGroup(newGroup, firstSong);
    }

    public interface OnAddGroupFragmentCommunicationListener {
        void addNewGroup(GroupData groupData, SongData firstSong);
        void addFirstSong(SongData songData);
    }
}
