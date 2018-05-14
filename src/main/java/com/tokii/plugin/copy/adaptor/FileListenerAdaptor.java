package com.tokii.plugin.copy.adaptor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.maven.plugin.logging.Log;

import com.tokii.plugin.copy.utils.PathUtils;
import com.tokii.plugin.copy.utils.PluginLog;

public class FileListenerAdaptor extends FileAlterationListenerAdaptor {
	private final static Log log = PluginLog.getLog();
	private String resourceDired,targetDired;
	public FileListenerAdaptor(String resourceDir,String targetDir) {
		resourceDired = PathUtils.translateSeparator(resourceDir);
		if(!resourceDired.endsWith("/")){
			resourceDired = resourceDired.concat("/");
		}
		targetDired = PathUtils.translateSeparator(targetDir);
		if(!targetDired.endsWith("/")){
			targetDired = targetDired.concat("/");
		}
	}
	@Override
	public void onStart(FileAlterationObserver observer) {super.onStart(observer);}
	@Override
	public void onStop(FileAlterationObserver observer) {super.onStop(observer);}

	@Override
	public void onDirectoryCreate(File directory) {
		String targetFileStr = PathUtils.substituteTargetDir(resourceDired, targetDired, directory.getAbsolutePath());
		File targetFile = new File(targetFileStr);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
	}
	
	@Override
	public void onDirectoryChange(File directory) {}
	
	@Override
	public void onDirectoryDelete(File directory) {
		String targetFileStr = PathUtils.substituteTargetDir(resourceDired, targetDired, directory.getAbsolutePath());
		File targetFile = new File(targetFileStr);
		if(targetFile.exists()){
			targetFile.delete();
		}
	}

	@Override
	public void onFileCreate(File file) {
		fileCreateAndModify(file);
	}

	@Override
	public void onFileChange(File file) {
		fileCreateAndModify(file);
	}

	@Override
	public void onFileDelete(File file) {
		String targetFileStr = PathUtils.substituteTargetDir(resourceDired, targetDired, file.getAbsolutePath());
		File targetFile = new File(targetFileStr);
		if(targetFile.exists()){
			targetFile.delete();
		}
	}
	
	private void fileCreateAndModify(File file){
		String targetFileStr = PathUtils.substituteTargetDir(resourceDired, targetDired, file.getAbsolutePath());
		File targetFile = new File(targetFileStr);
		try {
			FileUtils.copyFile(file, targetFile);
			log.info("update file:".concat(file.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
