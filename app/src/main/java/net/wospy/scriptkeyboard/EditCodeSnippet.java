package net.wospy.scriptkeyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditCodeSnippet extends AppCompatActivity {
    long id =-1;
    String content;
    Script script;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_code_snippet);
        //(previous activity)Prompt user with a dialog, offering him editable name&desc (and hidden id).
        Intent intent = getIntent();
        final String name = intent.getStringExtra("NAME");
        final String desc = intent.getStringExtra("DESC");

        if(intent.hasExtra("ID")){
            id = intent.getLongExtra("ID",-1);
            script = Script.findById(Script.class, id);
            System.out.println(script.content);
            content = script.content;
        }

        Button save = (Button) findViewById(R.id.btnSaveScript);
        final EditText snippet = (EditText) findViewById(R.id.txtCode);
        if (content != null) {
            snippet.setText(content);
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//TODO TEST IF SCRIPT IS OK
                if(id==-1){ //new script
                    content = snippet.getText().toString();
                    script = new Script(name,desc,content);

                }
                else{//existing script
                    script.content=snippet.getText().toString();
                    script.name = name;
                    script.desc=desc;
                }
                if(ScriptProcessor.isValidScript(content)){
                script.save();//TODO maybe add it to candidates
                    ScriptProcessor.savedScript();
                    finish();
                }else{
                    Toast.makeText(EditCodeSnippet.this, "Something is wrong with the script.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cancelEdit(View view) {
        finish();
    }
}
