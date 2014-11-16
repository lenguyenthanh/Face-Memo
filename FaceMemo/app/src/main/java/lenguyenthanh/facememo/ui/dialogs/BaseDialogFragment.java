package lenguyenthanh.facememo.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.ui.base.BaseActivity;

/**
 * Created by lenguyenthanh on 11/15/14.
 */

public class BaseDialogFragment extends DialogFragment{
    private IDialogCallback mDialogCallback;

    protected void clickCallBack(int aViewId){
        dismiss();
        if (mDialogCallback != null){
            mDialogCallback.onClick(this, aViewId);
        }
    }

    public void setDialogCallback(final IDialogCallback aDialogCallback){
        mDialogCallback = aDialogCallback;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        customizeDialog(dialog);
        return dialog;
    }

    protected void customizeDialog(Dialog dialog){
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFade;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState){

    }

    public BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
    }

    public static interface IDialogCallback{
        public void onClick(DialogFragment dialog, int id);
    }

}

