package lenguyenthanh.facememo.ui.activities.tablet;

import android.app.Fragment;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.ui.base.BaseActivity;

/**
 * Created by lenguyenthanh on 11/16/14.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
