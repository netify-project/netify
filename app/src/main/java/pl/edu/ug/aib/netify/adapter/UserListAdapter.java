package pl.edu.ug.aib.netify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.itemView.UserListItemView;
import pl.edu.ug.aib.netify.itemView.UserListItemView_;

@EBean
public class UserListAdapter extends BaseAdapter {

    @RootContext
    Context context;
    List<User> users = new ArrayList<User>();

    public UserListAdapter(){}

    public void update(UserList userList){
        users.clear();
        users.addAll(userList.records);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserListItemView userListItemView;
        if (convertView == null) {
            userListItemView = UserListItemView_.build(context);
        } else {
            userListItemView = (UserListItemView) convertView;
        }
        userListItemView.bind(getItem(position));
        return userListItemView;
    }
}
