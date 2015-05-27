package pl.edu.ug.aib.netify.itemView;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.User;


@EViewGroup(R.layout.invite_list_item)
public class InviteListItemView extends RelativeLayout {


    @ViewById
    TextView firstNameField;
    @ViewById
    TextView lastNameField;
    @ViewById
    TextView inviteText;
    @ViewById
    Button accept;
    @ViewById
    Button delete;

    User user;
    InviteData inviteData;

    OnInviteListCommunicationListener listener;
    //Context context;

    public InviteListItemView(Context context) {
        super(context);
        //this.context = context;
    }

    public void bind(InviteData inviteData){
        firstNameField.setText(inviteData.fromUser);
        inviteText.setText(inviteData.text);

    }


/*
    @Click
    void acceptClicked() {
        if (inviteData.groupId == null) { //to do znajomych

        } else  //to do grupy
        {

        }
    }
    */

    @Click
        void deleteClicked() {
        listener.onDeleteInviteSuccess();

    }


    public interface OnInviteListCommunicationListener {
        void onDeleteInviteSuccess();

    }

}
