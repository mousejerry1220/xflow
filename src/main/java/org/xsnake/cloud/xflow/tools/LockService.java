package org.xsnake.cloud.xflow.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.zookeeper.ZookeeperTemplate;

@Component
public class LockService {

	@Autowired
	ZookeeperTemplate zookeeperTemplate;
	
	public static final String LOCK_PATH = "/XFLOW/LOCK";
	
	public void lock(String... keys) throws LockedException, IOException {
		String key = getKey(keys);
		boolean exists = zookeeperTemplate.exists(key);
		if(exists){
			throw new LockedException();
		}
		zookeeperTemplate.node(key);
	}

	public void unLock(String... keys) throws IOException {
		String key = getKey(keys);
		zookeeperTemplate.delete(key);
	}
	
	private String getKey(String[] keys) {
		StringBuffer key = new StringBuffer(LOCK_PATH);
		key.append("/");
		for(String k : keys){
			key.append(k).append("_");
		}
		String result = key.toString().replaceAll("/", "-");
		return result;
	}

}
