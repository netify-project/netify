package pl.edu.ug.aib.netify.fragment;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import pl.edu.ug.aib.netify.GroupActivity_;
import pl.edu.ug.aib.netify.R;

@EFragment(R.layout.fragment_mygroups)
public class MyGroupsFragment extends Fragment {

    @Click
    void temporaryButtonClicked(){
        GroupActivity_.intent(this).start();
    }
}
