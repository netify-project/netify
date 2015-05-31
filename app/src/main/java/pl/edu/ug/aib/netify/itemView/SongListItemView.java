package pl.edu.ug.aib.netify.itemView;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.SongData;


@EViewGroup(R.layout.song_list_item)
public class SongListItemView extends RelativeLayout {


    @ViewById
    TextView title;
    @ViewById
    TextView group;

    SongData songData;

    OnSongListItemViewCommunicationListener listener;

    public SongListItemView(Context context) {
        super(context);
        try{
            listener = (OnSongListItemViewCommunicationListener)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Context must implement OnSongListItemViewCommunicationListener");
        }
    }

    public void bind(SongData songData){
       title.setText(songData.title);
        group.setText(songData.groupName);

    }



    public interface OnSongListItemViewCommunicationListener {
    }

}
