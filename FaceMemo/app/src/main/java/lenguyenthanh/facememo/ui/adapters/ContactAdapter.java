package lenguyenthanh.facememo.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lenguyenthanh.facememo.FaceMemoApplication;
import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.ui.widgets.fonts.FontTextView;
import lenguyenthanh.facememo.util.StringUtil;

/**
 * Created by lenguyenthanh on 11/16/14.
 */
public class ContactAdapter extends ArrayAdapter<Contact> implements Filterable{
    private List<Contact> list;
    private List<Contact> originalList;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, -1, contacts);
        originalList = new ArrayList<Contact>();
        list = new ArrayList<Contact>();
        originalList.addAll(contacts);
        list = contacts;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.view_contact_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = ViewHolder.getFromView(view);
        }
        holder.render(list.get(i));
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.tvContactName)
        FontTextView mTvContactName;
        @InjectView(R.id.imgProfile)
        ImageView mImgProfile;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public static ViewHolder getFromView(View view) {
            Object tag = view.getTag();
            if (tag instanceof ViewHolder) {
                return (ViewHolder) tag;
            } else {
                return new ViewHolder(view);
            }
        }

        public void render(Contact contact) {
            mTvContactName.setText(contact.getName());
            if(!StringUtil.isEmpty(contact.getPhoto())) {
                FaceMemoApplication.getInstance().getPicasso().load(new File(contact.getPhoto())).into(mImgProfile);
            }else{
                mImgProfile.setImageResource(R.drawable.img_avatar_default);
            }
        }
    }

    @Override
    public Filter getFilter(){
        if(mContactFilter == null){
            mContactFilter = new ContactFilter();
        }
        return mContactFilter;
    }

    private ContactFilter mContactFilter;
    class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(final CharSequence aCharSequence){
            FilterResults results = new FilterResults();
            List<Contact> filteredArrayNames = new ArrayList<Contact>();
            String search = aCharSequence.toString().toLowerCase(Locale.US);
            for(Contact discussionItem : originalList){
                if(discussionItem.getName().toLowerCase(Locale.US).contains(search)){
                    filteredArrayNames.add(discussionItem);
                }
            }
            results.count = filteredArrayNames.size();
            results.values = filteredArrayNames;
            return results;
        }

        @Override
        protected void publishResults(final CharSequence aCharSequence, final FilterResults aFilterResults){
            list.clear();
            list.addAll((List<Contact>) aFilterResults.values);
            notifyDataSetChanged();
        }
    }
}
