package server;

/**
 * Class that holds the configuration parameters used across the application
 * 
 * @author Adrian Tudor PANESCU adrian.panescu@epfl.ch
 * 
 */
public final class Config {

	/**
	 * The port on which the server runs
	 */
	public static final int PORT = 8888;

	/**
	 * The path to where the documents served by the server are stored
	 */
	public static final String DOCUMENT_ROOT = "www";

	/**
	 * How many clients can we concurrently serve
	 */
	public static final int MAXIMUM_CLIENTS = 5;

	/**
	 * For how long should we keep connections alive
	 */
	public static final long KEEP_ALIVE = 5 * 1000;

}
