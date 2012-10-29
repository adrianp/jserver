package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

/**
 * Class that generates responses for client requests
 * 
 * @author Adrian Tudor PANESCU adrian.panescu@epfl.ch
 * 
 */
public class RequestHandlers {

	/**
	 * Carriage Return, Line Feed, used for delimiting lines in responses
	 */
	public static final String CRLF = "\r\n";

	/**
	 * Handles GET requests
	 * 
	 * @param request
	 *            the URL
	 * @param outStream
	 *            the output stream to which the response will be written
	 * @param protocol
	 *            HTTP 1.1 or 1.0
	 * @param keepAlive
	 *            true if the connection is kept alive
	 * @throws IOException
	 */
	public static void handleGET(String request, DataOutputStream outStream,
			String protocol, boolean keepAlive) throws IOException {

		File f = new File("www" + File.separator + request);
		StringBuffer header = new StringBuffer();
		byte[] buffer = null;

		if (f.exists() && !f.isDirectory() && f.canRead()) {
			DataInputStream din = new DataInputStream(new FileInputStream(f));
			int contentLength = (int) f.length();
			buffer = new byte[contentLength];
			din.readFully(buffer);
			din.close();

			header.append(protocol + " 200 OK" + CRLF);
			header.append("Content-Length: " + contentLength + CRLF);
			String mime = URLConnection.guessContentTypeFromName(request);
			if (mime == null) {
				mime = "application/octet-stream";
			}
			header.append("Content-Type: " + mime + CRLF);
			if (keepAlive) {
				header.append("Connection: keep-alive" + CRLF);
			} else {
				header.append("Connection: close" + CRLF);
			}
			header.append(CRLF);
		} else {
			String code = "500 Internal Server Error";
			if (!f.exists()) {
				code = "404 Not Found";
			} else {
				code = "403 Forbidden";
			}
			header.append(protocol + " " + code + CRLF);
			header.append("Content-Length: " + code.length() + CRLF);
			header.append("Content-Type: text/plain" + CRLF);
			if (keepAlive) {
				header.append("Connection: keep-alive" + CRLF);
			} else {
				header.append("Connection: close" + CRLF);
			}
			header.append(CRLF);
			header.append(code);
		}

		outStream.write(new String(header).getBytes());
		if (buffer != null) {
			outStream.write(buffer);
		}
		outStream.flush();

	}
}
