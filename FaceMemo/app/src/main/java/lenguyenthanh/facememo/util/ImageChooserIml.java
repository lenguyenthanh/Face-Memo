package lenguyenthanh.facememo.util;

import android.app.Activity;
import android.content.Intent;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

/**
 * Created by lenguyenthanh on 11/14/14.
 */
public abstract class ImageChooserIml implements ImageChooserListener {

    private static final String DEFAULT_FOLDER = "facememo";

    private Activity activity;
    private ImageChooserManager imageChooserManager;

    private String filePath;

    private int chooserType;

    protected ImageChooserIml(Activity activity) {
        this.activity = activity;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(activity, chooserType,
               DEFAULT_FOLDER, true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    public void chooseImage(int chooserType) {
        this.chooserType = chooserType;
        imageChooserManager = new ImageChooserManager(activity,
                this.chooserType, DEFAULT_FOLDER, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getChooserType() {
        return chooserType;
    }

    public String getFilePath() {
        return filePath;
    }
}
