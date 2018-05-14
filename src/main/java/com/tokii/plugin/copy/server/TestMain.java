package com.tokii.plugin.copy.server;

import java.io.File;
import java.io.IOException;

public class TestMain {
	public static void main(String[] args) {
		for(String arg : args){
			System.out.println(arg);
		}
		String path = "F:/test/testCopyResource/init.test1";
		System.out.println(path);
		File file = new File(path);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
