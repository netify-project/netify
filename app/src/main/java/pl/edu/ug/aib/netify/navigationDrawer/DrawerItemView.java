package pl.edu.ug.aib.netify.navigationDrawer;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.fragment.LogoutFragment_;

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
        //hack to adjust logout icon until we have a better icon
        if(drawerItem.getFragmentClass().equals(LogoutFragment_.class)){
            ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
            layoutParams.height = 42;
            //layoutParams.width = 42;
            icon.setLayoutParams(layoutParams);
        }
    }



}
