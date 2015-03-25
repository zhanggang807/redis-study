package org.dean.study.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * Redis Demo类
 * 
 * @author dean Mar 24, 2015
 */
public class RedisDemo {
	
	private static Jedis jedis = new Jedis("127.0.0.1", 6379);
	
	public static void main(String[] args) {
		jedis.auth("123456");
		testString();
		testMap();
		testList();
		testSet();
		testSort();
		testRedisPool();
	}
	
	/** Redis String*/
	private static void testString(){
		jedis.set("name", "dean");//设置
		System.out.println("1 \t:\t" + jedis.get("name"));
		
		jedis.append("name", " is my name.");//追加
		System.out.println("2 \t:\t" + jedis.get("name"));

		jedis.del("name");//删除
		System.out.println("3 \t:\t" + jedis.get("name"));
		
		jedis.mset("name", "sam", "age", "23", "qq", "235805947");//多条设置
		jedis.incr("age");//加1操作
		System.out.println("4 \t:\t" + jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
	}

	/** Redis Map*/
	private static void testMap(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "jack");
		map.put("age", "32");
		map.put("qq", "346363464");
		jedis.hmset("user", map);//设置hashmap
		
		List<String> rsMapValue = jedis.hmget("user", "name", "age", "qq");//取出hashmap值,可变参数
		System.out.println("5 \t:\t" + rsMapValue);
		
		jedis.hdel("user", "age");//删除map-->key
		System.out.println("6 \t:\t" + jedis.hmget("user", "age"));
		System.out.println("7 \t:\t" + jedis.hlen("user"));//返回user(map类型)的key数量
		System.out.println("8 \t:\t" + jedis.exists("user"));//redis中是否存在此key
		System.out.println("9 \t:\t" + jedis.hkeys("user"));//返回user(map类型)的所有key
		System.out.println("10 \t:\t" + jedis.hvals("user"));//返回user(map类型)的所有value
		
		int count = 1;
		Iterator<String> ite = jedis.hkeys("user").iterator();//使用迭代器遍历
		while (ite.hasNext()){
			String key = ite.next();
			System.out.println("11-" + count++ + "\t:\t" + key + ":" + jedis.hmget("user", key));//返回user(map类型)的所有value
		}
	}

	/** Redis List*/
	private static void testList(){
		jedis.del("java framework");
		System.out.println("12 \t:\t" + jedis.lrange("java framework", 0, 1));
		
		//last push, from tail to head
		jedis.lpush("java framework", "spring");
		jedis.lpush("java framework", "struts");
		jedis.lpush("java framework", "hibernate");
		//params:key,start,end. end=-1 means all
		System.out.println("13 \t:\t" + jedis.lrange("java framework", 0, -1));
		
		jedis.del("java framework");
		//regular push, from head to tail
		jedis.rpush("java framework", "spring");
		jedis.rpush("java framework", "struts");
		jedis.rpush("java framework", "hibernate");
		System.out.println("14 \t:\t" + jedis.lrange("java framework", 0, -1));
	}

	/** Redis Set */
	private static void testSet(){
		jedis.sadd("driver", "liuling");
		jedis.sadd("driver", "xinxin");
		jedis.sadd("driver", "liling");
		jedis.sadd("driver", "zhangxinchao");
		jedis.sadd("driver", "who");
		
		jedis.srem("driver", "who");//移除who这个值
		System.out.println("15 \t:\t" + jedis.smembers("driver"));//获取所有值
		System.out.println("16 \t:\t" + jedis.sismember("driver", "who"));//who是否在set中
		System.out.println("17 \t:\t" + jedis.srandmember("driver"));//随机取一个
		System.out.println("18 \t:\t" + jedis.scard("driver"));//返回集合的元素个数
	}

	/** Redis Sort */
	private static void testSort(){
		jedis.del("a");
		jedis.rpush("a", "1");
		jedis.lpush("a", "6");
		jedis.lpush("a", "3");
		jedis.lpush("a", "9");
		System.out.println("19 \t:\t" + jedis.lrange("a", 0, -1));
		System.out.println("20 \t:\t" + jedis.sort("a"));//排序
		System.out.println("21 \t:\t" + jedis.lrange("a", 0, -1));
	}
	
	/** Redis Pool */
	private static void testRedisPool(){
		RedisUtil.getJedis().set("newname", "中文测试");
		System.out.println(RedisUtil.getJedis().get("newname"));
	}
	
}