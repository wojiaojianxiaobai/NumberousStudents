package com.wb.numerousstudents.Config;

public class Config {

    private static Config config = null;
    public static Config getInstance(){
        if (config == null){
            config = new Config();
        }
        return config;
    }

    private static boolean PROCESS_SERVER = false;

    public boolean isProcessServer(){
        return PROCESS_SERVER;
    }

    public void setProcessServerState(boolean state){
        PROCESS_SERVER = state;
    }

}