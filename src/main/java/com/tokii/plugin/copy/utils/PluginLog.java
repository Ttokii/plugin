package com.tokii.plugin.copy.utils;

import org.apache.maven.plugin.logging.Log;

public class PluginLog {
	private static Log log = null;
    public static void setLog(Log l){
        log = l;
    }
    public static Log getLog(){
        return log;
    }
}
