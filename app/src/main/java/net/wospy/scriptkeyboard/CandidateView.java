package net.wospy.scriptkeyboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by Matan on 30/11/2017.
 */

public class CandidateView extends LinearLayout {
    public CandidateView(ScriptKeyboard context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.candidate_view, this);
    }
}
