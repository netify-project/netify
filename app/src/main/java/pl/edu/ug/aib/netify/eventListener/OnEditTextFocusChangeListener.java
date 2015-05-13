package pl.edu.ug.aib.netify.eventListener;

import android.view.View;

import pl.edu.ug.aib.netify.R;

public class OnEditTextFocusChangeListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) v.setBackgroundResource(R.drawable.edittext_focus_style);
        else v.setBackgroundResource(R.drawable.edittext_style);
    }
}
