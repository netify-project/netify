package pl.edu.ug.aib.netify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.InviteDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.itemView.InviteListItemView;
import pl.edu.ug.aib.netify.itemView.InviteListItemView_;
import pl.edu.ug.aib.netify.itemView.SongListItemView;
import pl.edu.ug.aib.netify.itemView.SongListItemView_;

@EBean
public class SongListAdapter extends BaseAdapter {

    @RootContext
    Context context;
    List<SongData> songs = new ArrayList<SongData>();

    public SongListAdapter(){}

    public void update(SongDataList songDataList){
        songs.clear();
        songs.addAll(songDataList.records);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public SongData getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongListItemView songListItemView;
        if (convertView == null) {
            songListItemView = SongListItemView_.build(context);
        } else {
           songListItemView = (SongListItemView) convertView;
        }
        songListItemView.bind(getItem(position));
        return songListItemView;
    }
}
