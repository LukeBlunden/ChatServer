# ChatServer
@author Luke Blunden  
@version Java 18  

## Description 
A console-based application that allows users to connect to a server and communicate with everyone else who is connected to the same server and port number.

## To Run
The ChatServer application requires a port number command line parameter  
i.e., java ChatServer 55555

The ChatClient application required an IP address and a port number command line parameter, or if connecting with other clients over localhost, just a port number  
i.e., java ChatClient 127.0.0.1 55555  
or  
i.e., java ChatClient 55555

## Features  
![image](https://github.com/user-attachments/assets/53bb3c5a-1aa2-4a86-a184-1bc13e4078ba)

* The ChatServer application uses a ServerSocket to listen for incoming connections on a specified port and creates new Client threads as connections are accepted, which are added to a thread pool. The server also manages the broadcasting of messages received to all clients, the removal of clients as they exit the chat, and its own chat functionality to be able to communicate back to the clients.

* The individual Client threads manage the sending and receiving of messages through their socket connection with the client

* The ChatClient application attempts to connect to a specified IP and port number. Once it’s found a connection it will create 2 threads to manage the reading and writing of messages to and from the server. If the client cannot reach the specified server when it starts, it will reattempt connection before asking the client if they would like to try a different IP and port number.

* If the server loses connection with a client, it will remove that client as if they had left before broadcasting to the remaining clients that they have been disconnected.

* If the clients lose connection with the server, they will be notified that they’ve lost connection to the server before shutting down the application.
