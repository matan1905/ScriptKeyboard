package net.wospy.scriptkeyboard;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.util.Arrays;

/**
 * Created by Matan on 13/09/2016.
 */
public class ScriptKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private static final int LANGUAGE_CHANGE = -2;
    private static final int GO_BACK_ONCE = -99;
    private CustomKeyboard kv;

    public static final int SCRIPT_START = -101;
    private static final int SCRIPT_WRAPPER = -420;
    boolean caps = false;
    LanguageHandler languageHandler;
    ScriptProcessor processor = new ScriptProcessor();


    ExtractedTextRequest scriptParams = new ExtractedTextRequest();


    @Override
    public View onCreateInputView() {
        languageHandler = new LanguageHandler(getApplicationContext());
        kv = (CustomKeyboard) getLayoutInflater().inflate(R.layout.keyboard, null);
        kv.setPreviewEnabled(false);
        kv.setKeyboard(languageHandler.getCurrentKeyboard());
        kv.setOnKeyboardActionListener(this);
        setCandidatesViewShown(false);
        return kv;
    }


    @Override
    public View onCreateCandidatesView() {
        return new CandidateView(this);
    }


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }


    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        ExtractedText et;
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = ic.getSelectedText(0);
                if (selectedText == null) {
                    // no selection, so delete previous character
                    ic.deleteSurroundingText(1, 0);
                } else {
                    // delete the selection
                    ic.commitText("", 1);
                }
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                languageHandler.getCurrentKeyboard().setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                // ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                handleAction();
                break;
            case SCRIPT_START:
                new AsyncScript().execute(ic.getExtractedText(scriptParams, 0).text.toString());
                break;
            case LANGUAGE_CHANGE:
                kv.setKeyboard(languageHandler.next());
                break;
            case SCRIPT_WRAPPER:
                et = ic.getExtractedText(scriptParams, 0);
                ic.commitText(getString(R.string.script_brackets), et.selectionEnd + 2);
                ic.setSelection(et.selectionEnd + 2, et.selectionEnd + 2);
                break;
            case GO_BACK_ONCE:
                et = ic.getExtractedText(scriptParams, 0);
                ic.setSelection(et.selectionEnd - 1, et.selectionEnd - 1);
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps)
                    code = Character.toUpperCase(code);

                ic.commitText(String.valueOf(code), 1);
        }
    }

    private void handleAction() {
        EditorInfo curEditor = getCurrentInputEditorInfo();
        switch (curEditor.imeOptions & EditorInfo.IME_MASK_ACTION) {
            case EditorInfo.IME_ACTION_GO:
                getCurrentInputConnection().performEditorAction(EditorInfo.IME_ACTION_GO);
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                getCurrentInputConnection().performEditorAction(EditorInfo.IME_ACTION_SEARCH);
                break;
/*
       case EditorInfo.IME_ACTION_DONE:
                getCurrentInputConnection().performEditorAction(EditorInfo.IME_ACTION_DONE);
                break;
    case EditorInfo.IME_ACTION_NEXT:
                getCurrentInputConnection().performEditorAction(EditorInfo.IME_ACTION_NEXT);
                break;
            case EditorInfo.IME_ACTION_SEND:
                getCurrentInputConnection().performEditorAction(EditorInfo.IME_ACTION_SEND);
                break;*/
            default:
                getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
        }
    }

    private String processScript(CharSequence text) {
        /*
        New async task
        >change icon to some sort of loading
        >run script in the different thread
        >when finished, return icon to script run
         */
        return processor.process((String) text);
    }


    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }


    class AsyncScript extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kv.setLoadingScript(this);

        }

        @Override
        protected String doInBackground(String... params) {
            return ScriptKeyboard.this.processScript(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            try {
                ExtractedText et = getCurrentInputConnection().getExtractedText(scriptParams, 0);
                getCurrentInputConnection().setSelection(et.text.length(), et.text.length());
                getCurrentInputConnection().deleteSurroundingText(et.text.length(), 0);
                getCurrentInputConnection().commitText(s, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                kv.setFinishedScript();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            kv.setFinishedScript();
        }


    }
}
