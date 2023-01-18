import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private final int port;
    private ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    private final Set<Client> clients = new HashSet<>();
    private boolean keepRunning = true;

    public ChatServer(int port){ this.port = port; }

    public static void main(String[] args) {
        // Error if no program arguments are used.
        if(args.length < 1){
            System.out.println("Include port number as 1st program argument");
        } else {
            // Gets port number from 1st program argument.
            try{
                int port = Integer.parseInt(args[0].trim());
                new ChatServer(port).startServer();
            } catch (NumberFormatException e) {
                // Error if port number is not 1st argument
                System.out.println("Invalid program argument. Usage: java ChatServer [portNumber]");
            }
        }
    }

    // Starts server listening for connections on specified port
    private void startServer(){
        System.out.println("Searching for requests on port " + port);
        try {
            // ServerSocket not in try-with-resources to prevent auto-close
            serverSocket = new ServerSocket(port);
            // Begins the thread for server to chat with clients
            executor.execute(() -> startServerChat());
            System.out.println("Type \\q to quit");
            // Runs until \q is entered
            while(keepRunning){
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connection to client at " + clientSocket.getInetAddress() + " accepted");
                    // Creates a new client thread, adds to executor pool and starts
                    Client client = new Client(clientSocket, this);
                    clients.add(client);
                    executor.execute(client);
                } catch (IOException e) {
                    System.out.println("Server shutdown");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Removes client from Client set
    protected void removeUser(Client client){ clients.remove(client); }


    protected void send(String message, Client sender){
        // Prints messages to server console
        System.out.println(message);
        // Broadcasts messages to all clients excluding themselves
        for(Client client : clients){
            if(client != sender) client.receive(message);
        }
    }

    // Server chat method to be initiliaed in own thread
    private void startServerChat(){
        try(BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            String message;
            // Runs until server inputs \q
            while(!(message = input.readLine()).equals("\\q")) {
                send("Server: " + message);
            }
            send("Server shutting down");
            keepRunning = false;
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Sends messages from the server to all clients
    private void send(String message){
        for (Client client : clients) client.receive(message);
    }
}