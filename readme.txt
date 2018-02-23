Author: Dominick Taylor
Read me file for the Bulletin Board program. 

This program is used to test the understanding of basic cryptography. Using the
SHA-256 hashing protocol and the HMAC encyption scheme, send a message from 
the 'user' Post object, to the 'server' Bulletin Board object. The message is
encypted by the Post using user defined 16-hex byte key. The key and message
are sent over the network using UDP at the transport layer. The Bulletin Board
key (also specified by the user) is then used to verify that the message has
not been tampered with and prints the message. 

Execution:

	$> java BulletinBoard <bhost> <bport> <key>

	bhost should be a registered hostname for UDP connection.
	bport should be the port number for the connection.
	key should be a 16-hex byte ([0-9][a-f]) key to use.

	$> java Post <phost> <pport> <bhost> <bport> <key> <message>

	phost should the hostname for the computer you are using to send the
	    message.
	pport should the the port to use for this message.
	bhost and bport should be whatever you entered to invoke the Bulletin
	    Board object. 
	key should be a 16-hex byte sequence ([0-9][a-f]) that matches the
	    one used by the Bulletin Board.
	message can be any message you'd like to send. Note: The buffer used
	    for transport is limited to 1024 bytes for this project.

Note on execution:

	The commands above, even when used correctly, will still fail! This is
	due to this project's reliance on a class used within the RIT pj2
	library. In order to run an execute this code, you should download the
	full package (easily googled/binged via "rit pj2"). Contact
	ark.cs.rit.edu with concerns, however he does not provide technical
	support. 

Regards:

	This project uses the RIT pj2 library, a creation of Professor Alan
	Kaminsky at the Rochester Institute of Technology. The software is open
	source and available to download. The usage within this project is limited
	to the SHA-256 hashing algorithm and the Hex class, each vital to the
	project.


