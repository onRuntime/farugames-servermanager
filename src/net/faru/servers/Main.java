package net.faru.servers;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import net.faru.servers.database.IRequestServer;
import net.faru.servers.database.IServer;
import net.faru.servers.database.MySQLManager;
import net.faru.servers.files.FileManager;

public class Main {

	private static String PREFIX = "[FaruServerManager] ";

	private static Timer timer = new Timer();

	private static MySQLManager mysql;

	public static void main(String[] args) {
		System.out.println(getPrefix() + "The request server program has successfully started.");

		mysql = new MySQLManager("jdbc:mysql://", "localhost", "farugames", "root","2y6bj7LMu2JT"); 
		mysql.connection();

		timer.schedule(new TimerTask() {
			public void run() {
				for (ServerType serverType : ServerType.values()) {
					if (IServer.getServers(serverType).isEmpty() || IServer.getServers(serverType).size() < 1) {
						IRequestServer.create(serverType);
					}
					for (ResultSet rs : IServer.getServers(ServerType.HUB)) {
						try {
							if (rs.getInt("players") < (serverType.getSlots() / 2)) {
								IRequestServer.create(serverType);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				for (ServerType serverType : IRequestServer.getServers()) {
					IServer.createServer(serverType);
				}
			}
		}, 0, 1000);
	}

	public static void createServer(ServerType serverType, Integer port, Integer ID) throws SQLException {
		try {
			String sType = serverType.getNameID();
			Integer sID = ID;
			File sResult = new File(sType + sID);
			File folder = new File("servers\\" + sResult + "\\");

			File from = new File(serverType.getFolder());
			File to = folder;

			try {
				System.out.println(Main.getPrefix() + "Trying to create " + sType + sID + "...");
				FileManager.copyFolder(from, to);
				System.out.println(Main.getPrefix() + sType + sID + " as been generated succesfuly !");
				System.out.println(Main.getPrefix() + "Trying to generate customs files...");
				try {
					FileManager.writeFile(folder + "/eula.txt", "eula=true");
					FileManager.writeFile(folder + "/server.properties",
							"server-name=" + sResult.toString() + "\n" + "server-port=" + port);

					System.out.println(Main.getPrefix() + sType + sID + " all files as been generated succesfuly !");
					System.out.println(Main.getPrefix() + "Trying to start " + sType + sID + "...");
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec("sh start.sh " + folder);
						System.out
								.println(Main.getPrefix() + sType + sID + " is running as well join on port: " + port);
					} catch (Exception exce) {
						System.err.println(Main.getPrefix() + "The request server program has encoured an error : ");
						exce.printStackTrace();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				System.err.println(
						Main.getPrefix() + "The request server program has encoured an error trying to copy files : ");
				e.printStackTrace();
			}

		} catch (Exception exc) {
			System.err.println(Main.getPrefix() + "The request server program has encoured an error : ");
			exc.printStackTrace();
		}
	}

	public static String getPrefix() {
		return PREFIX;
	}
}
