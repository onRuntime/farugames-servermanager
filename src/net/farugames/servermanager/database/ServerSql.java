package net.farugames.servermanager.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.farugames.servermanager.Methods;

public class ServerSql {

	public static String getTable() {
		return table;
	}

	private static String table = "servers";

	public static List<Integer> getServersPorts() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			final Connection connection = Database.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("SELECT * FROM " + table);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				list.add(rs.getInt("port"));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<String> getStatutDelete() {
		List<String> list = new ArrayList<String>();
		try {
			final Connection connection = Database.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("SELECT * FROM " + table + " WHERE `statut` = 'DELETE'");
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("name"));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<String> getServersNames() {
		List<String> list = new ArrayList<String>();
		try {
			final Connection connection = Database.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("SELECT * FROM " + table);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("name"));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void addServer(String name, String host, Integer port, String mode, String statut) {
        try {
            final Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = (PreparedStatement) connection
                    .prepareStatement("SELECT name FROM " + table + " WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.next()) {
                preparedStatement.close();
                preparedStatement = (PreparedStatement) connection
                        .prepareStatement("INSERT INTO " + table + " (name, host, port, mode, statut, onlineplayers, onlineplayersname) VALUES (?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, host);
                preparedStatement.setInt(3, port);
                preparedStatement.setString(4, mode);
                preparedStatement.setString(5, statut);
                preparedStatement.setInt(6, 0);
                preparedStatement.setString(7, new ArrayList<String>().toString());
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.print("[IServer] Error trying to connect to database : ");
            e.printStackTrace();
        }
    }
	
	public static void deleteServer(String name) {
		try {
			PreparedStatement preparedStatement = (PreparedStatement) Database.getConnection()
					.prepareStatement("DELETE FROM " + table + " WHERE name = ?");
			preparedStatement.setString(1, name);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			System.out.println(Methods.getPrefix() + name + " has been deleted from sql db succesfulys !");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
