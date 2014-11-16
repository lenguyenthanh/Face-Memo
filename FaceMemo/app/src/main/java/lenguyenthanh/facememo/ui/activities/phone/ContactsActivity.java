package lenguyenthanh.facememo.ui.activities.phone;

import android.app.Fragment;

import lenguyenthanh.facememo.ui.base.SimpleSinglePaneActivity;
import lenguyenthanh.facememo.ui.fragments.ContactsFragment;

/**
 * Created by lenguyenthanh on 11/15/14.
 */
public class ContactsActivity extends SimpleSinglePaneActivity {

    @Override
    protected Fragment onCreatePane() {
        return Fragment.instantiate(this, ContactsFragment.class.getName(), null);
    }


}
