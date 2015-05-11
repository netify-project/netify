package pl.edu.ug.aib.netify;

import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.UserRegistrationData;

@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity {

    @Pref
    UserPreferences_ preferences;

    @AfterViews
    void init(){
        Toast.makeText(this, preferences.email().get() + "\n SessionId: " + preferences.sessionId().get(), Toast.LENGTH_SHORT).show();

    }
}
