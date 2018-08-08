package net.farugames.database;

import java.util.ArrayList;
import java.util.List;

import net.farugames.api.core.server.ServerType;
import net.farugames.database.redis.RedisManager;
import net.farugames.servermanager.Methods;
import redis.clients.jedis.Jedis;

public class IRequestedServers {

	public static List<String> getRequestedHostList(ServerType serverType) {
		List<String> requestedHubList = new ArrayList<String>();
		try (Jedis jedis = RedisManager.getRedisDatabase().getJedisPool().getResource()) {
			for (String string : jedis
					.lrange("requested_list:" + serverType.toString().toLowerCase() + ":" + "host"  , 0, 50)) {
				requestedHubList.add(string);
			}
		} catch (Exception e) {
			System.out.println(Methods.getPrefixRedis() + "Error");
		}
		return requestedHubList;
	}

	public static List<String> getRequestedMapList(ServerType serverType) {
		List<String> requestedHubList = new ArrayList<String>();
		try (Jedis jedis = RedisManager.getRedisDatabase().getJedisPool().getResource()) {
			for (String string : jedis.lrange("requested_list:" + serverType.toString().toLowerCase() + ":" + "map",
					0, 50)) {
				requestedHubList.add(string);
			}
		} catch (Exception e) {
			System.out.println(Methods.getPrefixRedis() + "Error");
		}
		return requestedHubList;
	}
	
	public static void removeLastRequested(ServerType serverType, String host, String map) {
		try (Jedis jedis = RedisManager.getRedisDatabase().getJedisPool().getResource()) {
			jedis.lrem("requested_list:" + serverType.toString().toLowerCase() + ":" + "host", 1, host);
			jedis.lrem("requested_list:" + serverType.toString().toLowerCase() + ":" + "map", 1, map);
		} catch (Exception e) {
			System.out.println(Methods.getPrefixRedis() + "Error");
		}
	}

}
