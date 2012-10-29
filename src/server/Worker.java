package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Adrian Tudor PANESCU adrian.panescu@epfl.ch
 * 
 */
public class Worker implements Runnable {

	Socket socket;

	/**
	 * @param socket
	 */
	public Worker(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		// TODO: cleanup

		long lastRequest = System.currentTimeMillis();
		boolean keepAlive = false;

		while (true) {
			try {

				if (socket.isClosed() || socket.isInputShutdown()) {
					break;
				}

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

					// check if we should keep the connection alive
					if (protocol.equals("HTTP/1.1")) {
						if (connection == null
								|| connection.equals("Connection: keep-alive")) {
							keepAlive = true;
						}
					} else if (protocol.equals("HTTP/1.0")) {
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
						break;
					}

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
