import java.util.*;


/**
 * The {@code ServerModel} is the class responsible for tracking the
 * state of the server, including its current users and the channels
 * they are in.
 * This class is used by subclasses of {@link Command} to:
 *     1. handle commands from clients, and
 *     2. handle commands from {@link ServerBackend} to coordinate 
 *        client connection/disconnection. 
 */
public final class ServerModel implements ServerModelApi {
    
	private Map<Integer, String> clientData = new TreeMap<Integer, String>();
	private Set<Channel> channels = new TreeSet<Channel>();

    /**
     * Constructs a {@code ServerModel} and initializes any
     * collections needed for modeling the server state.
     */
    public ServerModel() {
        // TODO: Initialize your state here
    }


    //==========================================================================
    // Client connection handlers
    //==========================================================================

    /**
     * Informs the model that a client has connected to the server
     * with the given user ID. The model should update its state so
     * that it can identify this user during later interactions. The
     * newly connected user will not yet have had the chance to set a
     * nickname, and so the model should provide a default nickname
     * for the user.  Any user who is registered with the server
     * (without being later deregistered) should appear in the output
     * of {@link #getRegisteredUsers()}.
     *
     * @param userId The unique ID created by the backend to represent this user
     * @return A {@link Broadcast} to the user with their new nickname
     */
    public Broadcast registerUser(int userId) {
    	
        String nickname = generateUniqueNickname();
        clientData.put(userId, nickname);
        return Broadcast.connected(nickname);
    }

    /**
     * Generates a unique nickname of the form "UserX", where X is the
     * smallest non-negative integer that yields a unique nickname for a user.
     * @return the generated nickname
     */
    private String generateUniqueNickname() {
        int suffix = 0;
        String nickname;
        Collection<String> existingUsers = getRegisteredUsers();
        do {
            nickname = "User" + suffix++;
        } while (existingUsers != null && existingUsers.contains(nickname));
        return nickname;
    }

    /**
     * Determines if a given nickname is valid or invalid (contains at least
     * one alphanumeric character, and no non-alphanumeric characters).
     * @param name The channel or nickname string to validate
     * @return true if the string is a valid name
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Informs the model that the client with the given user ID has
     * disconnected from the server.  After a user ID is deregistered,
     * the server backend is free to reassign this user ID to an
     * entirely different client; as such, the model should remove all
     * state of the user associated with the deregistered user ID. The
     * behavior of this method if the given user ID is not registered
     * with the model is undefined.  Any user who is deregistered
     * (without later being registered) should not appear in the
     * output of {@link #getRegisteredUsers()}.
     *
     * @param userId The unique ID of the user to deregister
     * @return A {@link Broadcast} instructing clients to remove the
     * user from all channels
     */
    public Broadcast deregisterUser(int userId) {
    	
    	String removedUser = clientData.get(userId);
    	Set<String> recipients = new TreeSet<String>();
        clientData.remove(userId);
        
        for (Channel channel : channels) {
        	
        	if (channel.contains(userId)) {
            	channel.removeUser(userId);
            	
            	if (channel.getOwnerNickname().equals(removedUser)) {
            		channels.remove(channel);
            		continue;
            	}
            	for (String userNicknames : channel.getUserNicknames()) {
                	recipients.add(userNicknames);
            	}
        	}
        	
        }
        
        return Broadcast.disconnected(removedUser, recipients);
    }

    

    //==========================================================================
    // Model update functions
    //==========================================================================

    public ServerError createChannel(String channelName, String owner, boolean inviteOnly) {
    	
    	for (Channel channel : channels) {
    		if (channel.getChannelName().equals(channelName)) {
    			return ServerError.CHANNEL_ALREADY_EXISTS;
    		}
    	}
        if (!isValidName(channelName)) {
        	return ServerError.INVALID_NAME;
        }
        
        int ownerId = getUserId(owner);
        Channel newChannel = new Channel(channelName, ownerId, owner, inviteOnly, numChannels());
        channels.add(newChannel);
        return ServerError.OKAY;
    }

    public ServerError addUserToChannel(String userName, String channelName) {
    	
    	Channel tempChannel = null;
    	
    	boolean channelExists = false;
    	for (Channel channel : channels) {
    		if (channel.getChannelName().equals(channelName)) {
    			channelExists = true;
    			tempChannel = channel;
    		}
    	}
        if (!channelExists) {
        	return ServerError.NO_SUCH_CHANNEL;
        }
        
        int userId = getUserId(userName);
        tempChannel.addUser(userId, userName);
        
        return ServerError.OKAY;
    }

    public ServerError removeUserFromChannel(String userName, String channelName) {
    	
    	Channel tempChannel = null;
    	boolean channelExists = false;
    	
    	for (Channel channel : channels) {
    		if (channel.getChannelName().equals(channelName)) {
    			channelExists = true;
    			tempChannel = channel;
    		}
    	}
    	
    	if (!channelExists) {
    		return ServerError.NO_SUCH_CHANNEL;
    	}
    	
    	boolean userInChannel = false;
    	Collection<String> channelUsers = tempChannel.getUserNicknames();
    	for (String users : channelUsers) {
    		if (users.equals(userName)) {
    			userInChannel = true;
    		}
    	}
    	
    	if (!userInChannel) {
    		return ServerError.USER_NOT_IN_CHANNEL;
    	}
    	
    	if (userName == tempChannel.getOwnerNickname()) {
    		tempChannel.clearUsers();
    		channels.remove(tempChannel);
    	} else {
    		tempChannel.removeUser(userName);
    	}
    	
        return ServerError.OKAY;
    }

    public Broadcast changeNickname(NicknameCommand command) {
    	Set<String> recipients = new TreeSet<String>();
    	int userId = command.getSenderId();
    	String newNickname = command.getNewNickname();
    	
        if (clientData.containsValue(newNickname)) {
        	return Broadcast.error(command, ServerError.NAME_ALREADY_IN_USE);
        }
        
        if (!isValidName(newNickname)) {
        	return Broadcast.error(command, ServerError.INVALID_NAME);
        }
        
        clientData.put(userId, newNickname);
        
        for (Channel channel: channels) {
        	if (channel.contains(userId)) {
        		channel.changeNickname(userId, newNickname);
        		for (String names : channel.getUserNicknames()) {
        			recipients.add(names);
        		}
        	}
        }
        
        return Broadcast.okay(command, recipients);
        
    }

    public boolean isPublicChannel(String channelName) {
        // TODO: Write me
        return false;
    }

    //==========================================================================
    // Server model queries
    // These functions provide helpful ways to test the state of your model.
    // You may also use them in your implementation.
    //==========================================================================

    /**
     * Gets the user ID currently associated with the given
     * nickname. The returned ID is -1 if the nickname is not
     * currently in use.
     *
     * @param nickname The nickname for which to get the associated user ID
     * @return The user ID of the user with the argued nickname if
     * such a user exists, otherwise -1
     */
    public int getUserId(String nickname) {
    	int id = -1;
        if (clientData.containsValue(nickname)) {
        	Set<Map.Entry<Integer, String>> clientSet = clientData.entrySet();
        	for (Map.Entry<Integer, String> ids : clientSet) {
        		if ((ids.getValue()).equals(nickname)) {
        			return ids.getKey();
        		}
        	}
        }
        return id;
    }

    /**
     * Gets the nickname currently associated with the given user
     * ID. The returned nickname is null if the user ID is not
     * currently in use.
     *
     * @param userId The user ID for which to get the associated
     *        nickname
     * @return The nickname of the user with the argued user ID if
     *          such a user exists, otherwise null
     */
    public String getNickname(int userId) {
        if (clientData.containsKey(userId)) {
        	return clientData.get(userId);
        } else {
        	return null;
        }
    }

    /**
     * Gets a collection of the nicknames of all users who are
     * registered with the server. Changes to the returned collection
     * should not affect the server state.
     * 
     * This method is provided for testing.
     *
     * @return The collection of registered user nicknames
     */
    public Collection<String> getRegisteredUsers() {
    	return clientData.values();
    }

    /**
     * Gets a collection of the names of all the channels that are
     * present on the server. Changes to the returned collection
     * should not affect the server state.
     * 
     * This method is provided for testing.
     *
     * @return The collection of channel names
     */
    public Collection<String> getChannels() {
    	Collection<String> channelSet = new TreeSet<String>();
    	
        for (Channel channel : channels) {
        	channelSet.add(channel.getChannelName());
        }
        
        return channelSet;
    }
    
// Added these functions myself.
    public Channel getChannel(String channelName) {
    	for (Channel channel : channels) {
    		if (channelName.equals(channel.getChannelName())) {
    			return channel;
    		}
    	}
    	return null;
    }
    
    public int numChannels() {
    	return channels.size();
    }

    /**
     * Gets a collection of the nicknames of all the users in a given
     * channel. The collection is empty if no channel with the given
     * name exists. Modifications to the returned collection should
     * not affect the server state.
     *
     * This method is provided for testing.
     *
     * @param channelName The channel for which to get member nicknames
     * @return The collection of user nicknames in the argued channel
     */
    public Collection<String> getUsersInChannel(String channelName) {
        Collection<String> channelUsers = new TreeSet<String>();
        
        for (Channel channel : channels) {
        	if (channelName.equals(channel.getChannelName())) {
    			channelUsers = channel.getUserNicknames();
    		}
        }
        
        return channelUsers;
    }

    /**
     * Gets the nickname of the owner of the given channel. The result
     * is {@code null} if no channel with the given name exists.
     *
     * This method is provided for testing.
     *
     * @param channelName The channel for which to get the owner nickname
     * @return The nickname of the channel owner if such a channel
     * exists, otherwise null
     */
    public String getOwner(String channelName) {
    	String owner = null;
    	
    	for (Channel channel : channels) {
    		if (channelName.equals(channel.getChannelName())) {
    			owner = channel.getOwnerNickname();
    		}
    	}
    	return owner;
    }

}
