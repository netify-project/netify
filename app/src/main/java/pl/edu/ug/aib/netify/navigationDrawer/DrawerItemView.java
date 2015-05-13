package pl.edu.ug.aib.netify.navigationDrawer;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;

@EViewGroup(R.layout.drawer_list_item)
public class DrawerItemView extends LinearLayout {
    @ViewById
    ImageView icon;
    @ViewById
    TextView name;

    public DrawerItemView(Context context) {
        super(context);
    }

    public void bind(DrawerItem drawerItem) {
        icon.setImageResource(drawerItem.getIconResId());
        name.setText(drawerItem.getTitleResId());
    }

}
