package lenguyenthanh.facememo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lenguyenthanh.facememo.FaceMemoApplication;
import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.ui.widgets.CircleImageView;
import lenguyenthanh.facememo.ui.widgets.fonts.FontTextView;
import lenguyenthanh.facememo.util.StringUtil;
import timber.log.Timber;

/**
 * @author lenguyenthanh on 7/17/14.
 */
public abstract class BaseContactAdapter extends LazyListAdapter<Contact>{
    private final LayoutInflater layoutInflater;

    public BaseContactAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(getLayoutId(), viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = ViewHolder.getFromView(view);
        }
        holder.render(getItem(i));
        return view;
    }

    abstract protected int getLayoutId();

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
}