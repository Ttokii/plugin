package com.tokii.plugin.copy.utils;

public class PathUtils {
	
	public static String substituteTargetDir(String resourceDired,String targetDired,String filePath){
		return translateSeparator(filePath).replaceAll(resourceDired, targetDired);
	}
	
	public static String translateSeparator(String path){
		if(path != null && path.length() > 0){
			return path.replaceAll("\\\\", "/");
		}
		return path;
	}
}
