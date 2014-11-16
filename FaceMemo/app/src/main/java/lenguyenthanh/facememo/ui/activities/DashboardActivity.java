package lenguyenthanh.facememo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.dao.query.LazyList;
import it.sephiroth.android.library.widget.HListView;
import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.data.database.ContactModel;
import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.data.tasks.SimpleBackgroundTask;
import lenguyenthanh.facememo.ui.Extras;
import lenguyenthanh.facememo.ui.activities.phone.ContactDetailActivity;
import lenguyenthanh.facememo.ui.activities.phone.ContactsActivity;
import lenguyenthanh.facememo.ui.activities.phone.EditContactActivity;
import lenguyenthanh.facememo.ui.adapters.BaseContactAdapter;
import lenguyenthanh.facememo.ui.adapters.HorizontalContactAdapter;
import lenguyenthanh.facememo.ui.base.BaseActivity;
import lenguyenthanh.facememo.ui.widgets.BetterViewAnimator;
import lenguyenthanh.facememo.ui.widgets.CircleImageView;
import lenguyenthanh.facememo.ui.widgets.fonts.FontTextView;

/**
 * Created by lenguyenthanh on 11/15/14.
 */

public class DashboardActivity extends BaseActivity {

    @InjectView(R.id.imgProfile)
    CircleImageView mImgProfile;
    @InjectView(R.id.hListView1)
    HListView mHListView1;
    @InjectView(R.id.btnBooks)
    Button mBtnBooks;
    @InjectView(R.id.content)
    BetterViewAnimator mContent;
    @InjectView(R.id.tvRecent)
    FontTextView mTvRecent;

    private BaseContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.inject(this);
        initListView();
    }

    private void initListView() {
        contactAdapter = new HorizontalContactAdapter(getLayoutInflater());
        mHListView1.setAdapter(contactAdapter);
        mHListView1.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = contactAdapter.getItem(i);
                gotoContactDetail(contact);
            }
        });
    }

    private void gotoContactDetail(Contact contact){
        Intent intent = new Intent(this, ContactDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Extras.EXTRA_CONTACT, contact);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void refreshList() {
        new SimpleBackgroundTask<LazyList<Contact>>(this) {
            @Override
            protected LazyList<Contact> onRun() {
                return ContactModel.getInstance().recentContact(10);
            }

            @Override
            protected void onSuccess(LazyList<Contact> result) {
                if (result.size() != 0) {
                    mContent.setDisplayedChildId(R.id.hListView1);
                    contactAdapter.replaceLazyList(result);
                    mBtnBooks.setVisibility(View.VISIBLE);
                    mTvRecent.setVisibility(View.VISIBLE);
                } else {
                    mContent.setDisplayedChildId(R.id.tvEmptyMessage);

                }
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    @OnClick(R.id.btnBooks)
    public void onBooks() {
        startActivity(ContactsActivity.class);
    }

    @OnClick(R.id.imgProfile)
    public void addNewFace(){
        startActivity(EditContactActivity.class);
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected boolean showAppIcon() {
        return true;
    }
}
