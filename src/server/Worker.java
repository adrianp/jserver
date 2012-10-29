package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class that handles client requests
 * 
 * @author Adrian Tudor PANESCU adrian.panescu@epfl.ch
 * 
 */
public class Worker implements Runnable {

	private Socket socket;

	/**
	 * @param socket
	 */
	public Worker(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		// when did the last client request happen
		long lastRequest = System.currentTimeMillis();

		// flag for keeping the connection alive; if the client does not
		// support the keep-alive functionality, it should remain false
		boolean keepAlive = false;

		while (true) {
			try {

				// if the socket is no longer functional, exit
				if (socket.isClosed() || socket.isInputShutdown()
						|| socket.isOutputShutdown()) {

					break;
				}

				// if the last request was to long ago, exit
				if (lastRequest + Config.KEEP_ALIVE < System
						.currentTimeMillis()) {
					socket.close();
					break;
				}

				InputStreamReader inStream = new InputStreamReader(
						socket.getInputStream());
				DataOutputStream outStream = new DataOutputStream(
						socket.getOutputStream());
				BufferedReader in = new BufferedReader(inStream);
				String request = in.readLine();

				if (request != null) {
					String[] tokens = request.split(" ");
					String method = tokens[0];
					String path = tokens[1];
					String protocol = tokens[2];
					String connection = null;

					String aux = null;
					do {
						aux = in.readLine();
						if (aux.contains("Connection")) {
							connection = aux;
						}
					} while (aux != null && !aux.equals(""));

					// check the client functionality for keep-alive
					if (protocol.equals("HTTP/1.1")) {
						// the 1.1 version assumes keep-alive by default
						if (connection == null
								|| connection.equals("Connection: keep-alive")) {

							keepAlive = true;

						}
					} else if (protocol.equals("HTTP/1.0")) {
						// for 1.0 the specification is fuzzy; if the client
						// explicitly specifies, we provide keep-alive
						if (connection != null
								&& connection.equals("Connection: keep-alive")) {
							keepAlive = true;
						}
					}

					if (method.equals("GET")) {
						RequestHandlers.handleGET(path, outStream, protocol,
								keepAlive);
					}

					if (!keepAlive) {
						// if the client does no support keep-alive, exit
						break;
					}

					// update the time of the last request
					lastRequest = System.currentTimeMillis();
				}
			} catch (IOException e) {
				e.printStackTrace();
				if (!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				}
				break;
			}
		}
	}

}
