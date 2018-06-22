package net.faru.servers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.faru.servers.Main;
import net.faru.servers.ServerType;

public class IServer {

	private static String table = "servers";
	
	public static String getTable() {
		return table;
	}
	
	public static void createServer(ServerType serverType) {
		Integer port = (new Random(5).nextInt(25200 - 25100) + 25200);
		Integer ID = getAvaibleID(serverType);
		try {
			final Connection connection = MySQLManager.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("INSERT INTO " + table + " ("
							+ "server_id,"
							+ " server_name,"
							+ " server_name_id,"
							+ " server_type,"
							+ " server_port"
							+ ") VALUES ("
							+ "?,"
							+ " ?,"
							+ " ?,"
							+ " ?,"
							+ " ?)");
			preparedStatement.setInt(1, getAvaibleID(serverType));
			preparedStatement.setString(2, serverType.getName());
			preparedStatement.setString(3, (serverType.getNameID() + ID));
			preparedStatement.setString(4, serverType.toString());
			preparedStatement.setInt(5, port);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			Main.createServer(serverType, port, ID);
		} catch (SQLException e) {
			System.out.print("");
			System.out.print("[IServer] Connexion à la base de données par la table " + table + " impossible : ");
			e.printStackTrace();
			System.out.print("");
		}
	}
	
	public static List<ResultSet> getServers() {
		List<ResultSet> list = new ArrayList<ResultSet>();
		try {
			final Connection connection = MySQLManager.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("SELECT * FROM " + table);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				list.add(rs);
			}
		} catch (SQLException e) {
			System.out.print("");
			System.out.print("[IServer] Connexion à la base de données par la table " + table + " impossible : ");
			e.printStackTrace();
			System.out.print("");
		}
		return list;
	}
	
	public static List<ResultSet> getServers(ServerType serverType) {
		List<ResultSet> list = new ArrayList<>();
		try {
			final Connection connection = MySQLManager.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("SELECT * FROM " + table + " WHERE server_type = ?");
			preparedStatement.setString(1, serverType.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				list.add(rs);
			}
		} catch (SQLException e) {
			System.out.print("");
			System.out.print("[IServer] Connexion à la base de données par la table " + table + " impossible : ");
			e.printStackTrace();
			System.out.print("");
		}
		return list;
	}
	
	public static Integer getAvaibleID(ServerType serverType) {
		for(ResultSet rs : getServers(serverType)) {
			for(Integer i = 1; i < 8; i++) {
				try {
					if(rs.getInt("server_id") != i) {
						return i;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return 1;
	}
		
	
//	public static void removeServer(String serverNameID) {
//		try {
//			final Connection connection = MySQLManager.getConnection();
//			PreparedStatement preparedStatement = (PreparedStatement) connection
//					.prepareStatement("SELECT server_type FROM " + table + " WHERE server_type = ?");
//			preparedStatement.setString(1, faruServerAPI.getServerType().toString());
//			ResultSet rs = preparedStatement.executeQuery();
//			if(rs.next()) {
//				preparedStatement.close();
//				if(getServers(faruServerAPI.getServerType()).size() > 0) {
//					PreparedStatement preparedStatement1 = (PreparedStatement) connection.prepareStatement("DELETE FROM " + table + " WHERE server_name_id = ?");
//					preparedStatement1.setString(1, faruServerAPI.getServerType().getNameID() + faruServerAPI.getID());
//					preparedStatement1.executeUpdate();
//					preparedStatement1.close();
//				}
//			}
//		} catch (SQLException e) {
//			System.out.print("");
//			System.out.print("[IServer] Connexion à la base de données par la table " + table + " impossible : ");
//			e.printStackTrace();
//			System.out.print("");
//		}
//	}
}
