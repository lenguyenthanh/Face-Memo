package lenguyenthanh.facememo.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.data.database.ContactModel;
import lenguyenthanh.facememo.data.entities.Contact;
import lenguyenthanh.facememo.ui.Extras;
import lenguyenthanh.facememo.ui.base.BaseFragment;
import lenguyenthanh.facememo.ui.widgets.CircleImageView;
import lenguyenthanh.facememo.ui.widgets.fonts.FontEditTextView;
import lenguyenthanh.facememo.ui.widgets.fonts.FontTextView;
import lenguyenthanh.facememo.util.Blur;
import lenguyenthanh.facememo.util.ImageUtils;
import lenguyenthanh.facememo.util.StorageUtil;
import lenguyenthanh.facememo.util.StringUtil;
import timber.log.Timber;

/**
 * Created by lenguyenthanh on 11/15/14.
 */

public abstract class BaseContactFragment extends BaseFragment {
    protected Contact contact;

    @InjectView(R.id.photoContainer)
    ImageView mPhotoContainer;
    @InjectView(R.id.imgProfile)
    CircleImageView mImgProfile;
    @InjectView(R.id.tvName)
    FontTextView mTvName;
    @InjectView(R.id.tvPhone)
    FontTextView mTvPhone;
    @InjectView(R.id.tvNote)
    FontTextView mTvNote;
    @InjectView(R.id.layoutDetail)
    LinearLayout mLayoutDetail;
    @InjectView(R.id.etFirstName)
    FontEditTextView mEtFirstName;
    @InjectView(R.id.etLastName)
    FontEditTextView mEtLastName;
    @InjectView(R.id.etPhone)
    FontEditTextView mEtPhone;
    @InjectView(R.id.etNote)
    FontEditTextView mEtNote;
    @InjectView(R.id.layoutEdit)
    LinearLayout mLayoutEdit;


    public static Bundle prepareData(Contact contact) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Extras.EXTRA_CONTACT, contact);
        return bundle;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmen_contact_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        refreshData();
    }

    protected void refreshData(){
        bindData();
        if (!StringUtil.isEmpty(contact.getPhoto())) {
            showImage(contact.getPhoto(), mImgProfile);
            generateBlurredImage();
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initContact();
    }

    private void initContact() {
        contact = (Contact)getArguments().getSerializable(Extras.EXTRA_CONTACT);
        if (contact == null){
            contact = new Contact();
        }
    }

    protected abstract void bindData();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    protected void generateBlurredImage() {
        generateBlurredImage(contact.getPhoto());
    }

    protected void generateBlurredImage(final String filePath) {
        Timber.d("generateBlurredImage: " + filePath);
        final File blurredImage = new File(StorageUtil.getBlurrRootDirectory(getActivity()) + StringUtil.getImageName(filePath) + ".png");
        if (!blurredImage.exists()) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    // No image found => let's generate it!
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap image = BitmapFactory.decodeFile(filePath, options);
                    Bitmap newImg = Blur.fastblur(getActivity(), image, 12);
                    ImageUtils.storeImage(newImg, blurredImage);
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            updateBlurrImage(blurredImage);
                        }
                    });

                }
            }).start();
        } else {
            updateBlurrImage(blurredImage);
        }
    }

    private void updateBlurrImage(File blurrImage) {
        Timber.d("updateBlurrImage: " + blurrImage.toString());
        showImage(blurrImage.toString(), mPhotoContainer);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
