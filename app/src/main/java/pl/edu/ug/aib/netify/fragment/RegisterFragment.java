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
import pl.edu.ug.aib.netify.data.UserRegistrationData;
import pl.edu.ug.aib.netify.eventListener.OnEditTextFocusChangeListener;

@EFragment(R.layout.fragment_register)
public class RegisterFragment extends Fragment {

    @ViewById
    EditText emailField;
    @ViewById
    EditText passwordField;
    @ViewById
    EditText confirmPasswordField;
    @ViewById
    EditText firstNameField;
    @ViewById
    EditText lastNameField;
    @ViewById
    EditText displayNameField;
    //interface for communication with activity
    OnRegisterFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        OnEditTextFocusChangeListener onEditTextFocusChangeListener = new OnEditTextFocusChangeListener();
        emailField.setOnFocusChangeListener(onEditTextFocusChangeListener);
        passwordField.setOnFocusChangeListener(onEditTextFocusChangeListener);
        confirmPasswordField.setOnFocusChangeListener(onEditTextFocusChangeListener);
        firstNameField.setOnFocusChangeListener(onEditTextFocusChangeListener);
        lastNameField.setOnFocusChangeListener(onEditTextFocusChangeListener);
        displayNameField.setOnFocusChangeListener(onEditTextFocusChangeListener);
        emailField.requestFocus();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnRegisterFragmentCommunicationListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnRegisterFragmentCommunicationListener");
        }
    }

    @Click
    void registerButtonClicked(){
        //registration data verification
        if(emailField.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.email_missing), Toast.LENGTH_LONG).show();
            return;
        }
        //password verification
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();
        if(password.isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.password_missing), Toast.LENGTH_LONG).show();
            return;
        }
        if(confirmPassword.isEmpty()){
            Toast.makeText(getActivity(), getString(R.string.password_confirmation_missing), Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.equals(confirmPassword)) {
            Toast.makeText(getActivity(), getString(R.string.password_confirmation_mismatch), Toast.LENGTH_LONG).show();
            return;
        }
        //creates object with registration data and passes to activity
        UserRegistrationData userRegistrationData = new UserRegistrationData();
        userRegistrationData.email = emailField.getText().toString().trim();
        userRegistrationData.password = password;
        userRegistrationData.firstName = firstNameField.getText().toString().trim();
        userRegistrationData.lastName = lastNameField.getText().toString().trim();
        userRegistrationData.displayName = displayNameField.getText().toString().trim();
        listener.onRegister(userRegistrationData);
    }

    public interface OnRegisterFragmentCommunicationListener {
        void onRegister(UserRegistrationData userRegistrationData);
    }

}
