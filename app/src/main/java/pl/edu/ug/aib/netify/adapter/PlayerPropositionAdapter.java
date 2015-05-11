package pl.edu.ug.aib.netify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.itemView.PlayerPropositionItemView;
import pl.edu.ug.aib.netify.itemView.PlayerPropositionItemView_;

@EBean
public class PlayerPropositionAdapter extends BaseAdapter {

    @RootContext
    Context context;
    List<SongData> songDatas = new ArrayList<SongData>();

    public PlayerPropositionAdapter(){}

    public void update(List<SongData> songDataList){
        songDatas.clear();
        songDatas.addAll(songDataList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songDatas.size();
    }

    @Override
    public SongData getItem(int position) {
        return songDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlayerPropositionItemView playerPropositionItemView;
        if (convertView == null) {
            playerPropositionItemView = PlayerPropositionItemView_.build(context);
        } else {
            playerPropositionItemView = (PlayerPropositionItemView) convertView;
        }
        playerPropositionItemView.bind(getItem(position));
        return playerPropositionItemView;
    }
}
