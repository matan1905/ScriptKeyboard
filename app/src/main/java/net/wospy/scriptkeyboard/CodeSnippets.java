package net.wospy.scriptkeyboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


public class CodeSnippets extends AppCompatActivity {
    ScriptAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_snippets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView lstSnippets = (ListView) findViewById(R.id.lstCodeSnippets);
        adapter = new ScriptAdapter(this);
        lstSnippets.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            editCodeSnippet(-1l);
            }
        });
    }

    void editCodeSnippet(final Long id){
        AlertDialog.Builder ad = new AlertDialog.Builder(CodeSnippets.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_code_snippet,null);
        final EditText name = (EditText) v.findViewById(R.id.txtName);
        final EditText desc = (EditText) v.findViewById(R.id.txtDesc);
        if(id!=-1){
            Script s = Script.findById(Script.class,id);
            name.setText(s.name);
            desc.setText(s.desc);
        }
        ad.setView(v);
        ad.setTitle("Before we jump to the code...");
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(name.getText().length()>0 && desc.getText().length()>0)
                {
                    Intent intent = new Intent(CodeSnippets.this,EditCodeSnippet.class);
                    intent.putExtra("NAME",name.getText().toString());
                    intent.putExtra("DESC",desc.getText().toString());
                    if(id>=0){
                        intent.putExtra("ID",id);
                    }
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        ad.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
