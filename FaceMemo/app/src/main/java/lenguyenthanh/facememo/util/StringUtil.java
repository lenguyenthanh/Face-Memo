package lenguyenthanh.facememo.util;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public final class StringUtil {
    private StringUtil() {
        // No instances.
    }

    public static boolean isEmpty(CharSequence string) {
        return (string == null || string.toString().trim().length() == 0);
    }

    public static String valueOrDefault(String string, String defaultString) {
        return isEmpty(string) ? defaultString : string;
    }

    public static String truncateAt(String string, int length) {
        return string.length() > length ? string.substring(0, length) : string;
    }

    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static void setText(TextView textView, String text){
        if(!isEmpty(text)){
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }else{
            textView.setVisibility(View.GONE);
        }
    }

    public static boolean isEmpty(final EditText aEditText) {
        return StringUtil.isEmpty(aEditText.getText());
    }

    public static String getImageName(String imageName){
        String[] splits = imageName.split("/");
        String name = splits[splits.length - 1];
        return name.split("\\.")[0];
    }
}
