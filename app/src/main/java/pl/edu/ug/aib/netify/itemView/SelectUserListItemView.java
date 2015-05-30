package pl.edu.ug.aib.netify.itemView;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.User;

@EViewGroup(R.layout.select_user_list_item)
public class SelectUserListItemView extends UserListItemView {

    @ViewById
    CheckBox checkBox;


    public SelectUserListItemView(Context context) {
        super(context);
    }

    @Override
    public void bind(User user){
        super.bind(user);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getUser().isChecked = checkBox.isChecked();
            }
        });
    }
}
