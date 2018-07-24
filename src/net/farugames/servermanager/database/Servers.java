package net.farugames.servermanager.database;

import net.farugames.data.database.entities.ServerDataEntity;
import net.farugames.data.database.entities.ServerType;
import net.farugames.servermanager.Main;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Servers {

	public static void removeLastRequest() {
		try (Jedis j = Main.database.getJedis()) {
			List<String> rqt = j.smembers("ServerManager:RequestedServers").stream().map(str -> str)
					.collect(Collectors.toList());
			j.srem("ServerManager:RequestedServers", rqt.get(0));
		}

	}

	public static List<ServerType> getAllRequestedServers() {
		List<ServerType> s = new ArrayList<>();
		try (Jedis j = Main.database.getJedis()) {
			List<String> ids = j.smembers("ServerManager:RequestedServers").stream().map(str -> str)
					.collect(Collectors.toList());
			String[] i = new String[ids.size()];
			for (int k = 0; k < i.length; k++) {
				i[k] = ids.get(k);
			}
			s.addAll(getServerType(i));
		}
		return s;
	}

	private static List<ServerType> getServerType(String... i) {
		if (i.length == 0)
			return new ArrayList<>();
		
		List<ServerType> st = new ArrayList<>();
		for (String id : i) {
			st.add(ServerType.getServerType(id));
		}
		
		return st;
	}

	public static List<String> getStatutDelete() {
		List<String> p = new ArrayList<>();
		try (Jedis j = Main.database.getJedis()) {
			getAllServers().forEach((values) -> {
				if (values.getServerStatus().equals("DELETE"))
					p.add(values.getServerName());
			});
		}
		return p;
	}

	public static List<Integer> getServersPorts() {
		List<Integer> p = new ArrayList<>();
		try (Jedis j = Main.database.getJedis()) {
			getAllServers().forEach((values) -> p.add(values.getServerPort()));
		}
		return p;
	}

	public static List<String> getServersNames() {
		List<String> n = new ArrayList<>();
		try (Jedis j = Main.database.getJedis()) {
			getAllServers().forEach((values) -> n.add(values.getServerName()));
		}
		return n;
	}

	public static List<ServerDataEntity> getAllServers() {
		List<ServerDataEntity> s = new ArrayList<>();
		try (Jedis j = Main.database.getJedis()) {
			List<String> ids = j.smembers("ServerManager:ServerID").stream().map(str -> str)
					.collect(Collectors.toList());
			String[] i = new String[ids.size()];
			for (int k = 0; k < i.length; k++) {
				i[k] = ids.get(k);
			}
			s.addAll(getServer(i));
		}
		return s;
	}

	public static void addServer(String name, String host, Integer port, ServerType mode, String statut,
			String hostRequester) {
		try (Jedis j = Main.database.getJedis()) {
			storeId(name);
			Main.database.getJedis().set("ServerManager:ServerData:" + name, Main.getGson.toJson(
					new ServerDataEntity(name, host, port, hostRequester, mode, statut, 0, new ArrayList<String>())));

		}

	}

	public static List<ServerDataEntity> getServer(String... names) {
		if (names.length == 0)
			return new ArrayList<>();
		List<ServerDataEntity> i = new ArrayList<>();
		try (Jedis j = Main.database.getJedis()) {
			Pipeline p = j.pipelined();
			List<Response<String>> r = new ArrayList<>();
			for (String id : names) {
				r.add(p.get("ServerManager:ServerData:" + id));
			}
			p.sync();
			for (Response<String> s : r) {
				i.add(Main.getGson.fromJson(s.get(), ServerDataEntity.class));
			}
		}
		return i;
	}

	private static void storeId(String id) {
		try (Jedis j = Main.database.getJedis()) {
			j.sadd("ServerManager:ServerID", id + "");
		}
	}

	public static void deleteServer(String name) {
		try (Jedis j = Main.database.getJedis()) {
			j.srem("ServerManager:ServerID", name);
			j.del("ServerManager:ServerData:" + name);
		}

	}

}
