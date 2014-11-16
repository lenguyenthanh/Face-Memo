package lenguyenthanh.facememo.ui.activities.phone;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.ui.Extras;
import lenguyenthanh.facememo.ui.base.SimpleSinglePaneActivity;
import lenguyenthanh.facememo.ui.fragments.BaseContactFragment;
import lenguyenthanh.facememo.ui.fragments.EditContactFragment;
import lenguyenthanh.facememo.util.StringUtil;

/**
 * Created by lenguyenthanh on 11/15/14.
 */
public class EditContactActivity extends SimpleSinglePaneActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(Extras.EXTRA_TITLE);
        if(!StringUtil.isEmpty(title)){
            setTitle(title);
        }else{
            setTitle("New Face");
        }
    }

    @Override
    protected Fragment onCreatePane() {
        Contact contact = (Contact) getIntent().getSerializableExtra(Extras.EXTRA_CONTACT);
        return Fragment.instantiate(this, EditContactFragment.class.getName(), BaseContactFragment.prepareData(contact));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getFragment().onActivityResult(requestCode, resultCode, data);
    }
}
