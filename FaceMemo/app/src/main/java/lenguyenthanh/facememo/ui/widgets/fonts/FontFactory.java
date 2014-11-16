package lenguyenthanh.facememo.ui.widgets.fonts;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenguyenthanh on 7/15/14.
 */


public class FontFactory {

    public static final String BOLD = "bold.ttf";
    public static final String BOLD_ITALIC = "bolditalic.ttf";
    public static final String LIGHT = "light.ttf";
    public static final String LIGHT_ITALIC = "lightitalic.ttf";

    private static final String FONT_FOLDER = "fonts";
    private static final FontFactory ourInstance = new FontFactory();

    private final Map<String, Typeface> mFonts = new HashMap<String, Typeface>();

    public static FontFactory getInstance(){
        return ourInstance;
    }

    private FontFactory(){
    }

    public void initialize(Context aContext){
        AssetManager assetManager = aContext.getAssets();
        try{
            String[] fontses = assetManager.list(FONT_FOLDER);
            for(String font : fontses){
                mFonts.put(font, Typeface.createFromAsset(aContext.getAssets(), FONT_FOLDER + "/" + font));
            }
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public Typeface getTypeface(String aFont){
        return mFonts.get(aFont);
    }

    public void setTypeface(ViewGroup aContainer, String aFont){
        Typeface typeface = getTypeface(aFont);
        if(typeface == null){
            return;
        }

        for(int i = 0; i < aContainer.getChildCount(); ++i){
            final View mChild = aContainer.getChildAt(i);
            if(mChild instanceof TextView){
                ((TextView) mChild).setTypeface(typeface);
            } else if(mChild instanceof ViewGroup){
                setTypeface((ViewGroup) mChild, aFont);
            }
        }
    }
}
