jserver
=======

A lean multi-threaded webserver written in Java, with HTTP keep-alive for 1.1 and 1.0 protocol versions.
========================================================================================================

The files included in the project are:

*Main.java* - the application entry point

*Worker.java* - class that implements Runnable, can answer to client requests

*RequestHandlers.java* - class that generates the answer to requests, can currently handle only GET requests

*Config.java* - some configuration parameters