package net.farugames.servermanager.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.farugames.servermanager.ServerType;

public class RequestedServerSql {

	public static String getTable() {
		return table;
	}

	private static String table = "requested_servers";

	public static List<ServerType> getServers() {
		List<ServerType> list = new ArrayList<ServerType>();
		try {
			final Connection connection = Database.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) connection
					.prepareStatement("SELECT * FROM " + table);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				list.add(ServerType.getServerType(rs.getString("server_type")));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	
	public static void removeLastRequested() {
		try {
			PreparedStatement preparedStatement = (PreparedStatement) Database.getConnection().prepareStatement("DELETE FROM " + table + " LIMIT 1");
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
