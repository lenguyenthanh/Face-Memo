package lenguyenthanh.facememo.ui.activities.phone;

import android.app.Fragment;
import android.view.Menu;

import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.ui.Extras;
import lenguyenthanh.facememo.ui.base.SimpleSinglePaneActivity;
import lenguyenthanh.facememo.ui.fragments.BaseContactFragment;
import lenguyenthanh.facememo.ui.fragments.ContactDetailFragment;

/**
 * Created by lenguyenthanh on 11/15/14.
 */
public class ContactDetailActivity extends SimpleSinglePaneActivity{

    @Override
    protected Fragment onCreatePane() {
        Contact contact = (Contact) getIntent().getSerializableExtra(Extras.EXTRA_CONTACT);
        return Fragment.instantiate(this, ContactDetailFragment.class.getName(), BaseContactFragment.prepareData(contact));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
