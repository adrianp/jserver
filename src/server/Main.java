/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Adrian Tudor Panescu adrian.panescu@epfl.ch
 * 
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// TODO: exceptions should be treated inplace, not thrown

		ServerSocket serverSocket = new ServerSocket(Config.PORT);

		while (true) {
			Socket socket = serverSocket.accept();
			InputStreamReader inStream = new InputStreamReader(
					socket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(
					socket.getOutputStream());
			BufferedReader in = new BufferedReader(inStream);
			String request = in.readLine();

			if (request != null) {
				handleGET(request.split(" ")[1], outStream);
			}

			socket.close();
		}

	}

	public static void handleGET(String request, DataOutputStream outStream)
			throws IOException {
		File f = new File("www" + File.separator + request);
		if (f.exists()) {
			DataInputStream din = new DataInputStream(new FileInputStream(f));
			int contentLength = (int) f.length();
			byte[] buffer = new byte[contentLength];
			din.readFully(buffer);
			outStream.writeBytes("HTTP/1.0 200 OK" + Config.CRLF);
			outStream.writeBytes("Content-Length: " + contentLength
					+ Config.CRLF);
			outStream.writeBytes("Content-Type: text/html" + Config.CRLF
					+ Config.CRLF);
			outStream.write(buffer);
			outStream.flush();
			outStream.close();
			din.close();
		} else {
			// TODO: return 404
		}

	}

}
