package model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class NetPaintServer {

	public static final int SERVER_PORT = 9001;

	private static ServerSocket sock;
	private static List<ObjectOutputStream> clients = Collections.synchronizedList(new ArrayList<>());

	public static void main(String[] args) throws IOException {
		sock = new ServerSocket(SERVER_PORT);
		System.out.println("Server started on port " + SERVER_PORT);

		while (true) {
			// done 1: Accept a connection from the ServerSocket.
			Socket s = sock.accept();

			ObjectInputStream is = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

			// done 2: Save the output stream to our clients list so we can
			// broadcast to this client later
			clients.add(os);

			// done 3: Start a new ClientHandler thread for this client.
			ClientHandler c = new ClientHandler(is, clients);
			c.start();

			System.out.println("Accepted a new connection from " + s.getInetAddress());
		}
	}
}

class ClientHandler extends Thread {
	
	Vector<PaintObject> serverShapes = new Vector<PaintObject>();
	ObjectInputStream input;
	List<ObjectOutputStream> clients;

	public ClientHandler(ObjectInputStream input, List<ObjectOutputStream> clients) {
		this.input = input;
		this.clients = clients;
	}

	@Override
	public void run() {
		while (true) {

			PaintObject s = null;

			//  Read shapes from the client
			try {
				s = (PaintObject) input.readObject();
				serverShapes.addElement(s);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				cleanUp();
				return;
			} catch (IOException e) {
				cleanUp();
				return;
			}
			System.out.println("The Server recieved the Shapes " + " from a client.");
			writeShapesToClients(serverShapes);

		}

	}

	private void cleanUp() {
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeShapesToClients(Vector<PaintObject> theShapes) {
		// done 5: Send a string to all clients in the client list
		synchronized(clients){
			Set<ObjectOutputStream> closed = new HashSet<>();
			for (ObjectOutputStream client : clients){
				System.out.println("Writing the shapes " + " to a client");
				try {
					client.reset();
					client.writeObject(theShapes);
					client.reset();
				} catch (IOException e) {
					// can't write? client left chat. remove the client from list
					closed.add(client);
				}
			}
			clients.removeAll(closed);
		}
	}

	// TODO (When you get the chance): Write a method that closes all the
	// resources of a ClientHandler and logs a message, and call it from every
	// place that a fatal error occurs in ClientHandler (the catch blocks that
	// you can't recover from).
}