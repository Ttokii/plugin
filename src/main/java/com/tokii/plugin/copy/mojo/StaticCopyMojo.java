package com.tokii.plugin.copy.mojo;

import com.tokii.plugin.copy.server.FileListenerServer;
import com.tokii.plugin.copy.utils.PluginLog;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

@Mojo(name="run")
public class StaticCopyMojo extends AbstractMojo {
	@Parameter(required=true)
	private String resourceDir;
	@Parameter(required=true)
	private String targetDir;
	@Parameter(required=true)
	private String mainClass;
	@Parameter(required=true)
	private String classPath;
	@Parameter
	private String args;

	public void execute() throws MojoExecutionException, MojoFailureException {

		Map<String,Object> pluginContext = getPluginContext();
		Iterator<String> it = pluginContext.keySet().iterator();
		System.out.println("------------ pluginContext ------------");
		while(it.hasNext()){
			String key = it.next();
			System.out.println("");
		}
		System.out.println("------------ pluginContext ------------");
		if(mainClass == null || mainClass.trim().equals("") || 
				classPath == null || classPath.trim().equals("")){
			getLog().info("plugin:static-copy:无法加载主类,启动失败...");
			return;
		}
		if(resourceDir == null || resourceDir.trim().equals("") || 
				targetDir == null || targetDir.trim().equals("")){
			getLog().info("plugin:static-copy:资源目录或部署目录不能为空,启动失败...");
		}
		PluginLog.setLog(getLog());
		FileListenerServer server = new FileListenerServer(resourceDir, targetDir);
		server.start();
		
		boolean isSuccess = runMain();
		if(!isSuccess) System.exit(0);
		
		getLog().info("plugin:static-copy:应用启动成功...");
		waitIndefinitely();
	}
	
	private boolean runMain(){
		boolean success = true;
		StringBuilder sb = new StringBuilder();
		sb.append("mvn exec:java ").append("-Dexec.mainClass=\"").append(mainClass).append("\" ")
			.append("-Dexec.args=\"").append(args).append("\"");
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec(sb.toString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (proc != null) {
            try {
            	BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GBK"));
                String line;
                while ((line = in.readLine()) != null) {
                	System.out.println(line);
                }
                in.close();
            } catch (Exception e) {
            	success = false;
            }
            
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getErrorStream(),"GBK"));
                String line;
                while ((line = in.readLine()) != null) {
                	System.out.println(line);
                	if(success) success = false;
                }
                in.close();
            } catch (Exception e) {
            	success = false;
            }
			
            try {
				proc.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            proc.destroy();
        }
		return success;
	}
	
	private void waitIndefinitely() {
        Object lock = new Object();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                getLog().warn("RunMojo.interrupted", e);
            }
        }
    }
	
}