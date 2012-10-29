jserver
=======

A lean multi-threaded webserver written in Java, with HTTP keep-alive for 1.1 and 1.0 protocol versions.
========================================================================================================

The files included in the project are:

_Main.java_ - the application entry point

_Worker.java_ - class that implements Runnable, can answer to client requests

_RequestHandlers.java_ - class that generates the answer to requests, can currently handle only GET requests

_Config.java_ - some configuration parameters


The *www* folder is the default root of the webserver.