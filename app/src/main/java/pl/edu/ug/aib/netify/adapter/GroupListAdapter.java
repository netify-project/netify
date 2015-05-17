package pl.edu.ug.aib.netify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.itemView.GroupListItemView;
import pl.edu.ug.aib.netify.itemView.GroupListItemView_;

@EBean
public class GroupListAdapter extends BaseAdapter {

    @RootContext
    Context context;
    List<GroupData> groupDatas = new ArrayList<GroupData>();

    public GroupListAdapter(){}

    public void update(GroupDataList groupDataList){
        groupDatas.clear();
        groupDatas.addAll(groupDataList.records);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return groupDatas.size();
    }

    @Override
    public GroupData getItem(int position) {
        return groupDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupListItemView groupListItemView;
        if (convertView == null) {
            groupListItemView = GroupListItemView_.build(context);
        } else {
            groupListItemView = (GroupListItemView) convertView;
        }
        groupListItemView.bind(getItem(position));
        return groupListItemView;
    }
}
