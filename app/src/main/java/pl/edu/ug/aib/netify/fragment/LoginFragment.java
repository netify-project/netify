package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.eventListener.OnEditTextFocusChangeListener;

@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment {

    @ViewById
    EditText emailField;
    @ViewById
    EditText passwordField;
    //interface for communication with activity
    OnLoginFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        OnEditTextFocusChangeListener onEditTextFocusChangeListener = new OnEditTextFocusChangeListener();
        emailField.setOnFocusChangeListener( onEditTextFocusChangeListener);
        passwordField.setOnFocusChangeListener(onEditTextFocusChangeListener);
        emailField.requestFocus();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnLoginFragmentCommunicationListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnLoginFragmentCommunicationListener");
        }
    }

    @Click
    void loginButtonClicked(){
        //email and password verification
        if(emailField.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.email_missing), Toast.LENGTH_LONG).show();
            return;
        }
        if(passwordField.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.password_missing), Toast.LENGTH_LONG).show();
            return;
        }
        //creates object with login data and passes to activity
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.email = emailField.getText().toString().trim();
        emailAndPassword.password = passwordField.getText().toString().trim();
        listener.onLogin(emailAndPassword);
    }

    public interface OnLoginFragmentCommunicationListener {
        void onLogin(EmailAndPassword emailAndPassword);
    }

}
