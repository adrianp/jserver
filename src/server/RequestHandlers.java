package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Adrian Tudor PANESCU adrian.panescu@epfl.ch
 * 
 */
public class RequestHandlers {

	/**
	 * 
	 */
	public static final String CRLF = "\r\n";

	/**
	 * @param request
	 * @param outStream
	 * @throws IOException
	 */
	public static void handleGET(String request, DataOutputStream outStream)
			throws IOException {
		
		// TODO: cleanup
		
		File f = new File("www" + File.separator + request);
		if (f.exists()) {
			DataInputStream din = new DataInputStream(new FileInputStream(f));
			int contentLength = (int) f.length();
			byte[] buffer = new byte[contentLength];
			din.readFully(buffer);
			outStream.writeBytes("HTTP/1.0 200 OK" + RequestHandlers.CRLF);
			outStream.writeBytes("Content-Length: " + contentLength
					+ RequestHandlers.CRLF);
			outStream.writeBytes("Content-Type: text/html"
					+ RequestHandlers.CRLF + RequestHandlers.CRLF);
			outStream.write(buffer);
			outStream.flush();
			outStream.close();
			din.close();
		} else {
			// TODO: return 404
		}

	}

}
