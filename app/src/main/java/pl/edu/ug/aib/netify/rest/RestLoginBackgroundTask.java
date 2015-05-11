package pl.edu.ug.aib.netify.rest;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import pl.edu.ug.aib.netify.LoginActivity;
import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserRegistrationData;

@EBean
public class RestLoginBackgroundTask {

    @RootContext
    LoginActivity activity;
    @RestService
    NetifyRestClient restClient;


    @Background
    public void login(EmailAndPassword emailAndPassword){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            User user = restClient.login(emailAndPassword);
            publishLoginResult(user, emailAndPassword.password);
        }catch(Exception e){
            publishError(e);
        }
    }
    //register and automatically log in
    @Background
    public void register(UserRegistrationData userRegistrationData){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            User user = restClient.register(userRegistrationData);
            publishLoginResult(user, userRegistrationData.password);
        }catch(Exception e){
            publishError(e);
        }
    }

    @UiThread
    void publishLoginResult(User user, String password){
        activity.onLoginConfirmed(user, password);
    }
    @UiThread
    void publishError(Exception e) {
        activity.showError(e);
    }
}
