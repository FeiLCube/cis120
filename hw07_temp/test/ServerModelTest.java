import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;

public class ServerModelTest {
    private ServerModel model;

    @Before
    public void setUp() {
        // We initialize a fresh ServerModel for each test
        model = new ServerModel();
    }

    /* Here is an example test that checks the functionality of your changeNickname error handling.
     * Each line has commentary directly above it which you can use as a framework for the remainder
     * of your tests.
     */
    @Test
    public void testInvalidNickname() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is invalid.
        Command command = new NicknameCommand(0, "User0", "!nv@l!d!");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create an error Broadcast with our command and an INVALID_NAME error.
        Broadcast expected = Broadcast.error(command, ServerError.INVALID_NAME);

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 1, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("User0"));

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("User with id 0 nickname unchanged", "User0", model.getNickname(0));
    }

    /*
     * Your TAs will be manually grading the tests you write in this file.
     * Don't forget to test both the public methods you have added to your
     * ServerModel class as well as the behavior of the server in different
     * scenarios.
     * You might find it helpful to take a look at the tests we have already
     * provided you with in ChannelsMessagesTest, ConnectionNicknamesTest,
     * and InviteOnlyTest.
     */

    // TODO: Add your own tests here...
    
    //This test should test channel properties, and how it interacts with users.
    @Test
    public void testCreateChannel() {
    	
    	//Register three users
    	model.registerUser(0);
    	model.registerUser(1);
    	model.registerUser(2);
    	
    	//User0 creates a channel with an invalid name.
    	Command command0 = new CreateCommand(0, "User0", "Channel@", false);
    	
    	Broadcast expected0 = Broadcast.error(command0, ServerError.INVALID_NAME);
    	Broadcast actual0 = command0.updateServerModel(model);
    	
    	assertEquals("Broadcast", expected0, actual0);
    	
    	//User0 creates a valid channel, and other users join successfully. This will also test
    	//to see if the channel was created, if the owner is indeed User0, and if User1 joined
    	//successfully.
    	Command command1 = new CreateCommand(0, "User0", "Channel0", false);
    	command1.updateServerModel(model);
    	
    	Command command2 = new JoinCommand(1, "User1", "Channel0");
    	command2.updateServerModel(model);
    	
    	Channel channel0 = model.getChannel("Channel0");
    	
    	assertEquals(1, model.numChannels());
    	assertEquals("User0", channel0.getOwnerNickname());
    	assertEquals(0, channel0.getOwnerId());
    	assertEquals(2, channel0.getUserNicknames().size());
    	
    	//User2 creates a channel0. Tests to see if the command fails because there's already
    	// a channel0.
    	Command command3 = new CreateCommand(2, "User2", "Channel0", false);
    	
    	Broadcast expected3 = Broadcast.error(command3, ServerError.CHANNEL_ALREADY_EXISTS);
    	Broadcast actual3 = command3.updateServerModel(model);
    	
    	assertEquals("Broadcast", expected3, actual3);
    	
    	//User2 attempts to join a non-existent channel.
    	//User2 then joins channel0, then disconnects.
    	//User 2 leaves a channel he's not in.
    	Command command4 = new JoinCommand(2, "User2", "Channel1");
    	Broadcast expected4 = Broadcast.error(command4, ServerError.NO_SUCH_CHANNEL);
    	Broadcast actual4 = command4.updateServerModel(model);
    	assertEquals(expected4, actual4);
    	
    	Command command5 = new JoinCommand(2, "User2", "Channel0");
    	command5.updateServerModel(model);
    	assertEquals(3, channel0.getUserNicknames().size());
    	
    	Command command6 = new LeaveCommand(2, "User2", "Channel0");
    	command6.updateServerModel(model);
    	assertEquals(2, channel0.getUserNicknames().size());
    	command6.updateServerModel(model);
    	Broadcast expected6 = Broadcast.error(command6, ServerError.USER_NOT_IN_CHANNEL);
    	Broadcast actual6 = command6.updateServerModel(model);
    	assertEquals("Broadcast", expected6, actual6);
    	
    	//User0 leaves channel0, thereby deleting the channel.
    	Command command7 = new LeaveCommand(0, "User0", "Channel0");
    	command7.updateServerModel(model);
    	assertEquals(0, model.numChannels());
    	Broadcast expected7 = Broadcast.error(command5, ServerError.NO_SUCH_CHANNEL);
    	Broadcast actual7 = command5.updateServerModel(model);
    	assertEquals("Broadcast", expected7, actual7);
    }
    
    //This test tests the properties of invite only channels, as well as the kick function.
    @Test
    public void testInviteOnly() {

    	//Register three users.
    	model.registerUser(0);
    	model.registerUser(1);
    	model.registerUser(2);
    	
    	//User0 creates an invite-only channel.
    	Command command0 = new CreateCommand(0, "User0", "Channel0", true);
    	command0.updateServerModel(model);
    	
    	//User1 joins the invite-only channel, unsuccessfully.
    	Command command1 = new JoinCommand(1, "User1", "Channel0");
    	Broadcast expected1 = Broadcast.error(command1, ServerError.JOIN_PRIVATE_CHANNEL);
    	Broadcast actual1 = command1.updateServerModel(model);
    	assertEquals("Broadcast", expected1, actual1);
    	
    	//User0 invites User1.
    	Command command2 = new InviteCommand(0, "User0", "Chanel0", "User1");
    	command2.updateServerModel(model);
    	assertEquals(2, model.getChannel("Channel0").getUserNicknames().size());
    }
}
