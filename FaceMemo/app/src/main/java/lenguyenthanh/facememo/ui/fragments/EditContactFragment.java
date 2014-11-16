package lenguyenthanh.facememo.ui.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import lenguyenthanh.facememo.R;
import lenguyenthanh.facememo.data.database.ContactModel;
import lenguyenthanh.facememo.data.tasks.SimpleBackgroundTask;
import lenguyenthanh.facememo.ui.dialogs.BaseDialogFragment;
import lenguyenthanh.facememo.ui.dialogs.ChoosePhotoDialog;
import lenguyenthanh.facememo.util.ContactBuilder;
import lenguyenthanh.facememo.util.ImageChooserIml;
import lenguyenthanh.facememo.util.ImageUtils;
import lenguyenthanh.facememo.util.StorageUtil;
import lenguyenthanh.facememo.util.StringUtil;
import timber.log.Timber;

/**
 * Created by lenguyenthanh on 11/15/14.
 */

public class EditContactFragment extends BaseContactFragment {

    private ImageChooserIml imageChooserListener;
    private String tempFile;
    private ContactHelper contactHelper = new ContactHelper();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initContactHelper();
    }

    private void initContactHelper(){
        if(!StringUtil.isEmpty(contact.getNumber())){
            contactHelper.hasNumber = true;
        }
        if(!StringUtil.isEmpty(contact.getPhoto())){
            contactHelper.hasPhoto = true;
        }
        if(!StringUtil.isEmpty(contact.getNote())){
            contactHelper.hasNote = true;
        }
    }

    @Override
    protected void bindData() {
        mLayoutDetail.setVisibility(View.GONE);
        mLayoutEdit.setVisibility(View.VISIBLE);
        bindContact();
    }

    private void bindContact() {
        mEtFirstName.setText(contact.getFirstName());
        mEtLastName.setText(contact.getLastName());
        mEtNote.setText(contact.getNote());
        mEtPhone.setText(contact.getNumber());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_edit).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    private void save(){
        if(valid()) {
            showLoadingDialog("Saving...");
            contact.setNote(StringUtil.getText(mEtNote));
            contact.setNumber(StringUtil.getText(mEtPhone));
            contact.setFirstName(StringUtil.getText(mEtFirstName));
            contact.setLastName(StringUtil.getText(mEtLastName));

            new SimpleBackgroundTask<Boolean>(getActivity()) {
                @Override
                protected Boolean onRun() {
                    try {
                        String photoPath = saveImage();
                        if (!StringUtil.isEmpty(photoPath)) {
                            contact.setPhoto(photoPath);
                        }
                        ContactBuilder contactBuilder = new ContactBuilder(getActivity(), contact, contactHelper);
                        int contactUri = contactBuilder.build();
                        if(contactUri != -1) {
                            contact.setRawContactId(contactUri);
                            save2Db();
                            return true;
                        }else{
                            return false;
                        }
                    } catch (IOException e) {
                        Timber.e(e, "Cannot save image to folder.");

                    }
                    return false;
                }

                @Override
                protected void onSuccess(Boolean result) {
                    dismissLoadingDialog();
                    if(result){
                        getActivity().finish();
                    }else{
                        Crouton.showText(getActivity(), "Cannot save contact. Please try again.", Style.ALERT);
                    }
                }
            }.execute();
        }
    }

    private boolean valid(){
        if(StringUtil.isEmpty(mEtFirstName)){
            mEtFirstName.setError("Please enter first name");
            return false;
        }

        if(StringUtil.isEmpty(mEtLastName)){
            mEtLastName.setError("Please enter first name");
            return false;
        }

        return true;
    }

    private void save2Db(){
        ContactModel.getInstance().insertOrReplace(contact);
    }

    private void init() {
        imageChooserListener = new ImageChooserIml(getActivity()) {
            @Override
            public void onImageChosen(final ChosenImage image) {
                if (image != null) {
                    Timber.d("Get Image Success: " + image.getFileThumbnail());
                    tempFile = image.getFileThumbnail();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showImage(image.getFileThumbnail(), mImgProfile);
                            generateBlurredImage(image.getFileThumbnail());
                        }
                    });
                }
            }

            @Override
            public void onError(final String s) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Crouton.showText(getActivity(), s, Style.ALERT);
                    }
                });
            }
        };
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @OnClick(R.id.imgProfile)
    public void captureImage() {
        ChoosePhotoDialog.showDialog(getFragmentManager(), new BaseDialogFragment.IDialogCallback() {
            @Override
            public void onClick(DialogFragment dialog, int id) {
                int choosePhotoType = ChooserType.REQUEST_CAPTURE_PICTURE;
                switch (id){
                    case R.id.tvChooseExist:
                        choosePhotoType = ChooserType.REQUEST_PICK_PICTURE;
                        break;
                    case R.id.tvTakePhoto:
                        choosePhotoType = ChooserType.REQUEST_CAPTURE_PICTURE;
                        break;
                }
                imageChooserListener.chooseImage(choosePhotoType);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageChooserListener.onActivityResult(requestCode, resultCode, data);
    }

    private String saveImage() throws IOException {
        if(!StringUtil.isEmpty(tempFile)){
            String photoPath = createPhotoFile();
            File source = new File(tempFile);
            File target = new File(photoPath);
            ImageUtils.copy(source, target);
            return photoPath;
        }
        return "";
    }

    private String createPhotoFile(){
        String name = UUID.randomUUID().toString();
        return StorageUtil.getNormalRootDirectory(getActivity()) + name + ".jpg";
    }

    public static class ContactHelper {
        public boolean hasPhoto = false;
        public boolean hasNumber = false;
        public boolean hasNote = false;
    }
}
