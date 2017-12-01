package net.wospy.scriptkeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Languages extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        final SharedPreferences prefs = this.getSharedPreferences(
                "net.wospy.scriptkeyboard", Context.MODE_PRIVATE);
        CheckBox heb =((CheckBox)findViewById(R.id.chkHebrew));
        CheckBox rus =((CheckBox)findViewById(R.id.chkRussian));

        heb.setChecked(prefs.getBoolean(LanguageHandler.PREFIX + LanguageHandler.HEBREW,false));
        rus.setChecked(prefs.getBoolean(LanguageHandler.PREFIX + LanguageHandler.RUSSIAN,false));

        heb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean(LanguageHandler.PREFIX + LanguageHandler.HEBREW,isChecked).apply();
                LanguageHandler.invalidateLanguages();
            }
        });
        rus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean(LanguageHandler.PREFIX + LanguageHandler.RUSSIAN,isChecked).apply();
                LanguageHandler.invalidateLanguages();
            }
        });

    }
}
