package pl.edu.ug.aib.netify;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.sharedpreferences.Pref;

import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserRegistrationData;
import pl.edu.ug.aib.netify.fragment.IntroFragment;
import pl.edu.ug.aib.netify.fragment.IntroFragment_;
import pl.edu.ug.aib.netify.fragment.LoginFragment;
import pl.edu.ug.aib.netify.fragment.LoginFragment_;
import pl.edu.ug.aib.netify.fragment.ProgressBarFragment_;
import pl.edu.ug.aib.netify.fragment.RegisterFragment;
import pl.edu.ug.aib.netify.fragment.RegisterFragment_;
import pl.edu.ug.aib.netify.rest.RestLoginBackgroundTask;

@EActivity(R.layout.activity_login)
public class LoginActivity extends ActionBarActivity
implements IntroFragment.OnIntroFragmentCommunicationListener,
        LoginFragment.OnLoginFragmentCommunicationListener,
        RegisterFragment.OnRegisterFragmentCommunicationListener{

    private static final String INTRO_FRAGMENT_TAG = "intro";
    @Pref
    UserPreferences_ preferences;
    @Bean
    @NonConfigurationInstance
    RestLoginBackgroundTask restBackgroundTask;

    FragmentManager fragmentManager;
    ProgressBarFragment_ progressBarFragment;

    @AfterViews
    void init(){
        //load initial fragment
        fragmentManager = this.getSupportFragmentManager();
        IntroFragment introFragment = new IntroFragment_();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                .add(R.id.contentFrame, introFragment, INTRO_FRAGMENT_TAG)
                .commit();
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.email = preferences.email().get();
        emailAndPassword.password = preferences.password().get();
        //if user credentials already saved in preferences, try to log in
        if(!emailAndPassword.email.isEmpty() && !emailAndPassword.password.isEmpty()){
            onLogin(emailAndPassword);
        }


    }
    //IntroFragment communication
    @Override
    public void onLoginClicked() {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.contentFrame, new LoginFragment_())
                .addToBackStack(null)
                .commit();
    }
    //IntroFragment communication
    @Override
    public void onRegisterClicked() {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.contentFrame, new RegisterFragment_())
                .addToBackStack(null)
                .commit();
    }
    //LoginFragment communication
    @Override
    public void onLogin(EmailAndPassword emailAndPassword) {
        restBackgroundTask.login(emailAndPassword);
        replaceWithProgressBarFragment();
    }
    //RegisterFragment communication
    @Override
    public void onRegister(UserRegistrationData userRegistrationData) {
        restBackgroundTask.register(userRegistrationData);
        replaceWithProgressBarFragment();
    }

    //RestLoginBackgroundTask communication
    public void onLoginConfirmed(User user, String password){
        //updates shared preferences with user data
        preferences.id().put(user.id);
        preferences.sessionId().put(user.sessionId);
        preferences.firstName().put(user.firstName);
        preferences.lastName().put(user.lastName);
        preferences.displayName().put(user.displayName);
        preferences.email().put(user.email);
        preferences.password().put(password);
        //starts main activity
        HomeActivity_.intent(this).start();
        //GroupActivity_.intent(this).start();
        //removes activity from back stack
        finish();
    }
    //Invoked after login/register exception in rest background task
    public void showError(Exception e){
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        removeProgressBarFragment();
    }
    //Shows progress bar fragment
    private void replaceWithProgressBarFragment(){
        progressBarFragment = new ProgressBarFragment_();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                .replace(R.id.contentFrame, progressBarFragment)
                .addToBackStack(null)
                .commit();
    }
    //Removes progress bar from fragment manager and from backstack
    private void removeProgressBarFragment(){
        fragmentManager.beginTransaction()
                .remove(progressBarFragment)
                .commit();
        fragmentManager.popBackStack();
    }
}
