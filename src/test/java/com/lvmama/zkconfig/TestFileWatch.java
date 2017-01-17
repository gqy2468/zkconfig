package com.lvmama.zkconfig;

import com.lvmama.zkconfig.dirwatch.FileWatch;
import com.lvmama.zkconfig.dirwatch.FileWatchProcessorImpl;

public class TestFileWatch {

	public static void main(String[] args) throws InterruptedException {
		FileWatch watch = FileWatch.buildWatch();
		FileWatchProcessorImpl processor = new FileWatchProcessorImpl();
		
		watch.runWatch("d:/test/v-cs/db.properties", processor);

//		Thread.sleep(5000);
//		watch.stopWatch();

	}

}
