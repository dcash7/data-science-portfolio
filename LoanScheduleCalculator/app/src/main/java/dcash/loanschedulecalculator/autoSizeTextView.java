package dcash.loanschedulecalculator;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
//Default Auto text resizing does not auto adjust Ellipsis
//Needs requestLayout() for list views

public class autoSizeTextView extends AppCompatTextView {

    public autoSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEllipsize(TextUtils.TruncateAt.END);
    }

    public autoSizeTextView(Context context) {
        super(context);
        setEllipsize(TextUtils.TruncateAt.END);
    }

    //Recursively measures and resizes text until ideal size is found
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final Layout layout = getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0) {
                final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    final float textSize = getTextSize();
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize - 1));
                    //if (textSize > 30) { //Restrict minimum textsize, will display with trailing ...
                        measure(widthMeasureSpec, heightMeasureSpec);
                    //}
                }
            }
        }
    }
}