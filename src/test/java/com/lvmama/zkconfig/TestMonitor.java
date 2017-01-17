package com.lvmama.zkconfig;

import java.io.File;
import java.util.List;

import com.lvmama.zkconfig.monitor.ConfFileMonitor;
import com.lvmama.zkconfig.monitor.Monitor;

public class TestMonitor {

	public static void main(String[] args) {
		Monitor monitor = new ConfFileMonitor();
		List<String> path = monitor.getAllConfigPaths();
		List<File> files = monitor.getAllConfigFiles();
		System.out.println(path);
		System.out.println(files);

	}
}
