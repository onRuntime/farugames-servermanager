package net.farugames.servermanager.database;

import net.farugames.servermanager.Methods;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Database {
	private final String host, password;
	private final int port;

	private JedisPool jedisPool;
	
	public Database(String host, String password, int port) {
		this.host = host;
		this.password = password;
		this.port = port;
	}
	public void connect() {
		ClassLoader previous = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(Jedis.class.getClassLoader());
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPool = new JedisPool(jedisPoolConfig, host, port, 3000, password);
		Thread.currentThread().setContextClassLoader(previous);
		try (Jedis jedisConnect = jedisPool.getResource()) {
			System.out.println(Methods.getPrefix() + "FaruGamesSM is connected to database.");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void disconnect() {
		try {
			jedisPool.close();
			return;
		} catch (Exception e) {
			System.out.println("Deconnection à la base de donées impossible.");
			return;
		}
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	public Jedis getJedis() {
		return jedisPool.getResource();
	}
}
