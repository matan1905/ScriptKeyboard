package net.wospy.scriptkeyboard;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Matan on 28/11/2017.
 */

public class ScriptAdapter extends BaseAdapter {

    private Context c;
    List<Script> scripts;
    ScriptAdapter(Context c){
        this.c = c;
        scripts= Script.listAll(Script.class);

    }

    @Override
    public void notifyDataSetChanged() {
        scripts= Script.listAll(Script.class);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return scripts.size();
    }

    @Override
    public Object getItem(int position) {
        return scripts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return scripts.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder h ;
        if(convertView ==null) {
            h= new Holder();
        convertView=    LayoutInflater.from(c).inflate(R.layout.listitem_snippet, null);
           h.desc = (TextView) convertView.findViewById(R.id.lblDesc);
           h.name = (TextView) convertView.findViewById(R.id.lblName);
            h.edit = (ImageButton) convertView.findViewById(R.id.btnEdit);
           h.delete = (ImageButton) convertView.findViewById(R.id.btnDelete);

            h.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CodeSnippets)c).editCodeSnippet(scripts.get(position).getId());
            }
            });
            h.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(c);
                    ad.setMessage("Are you sure you want to delete \"" + scripts.get(position).name + "\"?");
                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scripts.get(position).delete();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            });
            convertView.setTag(h);
        }
        else{
            h= (Holder) convertView.getTag();
        }

        h.name.setText(scripts.get(position).name);
        h.desc.setText(scripts.get(position).desc);
        return convertView;
    }
    class Holder{
        TextView name,desc;
        ImageButton edit,delete;
    }
}
