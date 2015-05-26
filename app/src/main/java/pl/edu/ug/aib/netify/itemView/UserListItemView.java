package pl.edu.ug.aib.netify.itemView;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.FriendData;
import pl.edu.ug.aib.netify.data.User;

@EViewGroup(R.layout.user_list_item)
public class UserListItemView extends RelativeLayout {


    @ViewById
    TextView firstNameField;
    @ViewById
    TextView lastNameField;

    //Context context;

    public UserListItemView(Context context) {
        super(context);
        //this.context = context;
    }

    public void bind(User user){

        firstNameField.setText(user.firstName);
        lastNameField.setText(user.lastName);
    }
}
