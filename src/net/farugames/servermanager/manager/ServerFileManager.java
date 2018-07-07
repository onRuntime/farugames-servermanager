package net.farugames.servermanager.manager;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import net.farugames.servermanager.Methods;
import net.farugames.servermanager.ServerType;
import net.farugames.servermanager.database.ServerSql;
import net.farugames.servermanager.utils.FileManager;

public class ServerFileManager {

	public static ServerFileManager getInstance() {
		return new ServerFileManager();
	}

	public void generateServer(ServerType serverType, String mode) {
		
		String sType = serverType.getNameId();
		Integer sId = generateId(serverType);
		Integer sPort = generatePort(serverType);
		
		if(mode.equals("DEFAULT")) {
			sId = 1;
			sPort = 25000;
		}
		
		File sResult = new File(sType + sId);
		String sName = sResult.toString();
		File sFolder = new File("servers/" + sResult);
		
		ServerSql.addServer(sName, "127.0.0.1", sPort, mode, "LOADING");

		File from = new File(serverType.getFolder());
		File to = sFolder;
		
		System.out.println(Methods.getPrefix() + "Trying to create " + sName + "...");
		try {
			System.out.println(Methods.getPrefix() +"Absolute file directory: " + sFolder.getAbsolutePath());
			FileManager.copyFolder(from, to);
			System.out.println(Methods.getPrefix() + sName + " has been generated succesfulys !");
			System.out.println(Methods.getPrefix() + "Trying to generate customs files...");
			generateFiles(serverType, sName, sPort, sFolder);
			try {
				Runtime.getRuntime().exec("sh " + sFolder + "/runner.sh");
				System.out
						.println(Methods.getPrefix() + sType + sId + " is running as well.");
			} catch (Exception exce) {
				System.err.println(Methods.getPrefix() + "fail at the start of " + sType + sId);
				exce.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println(Methods.getPrefix() + sType + sId + " as fail on generation.");
			e.printStackTrace();
		}
	}
	
	public void deleteServer(String sName) {
		File sFolder = new File("/home/minecraft/servers/" + sName);
		System.out.println(Methods.getPrefix() + "Trying to delete sql line " + sName + "...");
		ServerSql.deleteServer(sName);
		System.out.println(Methods.getPrefix() + "Trying to quit screen " + sName + "...");
		try {
			
			Runtime.getRuntime().exec("screen -XS " + sName + " quit");
			System.out.println(Methods.getPrefix() + sName + " has been quit succesfulys !");
			System.out.println(Methods.getPrefix() + "Trying to delete " + sName + "...");
			FileUtils.deleteDirectory(sFolder);
			System.out.println(Methods.getPrefix() + sName + " has been deleted succesfulys !");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Integer generatePort(ServerType serverType) {
		Random r = new Random();
		Integer portmin = null;
		Integer portmax = null;
		Integer randomport = null;
		switch (serverType) {
		case HUB:
			portmin = 25001;
			portmax = 25099;
			randomport = r.nextInt(portmax-portmin) + portmin;
			if(ServerSql.getServersPorts().toString().contains(String.valueOf(randomport))) {
				randomport = generatePort(serverType);
			}
			break;
		case BDB:
			portmin = 25100;
			portmax = 25199;
			randomport = r.nextInt(portmax-portmin) + portmin;
			if(ServerSql.getServersPorts().toString().contains(String.valueOf(randomport))) {
				randomport = generatePort(serverType);
			}
			break;
		default:
			break;
		}
		return randomport;
	}
	
	public Integer generateId(ServerType serverType) {
		Integer finalId = null;
		switch (serverType) {
			case HUB:
				for(int i = 2; i < 100; i++) {
				    if(!ServerSql.getServersNames().toString().contains(serverType.getName() + i)) {
				    	finalId = i;
				        break;
				    }
				}
				break;
			case BDB:
				for(int i = 1; i < 100; i++) {
				    if(!ServerSql.getServersNames().toString().contains(serverType.getName() + i)) {
				    	finalId = i;
				        break;
				    }
				}
				break;
			default:
				break;
		}
		return finalId;
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
				case BDB:
					FileManager.writeFile(sFolder + "/runner.sh", "screen -d -m -S " + sName + " bash -c 'cd " + sFolder.getAbsolutePath() + " && java -Xms128M -Xmx256M -jar spigot.jar'");
					FileManager.writeFile(sFolder + "/server.properties",
							"server-name=" + sName + "\n" + 
							"server-port=" + sPort + "\n" + 
							"motd=" + sName + "\n" + 
							"spawn-protection=0" + "\n" + 
							"max-players=24" + "\n" +
							"allow-nether=false" + "\n" +
							"gamemode=3" + "\n" +
							"spawn-monsters=false" + "\n" +
							"announce-player-achievements=false" + "\n" +
							"pvp=false" + "\n" +
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
