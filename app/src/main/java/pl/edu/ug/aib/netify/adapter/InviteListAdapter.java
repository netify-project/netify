package pl.edu.ug.aib.netify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.InviteDataList;
import pl.edu.ug.aib.netify.itemView.InviteListItemView;
import pl.edu.ug.aib.netify.itemView.InviteListItemView_;

@EBean
public class InviteListAdapter extends BaseAdapter {

    @RootContext
    Context context;
    List<InviteData> invites = new ArrayList<InviteData>();

    public InviteListAdapter(){}

    public void update(InviteDataList inviteDataList){
        invites.clear();
        invites.addAll(inviteDataList.records);
        notifyDataSetChanged();
    }
    public void remove(InviteData invite){
        invites.remove(invite);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return invites.size();
    }

    @Override
    public InviteData getItem(int position) {
        return invites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InviteListItemView inviteListItemView;
        if (convertView == null) {
            inviteListItemView = InviteListItemView_.build(context);
        } else {
            inviteListItemView = (InviteListItemView) convertView;
        }
        inviteListItemView.bind(getItem(position));
        return inviteListItemView;
    }
}
