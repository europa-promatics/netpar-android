package co.netpar.Widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by promatics on 12/2/2017.
 */
public class DevNagiriTextView extends android.support.v7.widget.AppCompatTextView {

    public DevNagiriTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DevNagiriTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DevNagiriTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "devnagari_new_normal.ttf");
        setTypeface(tf ,1);
    }

}