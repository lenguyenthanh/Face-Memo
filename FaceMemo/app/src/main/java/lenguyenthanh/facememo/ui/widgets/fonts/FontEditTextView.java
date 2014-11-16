package lenguyenthanh.facememo.ui.widgets.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import lenguyenthanh.facememo.R;


/**
 * @author lenguyenthanh on 7/15/14.
 */

public class FontEditTextView extends EditText{

    public FontEditTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public FontEditTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);

        int font = a.getInt(R.styleable.FontTextView_font, 0);
        switch(font){
            case 1:
                setTypeface(FontFactory.getInstance().getTypeface(FontFactory.BOLD));
                break;
            case 2:
                setTypeface(FontFactory.getInstance().getTypeface(FontFactory.BOLD_ITALIC));
                break;
            case 3:
                setTypeface(FontFactory.getInstance().getTypeface(FontFactory.LIGHT));
                break;
            case 4:
                setTypeface(FontFactory.getInstance().getTypeface(FontFactory.LIGHT_ITALIC));
                break;
            default:
                break;
        }
        a.recycle();
    }
}
