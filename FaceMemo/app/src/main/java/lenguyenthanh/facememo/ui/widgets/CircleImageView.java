package lenguyenthanh.facememo.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author lenguyenthanh on 9/12/14.
 */
public class CircleImageView extends ImageView {

    public CircleImageView(final Context context, final AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public void setImageBitmap(final Bitmap bm){
        CircleDrawable bitmapDrawable = new CircleDrawable(bm);
        super.setImageDrawable(bitmapDrawable);
    }

    @Override
    public void setImageDrawable(final Drawable drawable){
        if(drawable instanceof CircleDrawable) {
            super.setImageDrawable(drawable);
        } else if(drawable instanceof BitmapDrawable){
            Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
            CircleDrawable bitmapDrawable = new CircleDrawable(bm);
            super.setImageDrawable(bitmapDrawable);
        }
    }
}
