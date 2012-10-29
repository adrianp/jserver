package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;

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
	public static void handleGET(String request, DataOutputStream outStream,
			String protocol, boolean keepAlive) throws IOException {

		// TODO: cleanup

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

		} else if (!f.exists()) {
			String notFound = "404 Not Found";
			header.append(protocol + " 404 Not Found" + CRLF);
			header.append("Content-Length: " + notFound.length() + CRLF);
			header.append("Content-Type: text/plain" + CRLF);
			if (keepAlive) {
				header.append("Connection: keep-alive" + CRLF);
			} else {
				header.append("Connection: close" + CRLF);
			}
			header.append(CRLF);
			header.append(notFound);
		} else {
			String forbidden = "403 Forbidden";
			header.append(protocol + " " + forbidden + CRLF);
			header.append("Content-Length: " + forbidden.length() + CRLF);
			header.append("Content-Type: text/plain" + CRLF);
			if (keepAlive) {
				header.append("Connection: keep-alive" + CRLF);
			} else {
				header.append("Connection: close" + CRLF);
			}
			header.append(CRLF);
			header.append(forbidden);
		}

		outStream.write(new String(header).getBytes());
		if (buffer != null) {
			outStream.write(buffer);
		}
		outStream.flush();

	}

}
