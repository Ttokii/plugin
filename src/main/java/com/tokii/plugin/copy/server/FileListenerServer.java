package com.tokii.plugin.copy.server;

import com.tokii.plugin.copy.adaptor.FileListenerAdaptor;
import com.tokii.plugin.copy.io.FileFilterImpl;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class FileListenerServer {
	private String resourceDir;
	private String targetDir;
	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}
	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}
	
	public FileListenerServer(String _resourceDir,String _targetDir) {
		resourceDir = _resourceDir;
		targetDir = _targetDir;
	}
	
	public void start(){
		FileAlterationObserver observer = 
				new FileAlterationObserver(new File(resourceDir),new FileFilterImpl());
		FileListenerAdaptor listener = new FileListenerAdaptor(resourceDir,targetDir);
		observer.addListener(listener);
		FileAlterationMonitor fileMonitor = new FileAlterationMonitor(500 , new FileAlterationObserver[]{observer});
        try {
			fileMonitor.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
