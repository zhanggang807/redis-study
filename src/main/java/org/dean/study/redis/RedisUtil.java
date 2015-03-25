package org.dean.study.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * test reids connection pool
 * 
 * @author dean Mar 25, 2015
 */
public class RedisUtil {
	
	private static String ADDR = "192.168.0.100";
	private static int PORT = 6379;
	private static String AUTH = "123456";
//	private static int MAX_ACTIVE = 1024;
	private static int MAX_IDLE = 1024;
	private static int MAX_WAIT = 10000;
	private static int TIMEOUT = 10000;
	private static boolean TEST_ON_BORROW = true;
	
	private static JedisPool jedisPool = null;
	
	static {
		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxActive(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);
		jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
	}
	
	public synchronized static Jedis getJedis(){
		if (jedisPool != null){
			Jedis resource = jedisPool.getResource();
			return resource;
		}else{
			return null;
		}
	}
	
	public static void returnResource(final Jedis jedis){
		if (jedis != null){
			jedisPool.returnResource(jedis);
		}
	}
	
}