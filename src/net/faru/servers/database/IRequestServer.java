package net.faru.servers.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.faru.servers.ServerType;

public class IRequestServer {

	private static String table = "requested_servers";
	
	public static String getTable() {
		return table;
	}
	
	public static List<ServerType> getServers() {
		List<ServerType> list = new ArrayList<ServerType>();
		try {
			final Connection connection = MySQLManager.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("SELECT * FROM " + table);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				list.add(ServerType.getServerType(rs.getString("server_type")));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			System.out.print("");
			System.out.print("[IServer] Connexion à la base de données par la table " + table + " impossible : ");
			e.printStackTrace();
			System.out.print("");
		}
		return list;
	}
	
	public static void create(ServerType serverType) {
		try {
			final Connection connection = MySQLManager.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("INSERT INTO " + table + " (server_type) VALUES (?)");
			preparedStatement.setString(1, serverType.toString());
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			System.out.print("");
			System.out.print("[IServer] Connexion à la base de données par la table " + table + " impossible : ");
			e.printStackTrace();
			System.out.print("");
		}
	}
}
