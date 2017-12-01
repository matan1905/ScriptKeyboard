package net.wospy.scriptkeyboard;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Matan on 24/11/2017.
 */
public class ScriptProcessor {
    Context cx;
    ScriptableObject scope;
    public static ArrayList<String> scriptQueue = new ArrayList<>();
    private static final Pattern sPattern
            = Pattern.compile("\\$\\|(.|\\s)*?\\|\\$");// $<Anything In Here As Long As It's One Line> , onchange also change the eval method removal
     ScriptProcessor(){
        /*String script = "function abc(x,y) {return x+y;}"
                + "function def(u,v) {return u-v;}" +
                "var widenit = [\"value\", \"original\", \"wide\", \"code-3000\", \"ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz\", \"\\uFF21\\uFF22\\uFF23\\uFF24\\uFF25\\uFF26\\uFF27\\uFF28\\uFF29\\uFF2A\\uFF2B\\uFF2C\\uFF2D\\uFF2E\\uFF2F\\uFF30\\uFF31\\uFF32\\uFF33\\uFF34\\uFF35\\uFF36\\uFF37\\uFF38\\uFF39\\uFF3A\\x20\\uFF41\\uFF42\\uFF43\\uFF44\\uFF45\\uFF46\\uFF47\\uFF48\\uFF49\\uFF4A\\uFF4B\\uFF4C\\uFF4D\\uFF4E\\uFF4F\\uFF50\\uFF51\\uFF52\\uFF53\\uFF54\\uFF55\\uFF56\\uFF57\\uFF58\\uFF59\\uFF5A\", \"\", \"href\", \"\", \"search\", \"\", \"\", \"length\", \"charAt\", \"\", \"\"];\n" +
                "\n" +
                "var normal=widenit[4];var changed=widenit[5];\n" +
                "function c(changeitb) {\n" +
                "    var wideletters = widenit[6];\n" +
                "    var changeitc = changeitb;\n" +
                "    //var thispage=location[widenit[7]];\n" +
                "    //if(thispage[widenit[9]](widenit[8])==-1&&thispage[widenit[9]](widenit[10])==-1){return (widenit[11]);} ;\n" +
                "    for (i = 0; i < changeitc[widenit[12]]; i++) {\n" +
                "        var widelength = changeitc[widenit[13]](i);\n" +
                "        var letters = 0;\n" +
                "        for (;\n" +
                "            (letters < normal[widenit[12]]) && (widelength != normal[widenit[13]](letters)); letters++) {;;\n" +
                "        };\n" +
                "        if (letters < normal[widenit[12]]) {\n" +
                "            wideletters += changed[widenit[13]](letters);\n" +
                "        } else {\n" +
                "            wideletters += widelength;\n" +
                "        };\n" +
                "    };\n" +
                "    return wideletters;\n" +
                "};";*/

        //Load saved scripts.

        cx= Context.enter();
        cx.setLanguageVersion(Context.VERSION_ES6);
        cx.setOptimizationLevel(-1);
        scope = cx.initStandardObjects();
      initScripts();
    }

    private void initScripts(){
        cx.evaluateString(scope, "var http= new Packages.net.wospy.scriptkeyboard.HttpHandler()", "script", 0, null);
        List<Script> scripts = Script.listAll(Script.class);
        for (Script script : scripts) {
            if(script.content!=null)
                try {
                    cx.evaluateString(scope, script.content, "script", 0, null);
                }catch (Exception e){e.printStackTrace();}
        }

    }

    private static boolean nScriptSaved =false;
     static void savedScript(){
        nScriptSaved =true;
    }

     String process(String text) {
         cx= Context.enter();
         cx.getWrapFactory().setJavaPrimitiveWrap(false);//Makes native java strings be strings and not NativeJavaObject@#
         cx.setOptimizationLevel(-1);
        if (nScriptSaved) {
            scope = cx.initStandardObjects();
            initScripts();
         nScriptSaved = false;
        }
        //Should return a final string after all $(script) has been evaluated
        Matcher matcher = sPattern.matcher(text);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb,eval(matcher.group()));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private String eval(String script){
        script = script.substring(2,script.length()-2);//removing the <{ , }> from the string.


        String result ="";

        try {
            result += cx.evaluateString(scope, script, "script", 0, null).toString();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            Context.exit();
        }
        return result.replace("org.mozilla.javascript.Undefined@0","");
    }

     static boolean isValidScript(String content) {
        Context tcx = Context.enter();
        tcx.setLanguageVersion(Context.VERSION_1_2);
        tcx.setOptimizationLevel(-1);
        ScriptableObject tscope = tcx.initStandardObjects();
        List<Script> scripts = Script.listAll(Script.class);
         try {

             for (Script script : scripts) {
                 if(script.content!=null)
                     tcx.evaluateString(tscope, script.content, "script", 0, null);
             }
         } catch (Exception e){e.printStackTrace();}
        try{
            tcx.evaluateString(tscope,content,"testing",0,null);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
