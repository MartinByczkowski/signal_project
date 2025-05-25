package com.data_management;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A mock WebSocket server for testing the WebSocketDataReader.
 * This server simulates a real WebSocket server by sending test data to connected clients.
 */
public class MockWebSocketServer extends WebSocketServer {
    private final List<WebSocket> connections = new ArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private boolean sendingData = false;

    /**
     * Constructs a MockWebSocketServer with the specified port.
     *
     * @param port the port on which the server will listen
     */
    public MockWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    /**
     * Called when a new WebSocket connection is established.
     * Adds the connection to the list of connections and starts sending test data.
     *
     * @param conn the WebSocket connection
     * @param handshake the client handshake
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        System.out.println("New connection established in mock server: " + conn.getRemoteSocketAddress());

        if (!sendingData) {
            startSendingTestData();
        }
    }

    /**
     * Called when a WebSocket connection is closed.
     * Removes the connection from the list of connections.
     *
     * @param conn the WebSocket connection
     * @param code the status code
     * @param reason the reason for closing
     * @param remote whether the connection was closed by the remote endpoint
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        System.out.println("Connection closed in mock server: " + conn.getRemoteSocketAddress());
    }

    /**
     * Called when a message is received from a client.
     * Not used in this mock server.
     *
     * @param conn the WebSocket connection
     * @param message the message
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        // Not used in this mock server
    }

    /**
     * Called when an error occurs on a connection.
     * Prints the error to the console.
     *
     * @param conn the WebSocket connection
     * @param ex the exception
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error in mock server: " + ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Called when the server starts.
     * Prints a message to the console.
     */
    @Override
    public void onStart() {
        System.out.println("Mock WebSocket server started on port: " + getPort());
    }

    /**
     * Starts sending test data to all connected clients.
     * Sends a new message every 500 milliseconds.
     */
    private void startSendingTestData() {
        sendingData = true;

        // Send test data every 500ms
        executor.scheduleAtFixedRate(() -> {
            if (!connections.isEmpty()) {
                // Send different types of test data
                sendTestData(1, System.currentTimeMillis(), "HeartRate", "85.5");
                sendTestData(1, System.currentTimeMillis(), "BloodPressure", "120/80");
                sendTestData(2, System.currentTimeMillis(), "HeartRate", "72.0");
                sendTestData(2, System.currentTimeMillis(), "OxygenSaturation", "98.5");
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * Sends test data to all connected clients.
     *
     * @param patientId the patient ID
     * @param timestamp the timestamp
     * @param label the label (record type)
     * @param data the data value
     */
    private void sendTestData(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        for (WebSocket conn : connections) {
            conn.send(message);
        }
    }

    /**
     * Stops the server and cleans up resources.
     * 
     * @throws InterruptedException if the server is interrupted while stopping
     */
    @Override
    public void stop() throws InterruptedException {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.err.println("Executor shutdown interrupted: " + e.getMessage());
            throw e; // Re-throw the exception
        }

        try {
            super.stop();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Server stop interrupted: " + e.getMessage());
            throw e; // Re-throw the exception
        } catch (Exception e) {
            System.err.println("Error stopping server: " + e.getMessage());
            throw new RuntimeException("Error stopping server", e);
        }
    }
}
