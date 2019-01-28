package com.ting.base;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.widget.Toast;


import com.ting.view.MyToast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BaseToast {
    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = 0;

    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 1;
    /** @hide */
    @IntDef({LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}


    public static MyToast makeText(@NonNull Context context,
                                 @NonNull CharSequence text, @Duration int duration) {
        MyToast result = new MyToast(context);
        result.setText(text);
        result.setDuration(duration);
        return result;
    }



    public static MyToast makeText(@NonNull Context context,
                                   @NonNull CharSequence text) {
        MyToast result = new MyToast(context);
        result.setText(text);
        result.setDuration(Toast.LENGTH_SHORT);
        return result;
    }
}
