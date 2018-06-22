package net.faru.servers;

public enum ServerType {

	//PROXY("Proxy", "pxy#", "\\template\\proxy\\", null),
	HUB("Hub", "hub#", "\\home\\minecraft\\template\\hub\\", 100),
	
	BUILD_BATTLE("BuildBattle", "bdb#", "\\home\\minecraft\\template\\buildbattle\\", 16);
	
	private String name;
	private String nameID;
	private String folder;
	
	private Integer slots;
	
	ServerType(String name, String nameID, String folder, Integer slots) {
		this.nameID = nameID;
		this.name = name;
		this.folder = folder;
		this.slots = slots;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNameID() {
		return this.nameID;
	}
	
	public String getFolder() {
		return this.folder;
	}
	
	public Integer getSlots() {
		return this.slots;
	}
	
	public static ServerType getServerType(String name) {
		for(ServerType serverType : ServerType.values()) {
			if(serverType.getName().equalsIgnoreCase(name)) {
				return serverType;
			}
		}
		return null;
	}
}
