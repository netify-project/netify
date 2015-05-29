package pl.edu.ug.aib.netify.itemView;

import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.InviteData;


@EViewGroup(R.layout.invite_list_item)
public class InviteListItemView extends RelativeLayout {


    @ViewById
    TextView fullNameField;

    @ViewById
    TextView inviteText;
    @ViewById
    Button accept;
    @ViewById
    Button delete;

    InviteData inviteData;

    OnInviteListItemViewCommunicationListener listener;

    public InviteListItemView(Context context) {
        super(context);
        try{
            listener = (OnInviteListItemViewCommunicationListener)context;
        }
        catch (ClassCastException e){
            throw new ClassCastException("Context must implement OnInviteListItemViewCommunicationListener");
        }
    }

    public void bind(InviteData inviteData){
        fullNameField.setText(inviteData.fullName);
        if (inviteData.groupId==null)
        {
            inviteText.setText(R.string.friend_invitation);
        }
        else
        {
            inviteText.setText(R.string.group_invitation);
        }
        //set a field
        this.inviteData = inviteData;
    }



    @Click
    void acceptClicked() {
        listener.acceptInvite(inviteData);
    }


    @Click
        void deleteClicked() {
        listener.deleteInvite(inviteData);
    }


    public interface OnInviteListItemViewCommunicationListener {
        public void acceptInvite(InviteData invite);
        public void deleteInvite(InviteData invite);
    }

}
