package pl.edu.ug.aib.netify.itemView;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.User;


@EViewGroup(R.layout.user_list_item)
public class UserListItemView extends RelativeLayout {


    @ViewById
    TextView firstNameField;
    @ViewById
    TextView lastNameField;
    @ViewById
    Button sendInvite;

    User user;

    OnUserListCommunicationListener listener;
    //Context context;

    public UserListItemView(Context context) {
        super(context);
        //this.context = context;
    }

    public void bind(User user){

        firstNameField.setText(user.firstName);
        lastNameField.setText(user.lastName);

    }

    public void sendInviteConfirmed(InviteData inviteData){
        sendInvite.setVisibility(View.GONE);
    }



    @Click
    void sendInviteClicked(){
        InviteData inviteData = new InviteData();
        inviteData.groupId=null;
        inviteData.toUser=user.id;
        listener.sendInvite(inviteData);


    }
    public interface OnUserListCommunicationListener {
        void sendInvite(InviteData inviteData);
    }

}
