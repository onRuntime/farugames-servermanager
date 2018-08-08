package net.farugames.servermanager.manager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.farugames.api.core.server.ServerType;
import net.farugames.database.redis.servers.IServer;
import net.farugames.servermanager.Main;
import net.farugames.servermanager.Methods;
import net.farugames.servermanager.utils.FileManager;

public class ServersManager {

	public static ServersManager getInstance() {
		return new ServersManager();
	}

	public void generateServer(ServerType serverType, String host, String map) {
		String serverTypeName = serverType.toString().toLowerCase();
		Integer serverId = generateId(serverType);
		Integer serverPort = generatePort(serverType);
		File fileResult = new File(serverTypeName + serverId);
		String serverName = fileResult.toString();
		File serverFolder = new File("servers/" + fileResult);
		String serverHost = host;
		String serverMap = map;
		
		IServer.create(serverName, Main.getIp(), serverPort, serverHost, serverMap);

		File from = new File("template/" + serverType.toString().toLowerCase());
		File to = serverFolder;
		try {
			System.out.println(Methods.getPrefix() +"Absolute file directory: " + serverFolder.getAbsolutePath());
			FileManager.copyFolder(from, to);
			System.out.println(Methods.getPrefix() + serverName + " has been generated succesfulys !");
			System.out.println(Methods.getPrefix() + "Trying to generate customs files...");
			generateFiles(serverType, serverName, serverPort, serverFolder);
			try {
				Runtime.getRuntime().exec("sh " + serverFolder + "/runner.sh");
				System.out
						.println(Methods.getPrefix() + serverName + " is running as well.");
			} catch (Exception exce) {
				System.err.println(Methods.getPrefix() + "fail at the start of " + serverName);
				exce.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println(Methods.getPrefix() + serverType + serverId + " as fail on generation.");
			e.printStackTrace();
		}
	}
	
	public void deleteServer(String serverName) {
		File sFolder = new File("/home/minecraft/servers/" + serverName);
		System.out.println(Methods.getPrefix() + "Trying to delete sql line " + serverName + "...");
		IServer.delete(serverName);
		System.out.println(Methods.getPrefix() + "Trying to quit screen " + serverName + "...");
		try {
			
			Runtime.getRuntime().exec("screen -XS " + serverName + " quit");
			System.out.println(Methods.getPrefix() + serverName + " has been quit succesfulys !");
			System.out.println(Methods.getPrefix() + "Trying to delete " + serverName + "...");
			FileUtils.deleteDirectory(sFolder);
			System.out.println(Methods.getPrefix() + serverName + " has been deleted succesfulys !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Integer generatePort(ServerType serverType) {
		Integer portMin = 26000;
		Integer portMax = 26999;
		Integer port = null;
		List<Integer> serversPortList = IServer.getServersPortList();
		for(int i = portMin; i <= portMax; i++) {
		    if(!serversPortList.contains(i)) {
		    	port = i;
		        break;
		    }
		}
		return port;
	}
	
	public Integer generateId(ServerType serverType) {
		Integer idMax = 100;
		Integer idMin = 1;
		Integer id = idMin;
		String serverTypeName = serverType.toString().toLowerCase();
		List<String> serversNameList = IServer.getServersNameList();
		for(int i = idMin; i <= idMax; i++) {
		    if(!serversNameList.contains(serverTypeName + i)) {
		    	id = i;
		        break;
		    }
		}
		return id;
	}
	
	public void generateFiles(ServerType serverType, String sName , int sPort, File sFolder) {
		try {
			FileManager.writeFile(sFolder + "/eula.txt", "eula=true");
			switch (serverType) {
				case HUB:
					FileManager.writeFile(sFolder + "/runner.sh", "screen -d -m -S " + sName + " bash -c 'cd " + sFolder.getAbsolutePath() + " && java -Xms128M -Xmx1G -jar spigot.jar'");
					FileManager.writeFile(sFolder + "/server.properties",
							"server-name=" + sName + "\n" + 
							"server-port=" + sPort + "\n" + 
							"motd=" + sName + "\n" + 
							"spawn-protection=0" + "\n" + 
							"max-players=100" + "\n" +
							"allow-nether=false" + "\n" +
							"gamemode=2" + "\n" +
							"spawn-monsters=false" + "\n" +
							"announce-player-achievements=false" + "\n" +
							"pvp=true" + "\n" +
							"spawn-npcs=false" + "\n" +
							"allow-flight=true" + "\n" +
							"level-name=world" + "\n" +
							"spawn-animals=false" + "\n" + 
							"generate-structures=false" + "\n" + 
							"online-mode=false" + "\n");
					break;
				case THD:
					FileManager.writeFile(sFolder + "/runner.sh", "screen -d -m -S " + sName + " bash -c 'cd " + sFolder.getAbsolutePath() + " && java -Xms128M -Xmx256M -jar spigot.jar'");
					FileManager.writeFile(sFolder + "/server.properties",
							"server-name=" + sName + "\n" + 
							"server-port=" + sPort + "\n" + 
							"motd=" + sName + "\n" + 
							"spawn-protection=0" + "\n" + 
							"max-players=100" + "\n" +
							"allow-nether=false" + "\n" +
							"gamemode=3" + "\n" +
							"spawn-monsters=false" + "\n" +
							"announce-player-achievements=false" + "\n" +
							"pvp=true" + "\n" +
							"spawn-npcs=false" + "\n" +
							"allow-flight=false" + "\n" +
							"level-name=world" + "\n" +
							"spawn-animals=false" + "\n" + 
							"generate-structures=false" + "\n" + 
							"online-mode=false" + "\n");
					break;
				default:
					break;
			}
			
			System.out.println(Methods.getPrefix() + sName + " files has been generated succesfulys !");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
