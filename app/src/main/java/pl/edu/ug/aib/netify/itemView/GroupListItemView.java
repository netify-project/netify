package pl.edu.ug.aib.netify.itemView;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.GroupData;

@EViewGroup(R.layout.group_list_item)
public class GroupListItemView extends RelativeLayout {

    @ViewById
    ImageView groupThumbnail;
    @ViewById
    TextView groupName;
    @ViewById
    TextView groupDescription;

    Context context;

    public GroupListItemView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(GroupData groupData){
        Picasso.with(context).load(groupData.thumbnail).placeholder(R.drawable.ic_launcher).into(groupThumbnail);
        groupName.setText(groupData.name);
        groupDescription.setText(groupData.description);
    }
}
