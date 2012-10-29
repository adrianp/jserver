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

	/**
	 * 
	 */
	private Socket socket;

	/**
	 * @param socket
	 */
	public Worker(Socket socket) {
		this.socket = socket;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		// TODO: cleanup

		try {
			InputStreamReader inStream = new InputStreamReader(
					socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(
					socket.getOutputStream());
			BufferedReader in = new BufferedReader(inStream);
			String request = in.readLine();

			if (request != null) {
				RequestHandlers.handleGET(request.split(" ")[1], outStream);
			}

			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
