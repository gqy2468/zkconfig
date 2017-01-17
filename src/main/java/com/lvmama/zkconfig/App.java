package com.lvmama.zkconfig;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;

import com.lvmama.zkconfig.core.BeanRegisterCenter;
import com.lvmama.zkconfig.core.ZkConfigExecutor;
import com.lvmama.zkconfig.monitor.MonitorFactory;

public class App 
{
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
			//2、执行监听
			ZkConfigExecutor.execute(MonitorFactory.MonitorType.CONF_FILE_MONITOR);
			
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