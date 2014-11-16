package lenguyenthanh.facememo.ui.adapters;

import android.view.LayoutInflater;

import lenguyenthanh.facememo.R;

/**
 * Created by lenguyenthanh on 11/15/14.
 */
public class NormalContactAdapter extends  BaseContactAdapter {
    public NormalContactAdapter(LayoutInflater layoutInflater) {
        super(layoutInflater);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_contact_item;
    }
}
