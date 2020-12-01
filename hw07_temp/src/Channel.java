import java.util.*;

public class Channel implements Comparable<Channel> {
	
	private int channelID;
	private String name;
	private Map<Integer, String> nicknamesOfUsers = new TreeMap<Integer, String>();
	private final int ownerID;
	private final boolean inviteOnly;
	
// Constructor
	Channel(String name, int ownerId, String ownerNickname, boolean inviteOnly, int numOfChannels) {
		this.name = name;
		this.ownerID = ownerId;
		this.inviteOnly = inviteOnly;
		nicknamesOfUsers.put(ownerId, ownerNickname);
		this.channelID = numOfChannels + 1;
	}

// Add user
	public void addUser(int userId, String userNickname) {
		nicknamesOfUsers.put(userId, userNickname);
	}
	
/* Functions to help other classes */
	public void clearUsers() {
		this.nicknamesOfUsers.clear();
	}
	
	public void removeUser(int userId) {
		nicknamesOfUsers.remove(userId);
	}
	
	public void removeUser(String userNickname) {
		Set<Map.Entry<Integer, String>> userSet = nicknamesOfUsers.entrySet();
		for (Map.Entry<Integer, String> user : userSet) {
			if (user.getValue().equals(userNickname)) {
				nicknamesOfUsers.remove(user.getKey());
			}
		}
	}
	
	public void changeNickname(int userId, String newNickname) {
		nicknamesOfUsers.put(userId, newNickname);
	}
	
	public boolean contains(int userId) {
		return nicknamesOfUsers.containsKey(userId);
	}
	
	public String getChannelName() {
		return this.name;
	}
	
	public Collection<String> getUserNicknames() {
		return this.nicknamesOfUsers.values();
	}
	
	public String getOwnerNickname() {
		return nicknamesOfUsers.get(ownerID);
	}
	
	public int getOwnerId() {
		return ownerID;
	}
	
	public boolean inviteOnly() {
		return inviteOnly;
	}

	@Override
	public int compareTo(Channel c) {
		if (this.channelID < c.channelID) {
	    	return -1;
		} else if (this.channelID > c.channelID) {
	    	return 1;
	    } else {
	    	return 0;
	    }
	}
}
