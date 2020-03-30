package com.wb.numerousstudents.Utils;

import com.socks.library.KLog;

public class DebugUtil {

    private DebugUtil debugUtil;
    public DebugUtil getInstance(){
        return debugUtil;
    }

    public static void debug(String tag,String message){
        KLog.v("wb.z " + tag + " : " + message);
    }
    public static void debug(String message){
        KLog.v("wb.z "  + " : " + message);
    }
}
