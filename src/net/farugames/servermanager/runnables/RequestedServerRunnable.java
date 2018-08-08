package net.farugames.servermanager.runnables;

import java.util.TimerTask;

import net.farugames.api.core.server.ServerType;
import net.farugames.servermanager.Main;

public class RequestedServerRunnable extends TimerTask {
	
	@Override
	public void run() {
		// Check hub on status delete
		Main.checkDeletedServers(ServerType.HUB);
		// Check if there is available server
		Main.checkAvailableServers(ServerType.HUB);
		// Check requested servers
		Main.checkRequestedServers(ServerType.HUB);
		
	}
}
