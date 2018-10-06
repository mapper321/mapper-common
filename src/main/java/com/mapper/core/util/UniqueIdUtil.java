package com.mapper.core.util;

import java.util.UUID;


public class UniqueIdUtil {
	private static IdWorker idWorker = new IdWorker(1);
	
	public synchronized static long genId(){
		return idWorker.nextId();
	}

	public static final String getGuid() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
