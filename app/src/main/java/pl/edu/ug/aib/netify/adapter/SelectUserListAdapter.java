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
import pl.edu.ug.aib.netify.itemView.SelectUserListItemView;
import pl.edu.ug.aib.netify.itemView.SelectUserListItemView_;
import pl.edu.ug.aib.netify.itemView.UserListItemView;
import pl.edu.ug.aib.netify.itemView.UserListItemView_;

@EBean
public class SelectUserListAdapter extends UserListAdapter {

    //public SelectUserListAdapter(){}

    public ArrayList<User> getSelectedUsers(){
        ArrayList<User> selected = new ArrayList<>();
        for(User user : users){
            if(user.isChecked) selected.add(user);
        }
        return selected;
    }
    public void removeUser(int userId){
        for(User user : users){
            if(user.id == userId) {
                users.remove(user);
                notifyDataSetChanged();
                return;
            }
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectUserListItemView selectUserListItemView;
        if (convertView == null) {
            selectUserListItemView = SelectUserListItemView_.build(context);
        } else {
            selectUserListItemView = (SelectUserListItemView) convertView;
        }
        selectUserListItemView.bind(getItem(position));
        return selectUserListItemView;
    }
}
