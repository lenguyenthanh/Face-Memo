package lenguyenthanh.facememo.ui.adapters;

import android.widget.BaseAdapter;

import de.greenrobot.dao.query.LazyList;

/**
 * @author lenguyenthanh on 7/17/14.
 */
abstract public class LazyListAdapter<T> extends BaseAdapter {
    protected LazyList<T> lazyList = null;
    public LazyListAdapter() {

    }

    public LazyListAdapter(LazyList<T> initialList) {
        lazyList = initialList;
    }

    public void replaceLazyList(LazyList<T> newList) {
        if(lazyList != null) {
            lazyList.close();
        }
        lazyList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lazyList == null ? 0 : lazyList.size();
    }

    @Override
    public T getItem(int i) {
        return lazyList == null ? null : lazyList.get(i);
    }

    public void close() {
        if(lazyList != null) {
            lazyList.close();
            lazyList = null;
        }
    }
}