package pl.edu.ug.aib.netify.itemView;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.SongData;

@EViewGroup(R.layout.player_proposition_item)
public class PlayerPropositionItemView extends RelativeLayout {

    @ViewById
    ImageView video_thumbnail;
    @ViewById
    TextView video_title;
    @ViewById
    TextView video_user;

    Context context;

    public PlayerPropositionItemView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(SongData songData){
        Picasso.with(context).load(songData.thumbnail).into(video_thumbnail);
        video_title.setText(songData.title);
        //TODO Get and show name of the user
        video_user.setText("User");
    }
}
