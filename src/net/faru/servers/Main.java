package net.faru.servers;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import net.faru.servers.files.FileManager;

public class Main {

	private static String PREFIX = "[FaruServerManager] ";

	public static void main(String[] args) {
		try {
			System.out.println(getPrefix() + "The request server program has successfully started.");

			String sType = "hub";
			File sID = new File(FileManager.generate(5));
			File sResult = new File(sType + sID);
			File folder = new File("servers\\" + sResult);

			File from = new File("default-skript");
			File to = folder;

			try {
				System.out.println(getPrefix() + "Trying to create " + sType + sID + "...");
				FileManager.copyFolder(from, to);
				System.out.println(getPrefix() + sType + sID + " as been generated succesfuly !");
				System.out.println(getPrefix() + "Trying to generate customs files...");
				try {
					int port = (new Random(5).nextInt(25200 - 25100) + 25200);
					FileManager.writeFile(folder + "/eula.txt", "eula=true");
					FileManager.writeFile(folder + "/server.properties",
							"server-name=" + sResult.toString() + "\n" + "server-port=" + port);
					FileManager.writeFile(folder + "/start.bat", "cd C:\\test\\" + folder + "\n" + "java -jar -Xms512M -Xmx512M spigot.jar");
					System.out.println(getPrefix() + sType + sID + " all files as been generated succesfuly !");
					System.out.println(getPrefix() + "Trying to start " + sType + sID + "...");
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec("cmd /c start \"\" C:\\test\\" + folder + "\\start.bat");
						System.out.println(getPrefix() + sType + sID + " is running as well join on port: " + port);
					} catch (Exception exce) {
						System.err.println(getPrefix() + "The request server program has encoured an error : ");
						exce.printStackTrace();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				System.err.println(
						getPrefix() + "The request server program has encoured an error trying to copy files : ");
				e.printStackTrace();
			}

		} catch (Exception exc) {
			System.err.println(getPrefix() + "The request server program has encoured an error : ");
			exc.printStackTrace();
		}
	}

	public static String getPrefix() {
		return PREFIX;
	}
}
