package net.wospy.scriptkeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Matan on 27/11/2017.
 */

public class LanguageHandler {
    public static final String PREFIX = "Lang_";
    public static final String HEBREW = "HEBREW";
    public static final String RUSSIAN = "RUSSIAN";
    private final Context ctx;
    private ArrayList<Keyboard> keyboards = new ArrayList<>();
    private int index=0;
    private static boolean invalidLanguages=false;

    public LanguageHandler(Context ctx) {
        this.ctx=ctx;
        initLanguages();
    }

    public void initLanguages(){
        keyboards.clear();
        SharedPreferences prefs = ctx.getSharedPreferences(
                "net.wospy.scriptkeyboard", Context.MODE_PRIVATE);
        index=0;//for those who remove languages.
        keyboards.add(new Keyboard(ctx, R.xml.qwerty_en));
        if(prefs.getBoolean(PREFIX + HEBREW,false)) keyboards.add(new Keyboard(ctx, R.xml.qwerty_he));
        if(prefs.getBoolean(PREFIX + RUSSIAN,false)) keyboards.add(new Keyboard(ctx, R.xml.qwerty_ru));
        invalidLanguages=false;
    }
    public static void invalidateLanguages(){
        invalidLanguages=true;
    }

     Keyboard next() {
         if(invalidLanguages) initLanguages();
        if(index+1 >= keyboards.size()){index =-1;} //-1 because doing index++;
        index++;
        return keyboards.get(index);
    }
     Keyboard getCurrentKeyboard(){
        return keyboards.get(index);
    }

}
