package net.wospy.scriptkeyboard;

/**
 * Created by Vlad on 6/14/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class CustomKeyboard extends KeyboardView {



    public CustomKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

private static final int POPUP_TEXT_SIZE=35;
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(POPUP_TEXT_SIZE);
        paint.setColor(Color.WHITE);

        List<Key> keys = getKeyboard().getKeys();
        for (Key key : keys) {
            if (key.popupCharacters != null) {
                canvas.drawText(key.popupCharacters.toString(), key.x + (key.width / 2), key.y+POPUP_TEXT_SIZE +5/*Padding*/, paint);

            }
        }
    }
    @Override
    protected boolean onLongPress(final Key popupKey) {
        CharSequence options = popupKey.popupCharacters;
        if(popupKey.codes[0] == -4){
            getOnKeyboardActionListener().onKey(10, null);//new line
        return true;
        }
        if(options==null) return false;
        getOnKeyboardActionListener().onKey(options.charAt(0), null);
        if(options.length()>1) {//prints a second char and puts the cursor between the 2, (<here>) for example
            getOnKeyboardActionListener().onKey(options.charAt(1), null);
            getOnKeyboardActionListener().onKey(-99, null);
        }
        return true;
    }

    PopupWindow pw;
    public void setLoadingScript(final ScriptKeyboard.AsyncScript asyncScript) {
        for (Key key : getKeyboard().getKeys()) {
            if(key.codes[0]==ScriptKeyboard.SCRIPT_START){
                key.icon= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_access_time_white_24dp, null);
                final View v = LayoutInflater.from(getContext())
                        .inflate(R.layout.popup_script_running, new FrameLayout(getContext()));
                v.findViewById(R.id.btnCancelScript).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncScript.cancel(true);
                    }
                });
                pw = new PopupWindow(getContext());
                pw.setContentView(v);
                pw.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                pw.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
                pw.showAtLocation(this, Gravity.CENTER, key.x+key.width, key.y-key.height);
            }
        }
        invalidateAllKeys();
    }

    public void setFinishedScript(){
        for (Key key : getKeyboard().getKeys()) {
            if(key.codes[0]==ScriptKeyboard.SCRIPT_START){
                key.icon= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_circle_outline_white_24dp, null);
                if(pw!=null) pw.dismiss();
            }
        }
        invalidateAllKeys();
    }
}