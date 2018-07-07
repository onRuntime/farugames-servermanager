package net.farugames.servermanager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.farugames.servermanager.Methods;

public class Database {

	private String urlBase;
	private String host;
	private String database;
	private String username;
	private String password;
	private static Connection connection;

	public Database(String urlBase, String host, String database, String username, String password) {
		this.urlBase = urlBase;
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	@SuppressWarnings("static-access")
	public void connection() {
		if (!isConnected()) {
			try {
				this.connection = DriverManager.getConnection(this.urlBase + this.host + "/" + this.database + "?autoReconnect=true",
						this.username, this.password);
				System.out.println(Methods.getPrefix() + "FaruGamesSM is connected to database.");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	@SuppressWarnings("static-access")
	public void deconnection() {
		if (isConnected()) {
			try {
				this.connection.close();
				return;
			} catch (SQLException e) {
				System.out.println("Deconnection à la base de donées impossible.");
				return;
			}
		}
	}

	@SuppressWarnings("static-access")
	public boolean isConnected() {
		try {
			if ((this.connection == null) || (this.connection.isClosed()) || (this.connection.isValid(5))) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			System.out.println("isConnected class Database erronee");
		}
		return false;
	}

	public static Connection getConnection() {
		return connection;
	}
}
