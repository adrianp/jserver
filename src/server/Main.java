package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application entry-point
 * 
 * @author Adrian Tudor PANESCU adrian.panescu@epfl.ch
 * 
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("Starting server");

		// the pool of threads that will be used to handle client requests
		ExecutorService service = Executors
				.newFixedThreadPool(Config.MAXIMUM_CLIENTS);

		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(Config.PORT);

		while (true) {
			Socket socket = serverSocket.accept();
			service.execute(new Worker(socket));
		}

	}

}
