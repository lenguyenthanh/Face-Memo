package lenguyenthanh.facememo.ui.dialogs;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lenguyenthanh.facememo.R;

/**
 * Created by lenguyenthanh on 11/15/14.
 */

public class ChoosePhotoDialog extends BaseDialogFragment implements View.OnClickListener{

    public static void showDialog(FragmentManager aManager, IDialogCallback aCallback){
        FragmentTransaction ft = aManager.beginTransaction();
        ChoosePhotoDialog dialog = new ChoosePhotoDialog();
        dialog.setDialogCallback(aCallback);
        dialog.show(ft, ChoosePhotoDialog.class.getName());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
        return inflater.inflate(R.layout.dialog_choose_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tvChooseExist).setOnClickListener(this);
        view.findViewById(R.id.tvTakePhoto).setOnClickListener(this);
        setCancelable(true);
    }

    @Override
    public void onClick(final View v){
        clickCallBack(v.getId());
    }
}
