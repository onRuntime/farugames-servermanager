package net.farugames.servermanager;

import java.util.Timer;
import java.util.TimerTask;

import net.farugames.servermanager.database.Database;
import net.farugames.servermanager.database.RequestedServerSql;
import net.farugames.servermanager.database.ServerSql;
import net.farugames.servermanager.manager.ServerFileManager;

public class Main {

	public static Database database;

	public static void main(String[] args) {

		System.out.println(Methods.getPrefix() + "The request server program has successfully started.");
		database = new Database("jdbc:mysql://", "149.202.102.63", "farugames", "proxy", "HCK2u7a8Up4d");
		database.connection();
		
		
		if(!ServerSql.getServersPorts().contains(25000)) {
			System.out.println(Methods.getPrefix() + "Generation of default hub server...");
			ServerFileManager.getInstance().generateServer(ServerType.HUB, "DEFAULT");
		}
		
		new Timer().schedule(new TimerTask() {
			public void run() {
				System.out.println(Methods.getPrefix() + "Check delete statuts servers on the database.");
				if (ServerSql.getStatutDelete().size() >= 1) {
					ServerFileManager.getInstance().deleteServer(ServerSql.getStatutDelete().get(0));
				} else {
					System.out.println(Methods.getPrefix() + "There isn't delete statuts servers.");
				}
				
				System.out.println(Methods.getPrefix() + "Check requested servers on the database.");
				if (RequestedServerSql.getServers().size() >= 1) {
					switch (RequestedServerSql.getServers().get(0)) {
					case HUB:
						ServerFileManager.getInstance().generateServer(ServerType.HUB, "random");
						break;
					case BDB:
						ServerFileManager.getInstance().generateServer(ServerType.BDB, "random");
						break;
					default:
						break;
					}
					RequestedServerSql.removeLastRequested();
				} else {
					System.out.println(Methods.getPrefix() + "There isn't requested server.");
				}
			}
		}, 0, 1000);
	}
}
