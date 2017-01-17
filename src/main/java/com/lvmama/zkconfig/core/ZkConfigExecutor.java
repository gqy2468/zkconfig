package com.lvmama.zkconfig.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;

import com.lvmama.zkconfig.common.ActiveKeyValueStore;
import com.lvmama.zkconfig.common.FileUtils;
import com.lvmama.zkconfig.common.SystemInit;
import com.lvmama.zkconfig.common.ZkConfigParser;
import com.lvmama.zkconfig.monitor.Monitor;
import com.lvmama.zkconfig.monitor.MonitorFactory;
import com.lvmama.zkconfig.monitor.MonitorFactory.MonitorType;

public class ZkConfigExecutor {
	private static Logger logger = Logger.getLogger(ZkConfigExecutor.class);
	/**
	 * 连接zk服务器，初始化系统参数，返回 ActiveKeyValueStore对象。
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private static ActiveKeyValueStore buildStore() throws KeeperException, InterruptedException, IOException{
		ActiveKeyValueStore store = new ActiveKeyValueStore();
		store.connect();//连接服务器
		SystemInit.init(store);//初始化系统参数
		return store;
	}
	
	/**
	 * 对所有配置目录下所有文件执行监视
	 * @param monitor
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void execute(MonitorType type) throws IOException, InterruptedException, KeeperException{
		ActiveKeyValueStore store = buildStore();
		List<File> files = FileUtils.getAllConfigFiles();
		if(files != null && files.size() > 0){
			for(File file : files){
				Monitor monitor = MonitorFactory.getMonitor(type);
				monitor.monitor(store,file.getAbsolutePath());//执行监听
			}
		}else{
			logger.error("未获取到任何合法的配置文件，无法执行监控");
			//throw new IOException("未获取到任何合法的配置文件，无法执行监控");
		}

		//监控目录是否有新增文件
		int period = 1000*15;//默认十五秒
		String dwp = ZkConfigParser.getProperty("dir_watch_period");
		if(dwp !=null && !"".equals(dwp.trim())){
			period = Integer.parseInt(dwp);
		}
		List<String> allPaths = FileUtils.getAllConfigPaths();
		for(String path : allPaths){
			File dir = new File(path);
			if (dir.isDirectory()){
				while (dir.exists()) {
		            File[] dirfiles = dir.listFiles();
		            List<String> children = store.getChildrenWithoutWatcher(FileUtils.getZkPathByConfigPath(path));
					Monitor monitor = MonitorFactory.getMonitor(type);
		            for (File df : dirfiles) {
		            	if (children.indexOf(df.getName()) == -1) {
							monitor.monitor(store,df.getAbsolutePath());//执行监听
		            	}
		            }
		            for (String child : children) {
		            	String filePath = path + File.separator + child;
			            File newfile = new File(filePath);
		            	if (!newfile.exists()) { 
		                    try {  
		                        if (newfile.createNewFile()) {  
		                        	logger.error("创建文件" + filePath + "成功");
		                        	newfile.setWritable(true, false);
		            				byte[] data = store.readByteWithoutWatcher(FileUtils.getZkPathByConfigPath(filePath));
		                        	monitor.writeFile(data, filePath);
		                        } else {  
		                        	logger.error("创建文件" +  filePath + "失败");
		                        }  
		                    } catch (IOException e) {  
		                        logger.error("创建文件" +  filePath + "失败" + e.getMessage());
		            			throw new IOException("创建文件" +  filePath + "失败" + e.getMessage());
		                    }  
							monitor.monitor(store,newfile.getAbsolutePath());//执行监听
		            	}
		            }
					Thread.sleep(period);
				}
			}
		}
	}
	/**
	 * 对所有配置目录下所有文件执行监视
	 * @param monitor
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void execute(int type) throws IOException, InterruptedException, KeeperException{
		ActiveKeyValueStore store = buildStore();
		List<File> files = FileUtils.getAllConfigFiles();
		if(files != null && files.size() > 0){
			for(File file : files){
				Monitor monitor = MonitorFactory.getMonitor(MonitorFactory.MonitorType.getMonitorType(type));
				monitor.monitor(store,file.getAbsolutePath());//执行监听
			}
		}else{
			logger.error("未获取到任何合法的配置文件，无法执行监控");
			//throw new IOException("未获取到任何合法的配置文件，无法执行监控");
		}

		//监控目录是否有新增文件
		int period = 1000*15;//默认十五秒
		String dwp = ZkConfigParser.getProperty("dir_watch_period");
		if(dwp !=null && !"".equals(dwp.trim())){
			period = Integer.parseInt(dwp);
		}
		List<String> allPaths = FileUtils.getAllConfigPaths();
		for(String path : allPaths){
			File dir = new File(path);
			if (dir.isDirectory()){
				while (dir.exists()) {
		            File[] dirfiles = dir.listFiles();
		            List<String> children = store.getChildrenWithoutWatcher(FileUtils.getZkPathByConfigPath(path));
					Monitor monitor = MonitorFactory.getMonitor(MonitorFactory.MonitorType.getMonitorType(type));
		            for (File df : dirfiles) {
		            	if (children.indexOf(df.getName()) == -1) {
							monitor.monitor(store,df.getAbsolutePath());//执行监听
		            	}
		            }
		            for (String child : children) {
		            	String filePath = path + File.separator + child;
			            File newfile = new File(filePath);
		            	if (!newfile.exists()) { 
		                    try {  
		                        if (newfile.createNewFile()) {  
		                        	logger.error("创建文件" + filePath + "成功");
		                        	newfile.setWritable(true, false);
		            				byte[] data = store.readByteWithoutWatcher(FileUtils.getZkPathByConfigPath(filePath));
		                        	monitor.writeFile(data, filePath);
		                        } else {  
		                        	logger.error("创建文件" +  filePath + "失败");
		                        }  
		                    } catch (IOException e) {  
		                        logger.error("创建文件" +  filePath + "失败" + e.getMessage());
		            			throw new IOException("创建文件" +  filePath + "失败" + e.getMessage());
		                    }  
							monitor.monitor(store,newfile.getAbsolutePath());//执行监听
		            	}
		            }
					Thread.sleep(period);
				}
			}
		}
	}
	/**
	 * 监视指定配置文件
	 * @param monitor
	 * @param filePath
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void executeSpecial(MonitorType type,String filePath) throws IOException, InterruptedException, KeeperException{
		ActiveKeyValueStore store = buildStore();
		Monitor monitor = MonitorFactory.getMonitor(type);
		monitor.monitor(store,filePath);//执行监听
	}
	/**
	 * 监视指定配置文件
	 * @param monitor
	 * @param filePath
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void executeSpecial(int type,String filePath) throws IOException, InterruptedException, KeeperException{
		ActiveKeyValueStore store = buildStore();
		Monitor monitor = MonitorFactory.getMonitor(MonitorFactory.MonitorType.getMonitorType(type));
		monitor.monitor(store,filePath);//执行监听
	}

	/**
	 * run local example
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		CountDownLatch connectedSignal = new CountDownLatch(1);
		try {
			//1、注册配置所需配置类
			BeanRegisterCenter brc = new BeanRegisterCenter();//执行监听
//			brc.register(new TestObj());
			//2、执行监听
			ZkConfigExecutor.execute(MonitorFactory.MonitorType.CONF_PROPERTIES_FILE_MONITOR);
			
			connectedSignal.await();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
		
	}

}
