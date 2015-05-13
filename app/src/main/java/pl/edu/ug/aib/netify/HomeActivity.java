package pl.edu.ug.aib.netify;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.UserRegistrationData;
import pl.edu.ug.aib.netify.navigationDrawer.DrawerHandler;

@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity {

    @Pref
    UserPreferences_ preferences;

    public final String LOG_TAG = this.getClass().getSimpleName();
    @Bean
    DrawerHandler drawerHandler;

    @AfterViews
    void init() {
        drawerHandler.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerHandler.getDrawerToggle().syncState();
    }

    @OptionsItem(android.R.id.home)
    public boolean drawerToggleSelected(MenuItem item) {
        return drawerHandler.drawerToggleSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }
}
