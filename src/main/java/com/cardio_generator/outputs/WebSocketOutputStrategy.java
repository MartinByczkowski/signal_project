package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Implementation of the OutputStrategy interface that outputs patient data via WebSocket.
 * This strategy creates a WebSocket server that broadcasts patient data to all connected clients.
 * Clients can connect to the server to receive real-time patient health data.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    /**
     * The WebSocket server instance that handles client connections and message broadcasting.
     */
    private WebSocketServer server;

    /**
     * Constructs a WebSocketOutputStrategy with the specified port.
     * Creates and starts a WebSocket server that listens for client connections on the given port.
     *
     * @param port The port number on which the WebSocket server will listen for connections
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    /**
     * Outputs patient data by broadcasting it to all connected WebSocket clients.
     * The data is formatted as a comma-separated string containing patient ID, timestamp, label, and the actual data.
     *
     * @param patientId The unique identifier of the patient
     * @param timestamp The time when the data was recorded (in milliseconds since epoch)
     * @param label     A descriptive label for the data (e.g., "ECG", "Blood Pressure")
     * @param data      The actual health data as a string
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    /**
     * A simple implementation of WebSocketServer that handles client connections.
     * This inner class manages the WebSocket lifecycle events such as connection opening,
     * closing, message receiving, error handling, and server startup.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {

        /**
         * Constructs a SimpleWebSocketServer with the specified socket address.
         *
         * @param address The InetSocketAddress on which the server will listen
         */
        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        /**
         * Called when a new WebSocket connection is established.
         * Logs the connection information to the console.
         *
         * @param conn      The WebSocket connection that was opened
         * @param handshake The handshake data from the client
         */
        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Called when a WebSocket connection is closed.
         * Logs the connection closure information to the console.
         *
         * @param conn   The WebSocket connection that was closed
         * @param code   The status code indicating why the connection was closed
         * @param reason A human-readable explanation of why the connection was closed
         * @param remote Whether the connection was closed by the remote endpoint (true) or by this endpoint (false)
         */
        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Called when a message is received from a client.
         * This method is not used in the current implementation as the server only broadcasts messages
         * and does not process incoming messages.
         *
         * @param conn    The WebSocket connection that sent the message
         * @param message The message that was received
         */
        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        /**
         * Called when an error occurs on a connection.
         * Prints the stack trace of the exception to standard error.
         *
         * @param conn The WebSocket connection on which the error occurred
         * @param ex   The exception that describes the error
         */
        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        /**
         * Called when the server has successfully started.
         * Logs a success message to the console.
         */
        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
