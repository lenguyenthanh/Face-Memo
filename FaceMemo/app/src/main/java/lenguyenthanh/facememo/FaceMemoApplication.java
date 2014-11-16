package lenguyenthanh.facememo;

import android.app.Application;
import android.net.Uri;
import com.squareup.picasso.Picasso;
import lenguyenthanh.facememo.ui.widgets.fonts.FontFactory;
import timber.log.Timber;

/**
 * Created by lenguyenthanh on 11/14/14.
 */
public class FaceMemoApplication extends Application{
    private static FaceMemoApplication instance;
    private Picasso picasso;

    public FaceMemoApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configure();
    }

    private void configure(){
        configureMetadata();
        configureFont();
        configurePicasso();
    }

    private void configureMetadata() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // enable error tracking (ex: Crashlytics)
        }
    }

    private void configurePicasso(){
        picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e(exception, "Failed to load image: %s", uri);
                    }
                })
                .build();
    }

    private void configureFont(){
        FontFactory.getInstance().initialize(this);
    }

    public static FaceMemoApplication getInstance() {
        return instance;
    }

    public Picasso getPicasso(){
        return picasso;
    }
}
