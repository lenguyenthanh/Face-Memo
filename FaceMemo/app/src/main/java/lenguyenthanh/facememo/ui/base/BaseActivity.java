package lenguyenthanh.facememo.ui.base;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by lenguyenthanh on 7/14/14.
 */

@SuppressLint("Registered")
public class BaseActivity extends Activity {
    private boolean visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
    }

    protected void setUpActionBar(){
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled());
            actionBar.setDisplayShowHomeEnabled(showAppIcon());
            actionBar.setDisplayShowTitleEnabled(showTitle());
        }
    }

    protected boolean showAppIcon(){
        return false;
    }

    protected boolean showTitle(){
        return true;
    }

    protected boolean isDisplayHomeAsUpEnabled(){
        return true;
    }

    protected void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected void startAndFinishActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visible = false;
    }

    private ProgressDialog mProgressDialog;

    protected void showLoadingDialog(String aMessage) {
        showLoadingDialog("", aMessage);
    }

    public void showLoadingDialog(String aTitle, String aMessage) {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = ProgressDialog.show(this, aTitle, aMessage);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (mProgressDialog != null) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception ignored) {
            }
            mProgressDialog = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}
