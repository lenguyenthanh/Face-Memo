package lenguyenthanh.facememo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import lenguyenthanh.cropimagelibrary.CropImageIntentBuilder;

/**
 * Created by lenguyenthanh on 11/15/14.
 */
public class ActionUtil {
    public static Intent requestCrop(Context context, Uri from, Uri to){
        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, to);
        cropImage.setSourceImage(from);
        return cropImage.getIntent(context);
    }
}
