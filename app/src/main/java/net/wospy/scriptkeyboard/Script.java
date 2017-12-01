package net.wospy.scriptkeyboard;

import com.orm.SugarRecord;

/**
 * Created by Matan on 28/11/2017.
 */

public class Script extends SugarRecord<Script> {
    String name;
    String desc;
    String content;

    public Script(){
    }

    public Script(String name, String desc,String content){
        this.name = name;
        this.desc = desc;
        this.content=content;
    }
}
