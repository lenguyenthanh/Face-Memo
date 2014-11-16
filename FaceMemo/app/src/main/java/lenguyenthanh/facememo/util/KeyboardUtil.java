package lenguyenthanh.facememo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author lenguyenthanh on 7/17/14.
 */
public class KeyboardUtil {
    @SuppressLint("Recycle")
    public static void startEditWithKeyboard(final EditText aText) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                aText.requestFocus();
                aText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                aText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                aText.setSelection(aText.length());
            }
        }, 60);
    }

    public static abstract class KeyboardListener extends ResultReceiver {

        public KeyboardListener() {
            super(new Handler());
        }

        @Override
        protected final void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == InputMethodManager.RESULT_HIDDEN) {
                onHidden();
            }
        }

        protected void onHidden() {
        }
    }

    public static void hideKeyboard(Context aContext, final EditText aText) {
        hideKeyboard(aContext, aText, null);
    }

    public static void hideKeyboard(Context aContext, final EditText aText, KeyboardListener listener) {
        hideKeyboard(aContext, aText.getWindowToken(), listener);
    }

    public static void hideKeyboard(Context aContext, final IBinder aWindowToken, KeyboardListener listener){
        InputMethodManager imm = (InputMethodManager) aContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(aWindowToken, 0, listener);
    }

    public static void toggleKeyboard(Context aContext, final EditText aText) {
        InputMethodManager input = (InputMethodManager) aContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        input.toggleSoftInputFromWindow(aText.getWindowToken(), 0, 0);
    }


}
