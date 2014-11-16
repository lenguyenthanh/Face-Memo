package lenguyenthanh.facememo.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.data.database.ContactModel;
import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.data.tasks.SimpleBackgroundTask;
import lenguyenthanh.facememo.ui.Extras;
import lenguyenthanh.facememo.ui.activities.phone.ContactDetailActivity;
import lenguyenthanh.facememo.ui.activities.phone.EditContactActivity;
import lenguyenthanh.facememo.ui.adapters.ContactAdapter;
import lenguyenthanh.facememo.ui.base.BaseFragment;
import lenguyenthanh.facememo.ui.widgets.BetterViewAnimator;
import lenguyenthanh.facememo.util.KeyboardUtil;
import timber.log.Timber;

/**
 * Created by lenguyenthanh on 7/14/14.
 */
public class ContactsFragment extends BaseFragment {
    @InjectView(R.id.lvContacts)
    ListView lvContacts;
    @InjectView(R.id.content)
    BetterViewAnimator layoutContent;

    private ContactAdapter contactAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.d("onViewCreated");
        ButterKnife.inject(this, view);
//        bindListContacts();
    }

    private void bindListContacts(List<Contact> list) {
        contactAdapter = new ContactAdapter(getActivity(), list);
        lvContacts.setAdapter(contactAdapter);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contactAdapter.getItem(position);
                gotoContactDetail(contact);
            }
        });
    }

    private void gotoContactDetail(Contact contact){
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Extras.EXTRA_CONTACT, contact);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void refreshList() {
        new SimpleBackgroundTask<List<Contact>>(getActivity()) {
            @Override
            protected List<Contact> onRun() {
                return ContactModel.getInstance().listContacts();
            }

            @Override
            protected void onSuccess(List<Contact> result) {
                if(result.size() != 0) {
                    bindListContacts(result);
                    layoutContent.setDisplayedChildId(R.id.lvContacts);
                }else{
                    layoutContent.setDisplayedChildId(R.id.tvEmptyMessage);
                }
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contacts_menu, menu);
        setupSearchView(menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected: " + item.getTitle());
        if(item.getItemId() == R.id.action_new){
            startAddContactActivity();
        } else if(item.getItemId() == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAddContactActivity(){
        Intent intent = new Intent(getActivity(), EditContactActivity.class);
        intent.putExtra(Extras.EXTRA_TITLE, "New Face");
        startActivity(intent);
    }


    private void setupSearchView(final Menu menu){
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(final String s){
                contactAdapter.getFilter().filter(s.toString());
                KeyboardUtil.hideKeyboard(getActivity(), searchView.getWindowToken(), null);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String s){
                contactAdapter.getFilter().filter(s.toString());
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){

            @Override
            public boolean onClose(){
                contactAdapter.getFilter().filter("");
                return true;
            }
        });
    }


}