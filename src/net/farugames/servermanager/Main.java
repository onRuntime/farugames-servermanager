package net.farugames.servermanager;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Timer;

import net.farugames.api.core.server.ServerStatus;
import net.farugames.api.core.server.ServerType;
import net.farugames.database.IRequestedServers;
import net.farugames.database.redis.RedisManager;
import net.farugames.database.redis.servers.IServer;
import net.farugames.servermanager.manager.ServersManager;
import net.farugames.servermanager.runnables.RequestedServerRunnable;

public class Main {

	public static RedisManager redisManager;

	public static void main(String[] args) {

		redisManager = new RedisManager("149.202.102.63", "b4z5MT4Nk6hA", 6379);
		redisManager.connect();

		new Timer().schedule(new RequestedServerRunnable(), 0, 1000);
	}

	public static void checkDeletedServers(ServerType serverType) {
		System.out.println(Methods.getPrefix() + "Check deleted servers on the database.");
		List<String> serversNameList = IServer.getServersNameList();
		if (serversNameList.get(0) == null) {
			return;
		}
		for (int i = 0; i < serversNameList.size(); i++) {
			// Faudra trouver une solution plus simple car ca risque de prendre pas mal de
			// temps au bout d'une cinquantaine de serv
			if(IServer.getStatus(serversNameList.get(i)) == ServerStatus.DELETE) {
				ServersManager.getInstance().deleteServer(serversNameList.get(i));
			}
		}
	}

	public static void checkAvailableServers(ServerType serverType) {
		System.out.println(Methods.getPrefix() + "Check if servers are available.");
		if (!IServer.getServersNameList().toString().contains(serverType.toString().toLowerCase())) {
			IServer.request(serverType);
		}
	}

	public static void checkRequestedServers(ServerType serverType) {
		System.out.println(Methods.getPrefix() + "Check requested servers on the database.");
		List<String> requestedHostList = IRequestedServers.getRequestedHostList(serverType);
		List<String> requestedMapList = IRequestedServers.getRequestedMapList(serverType);
		if (requestedHostList.size() >= 1 && IRequestedServers.getRequestedMapList(serverType).size() >= 1) {
			String host = requestedHostList.get(0);
			String map = requestedMapList.get(0);
			ServersManager.getInstance().generateServer(serverType, host, map);
			IRequestedServers.removeLastRequested(serverType, host, map);
		} else {
			System.out.println(Methods.getPrefix() + "There isn't requested server.");
		}
	}

	public static String getIp() {
		String ip = "127.0.0.1";
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ip = socket.getLocalAddress().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
}
