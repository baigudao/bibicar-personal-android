package acplibrary;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.wiserz.pbibi.R;

/**
 * Created by jackie on 2017/9/27 17:14.
 * QQ : 971060378
 * Used as : 加载进度对话框
 */
public abstract class ACProgressBaseDialog extends Dialog {

    protected ACProgressBaseDialog(Context context) {
        super(context, R.style.ACPLDialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setMessage(String strTitle) {

    }

    protected int getMinimumSideOfScreen(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            return Math.min(size.x, size.y);
        } else {
            return Math.min(display.getWidth(), display.getHeight());
        }
    }
}
