package net.farugames.servermanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.farugames.data.database.entities.ServerType;
import net.farugames.servermanager.database.Database;
import net.farugames.servermanager.database.Servers;
import net.farugames.servermanager.manager.ServerFileManager;

import java.util.Timer;
import java.util.TimerTask;

public class Main {

	public static Database database;
	
	public static Gson getGson = new GsonBuilder().setPrettyPrinting().create();

	public static void main(String[] args) {

		System.out.println(Methods.getPrefix() + "The request server program has successfully started.");
		
		database = new Database("149.202.102.63","HCK2u7a8Up4d",6379);
		database.connect();
		
		if(!Servers.getServersPorts().contains(25000)) {
			System.out.println(Methods.getPrefix() + "Generation of default hub server...");
			ServerFileManager.getInstance().generateServer(ServerType.HUB, "DEFAULT");
		}
		
		new Timer().schedule(new TimerTask() {
			public void run() {
				System.out.println(Methods.getPrefix() + "Check delete statuts servers on the database.");
				if (Servers.getStatutDelete().size() >= 1) {
					ServerFileManager.getInstance().deleteServer(Servers.getStatutDelete().get(0));
				} else {
					System.out.println(Methods.getPrefix() + "There isn't delete statuts servers.");
				}
				
				System.out.println(Methods.getPrefix() + "Check requested servers on the database.");
				if (Servers.getAllRequestedServers().size() >= 1) {
					switch (Servers.getAllRequestedServers().get(0)) {
					case HUB:
						ServerFileManager.getInstance().generateServer(ServerType.HUB, "random");
						break;
					case BDB:
						ServerFileManager.getInstance().generateServer(ServerType.BDB, "random");
						break;
					default:
						break;
					}
					Servers.removeLastRequest();
				} else {
					System.out.println(Methods.getPrefix() + "There isn't requested server.");
				}
			}
		}, 0, 1000);
	}
}
