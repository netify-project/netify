package pl.edu.ug.aib.netify.navigationDrawer;

import android.support.v4.app.Fragment;

public class DrawerItem {

    private int titleResId;
    private int iconResId;
    private Class<? extends Fragment> fragmentClass;

    public DrawerItem(int titleResId, int iconResId, Class<? extends Fragment>
            fragmentClass) {
        this.titleResId = titleResId;
        this.iconResId = iconResId;
        this.fragmentClass = fragmentClass;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

}
