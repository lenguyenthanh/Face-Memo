package lenguyenthanh.facememo.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.data.database.ContactModel;
import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.data.tasks.SimpleBackgroundTask;
import lenguyenthanh.facememo.ui.Extras;
import lenguyenthanh.facememo.ui.activities.phone.EditContactActivity;
import lenguyenthanh.facememo.util.StringUtil;
import timber.log.Timber;

/**
 * Created by lenguyenthanh on 11/15/14.
 */
public class ContactDetailFragment extends BaseContactFragment {
    @Override
    protected void bindData() {
        mLayoutDetail.setVisibility(View.VISIBLE);
        mLayoutEdit.setVisibility(View.GONE);
        bindContact();
    }

    private void bindContact() {
        mTvName.setText(contact.getName());
        StringUtil.setText(mTvNote, contact.getNote());
        StringUtil.setText(mTvPhone, contact.getNumber());
        if(!StringUtil.isEmpty(contact.getPhoto())){
            showImage(contact.getPhoto(), mImgProfile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(contact.getId() != null){
            new SimpleBackgroundTask<Contact>(getActivity()) {
                @Override
                protected Contact onRun() {
                    return ContactModel.getInstance().load(contact.getId());
                }

                @Override
                protected void onSuccess(Contact result) {
                    if(!contact.equals(result)) {
                        contact = result;
                        refreshData();
                    }
                }
            }.execute();
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_save).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected: " + item.getTitle());
        if(item.getItemId() == R.id.action_edit){
            editContact();
        }
        return super.onOptionsItemSelected(item);
    }


    private void editContact(){
        Intent intent = new Intent(getActivity(), EditContactActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Extras.EXTRA_CONTACT, contact);
        intent.putExtras(bundle);
        intent.putExtra(Extras.EXTRA_TITLE, "Edit");
        startActivity(intent);
    }
}
