package lenguyenthanh.facememo.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

import lenguyenthanh.facememo.FaceMemoApplication;

/**
 * Created by lenguyenthanh on 7/14/14.
 */
public class BaseFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    protected BaseActivity getBaseActivity(){
        return (BaseActivity) getActivity();
    }

    public void showLoadingDialog(String aMessage) {
        showLoadingDialog("", aMessage);
    }

    public void showLoadingDialog(String aTitle, String aMessage) {
        getBaseActivity().showLoadingDialog(aTitle, aMessage);
    }

    protected void dismissLoadingDialog() {
        getBaseActivity().dismissLoadingDialog();
    }

    protected void showImage(String filePath, ImageView target){
        FaceMemoApplication.getInstance().getPicasso().load(new File(filePath)).into(target);
    }
}
