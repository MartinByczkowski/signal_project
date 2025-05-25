package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of DataReader that connects to a WebSocket server to receive real-time patient data.
 * This class establishes a connection to a WebSocket server, processes incoming messages,
 * and stores the data in the DataStorage system.
 */
public class WebSocketDataReader implements DataReader {
    private final String serverUrl;
    private WebSocketConnection client;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final DataStorage dataStorage;
    private final ScheduledExecutorService reconnectionExecutor;
    private static final int RECONNECTION_DELAY_SECONDS = 5;
    private static final int CONNECTION_TIMEOUT_SECONDS = 10;

    /**
     * Constructs a WebSocketDataReader with the specified server URL and data storage.
     *
     * @param serverUrl    the URL of the WebSocket server to connect to
     * @param dataStorage  the data storage instance to store received data
     */
    public WebSocketDataReader(String serverUrl, DataStorage dataStorage) {
        this.serverUrl = serverUrl;
        this.dataStorage = dataStorage;
        this.reconnectionExecutor = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * Reads data from the WebSocket server and stores it in the provided data storage.
     * This method establishes a connection to the WebSocket server and starts receiving data.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        if (isRunning.get()) {
            return;
        }

        try {
            connectToWebSocket();
            startReceivingData();
        } catch (Exception e) {
            throw new IOException("Failed to initialize WebSocket connection", e);
        }
    }

    /**
     * Connects to the WebSocket server.
     * This method creates a new WebSocketConnection and waits for the connection to be established.
     *
     * @throws Exception if the connection cannot be established
     */
    private void connectToWebSocket() throws Exception {
        CountDownLatch connectionLatch = new CountDownLatch(1);
        try {
            client = new WebSocketConnection(new URI(serverUrl), dataStorage, connectionLatch, this::handleReconnection);
            client.connect();

            // Wait for connection with timeout
            if (!connectionLatch.await(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new IOException("Connection timeout after " + CONNECTION_TIMEOUT_SECONDS + " seconds");
            }
        } catch (URISyntaxException e) {
            throw new IOException("Invalid WebSocket URI: " + serverUrl, e);
        }
    }

    /**
     * Handles reconnection to the WebSocket server when the connection is lost.
     * This method schedules a reconnection attempt after a delay.
     */
    private void handleReconnection() {
        if (isRunning.get()) {
            System.out.println("Scheduling reconnection in " + RECONNECTION_DELAY_SECONDS + " seconds...");
            reconnectionExecutor.schedule(() -> {
                try {
                    System.out.println("Attempting to reconnect to WebSocket server...");
                    connectToWebSocket();
                    System.out.println("Reconnection successful");
                } catch (Exception e) {
                    System.err.println("Reconnection failed: " + e.getMessage());
                    handleReconnection(); // Try again
                }
            }, RECONNECTION_DELAY_SECONDS, TimeUnit.SECONDS);
        }
    }

    /**
     * Starts receiving data from the WebSocket server.
     * This method sets the running flag to true.
     */
    public void startReceivingData() {
        isRunning.set(true);
    }

    /**
     * Stops receiving data from the WebSocket server.
     * This method sets the running flag to false and closes the WebSocket connection.
     */
    public void stopReceivingData() {
        isRunning.set(false);
        if (client != null) {
            client.close();
        }
        reconnectionExecutor.shutdown();
    }

    /**
     * Inner class that extends WebSocketClient to handle WebSocket communication.
     * This class processes incoming messages and stores the data in the DataStorage system.
     */
    private static class WebSocketConnection extends WebSocketClient {
        private final DataStorage dataStorage;
        private final CountDownLatch connectionLatch;
        private final Runnable reconnectionHandler;

        /**
         * Constructs a WebSocketConnection with the specified server URI, data storage, and connection latch.
         *
         * @param serverUri          the URI of the WebSocket server
         * @param dataStorage        the data storage instance to store received data
         * @param connectionLatch    a latch to signal when the connection is established
         * @param reconnectionHandler a handler to call when reconnection is needed
         */
        public WebSocketConnection(URI serverUri, DataStorage dataStorage, CountDownLatch connectionLatch, Runnable reconnectionHandler) {
            super(serverUri);
            this.dataStorage = dataStorage;
            this.connectionLatch = connectionLatch;
            this.reconnectionHandler = reconnectionHandler;
        }

        /**
         * Called when the WebSocket connection is established.
         * This method counts down the connection latch to signal that the connection is ready.
         *
         * @param handshakedata the handshake data from the server
         */
        @Override
        public void onOpen(ServerHandshake handshakedata) {
            connectionLatch.countDown();
            System.out.println("Connected to WebSocket server");
        }

        /**
         * Called when a message is received from the WebSocket server.
         * This method parses the message and stores the data in the DataStorage system.
         *
         * @param message the message received from the server
         */
        @Override
        public void onMessage(String message) {
            try {
                String[] parts = message.split(",");
                if (parts.length == 4) {
                    int patientId = Integer.parseInt(parts[0]);
                    long timestamp = Long.parseLong(parts[1]);
                    String recordType = parts[2];
                    double measurementValue = Double.parseDouble(parts[3]);

                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                } else {
                    System.err.println("Invalid message format: " + message);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing numeric values in message: " + message);
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error processing message: " + message);
                e.printStackTrace();
            }
        }

        /**
         * Called when the WebSocket connection is closed.
         * This method logs the closure and triggers the reconnection handler if needed.
         *
         * @param code   the status code indicating why the connection was closed
         * @param reason a human-readable explanation of why the connection was closed
         * @param remote whether the connection was closed by the remote endpoint
         */
        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("WebSocket connection closed: " + reason + " (code: " + code + ", remote: " + remote + ")");
            if (remote) {
                reconnectionHandler.run();
            }
        }

        /**
         * Called when an error occurs on the WebSocket connection.
         * This method logs the error and triggers the reconnection handler.
         *
         * @param ex the exception that describes the error
         */
        @Override
        public void onError(Exception ex) {
            System.err.println("WebSocket error occurred: " + ex.getMessage());
            ex.printStackTrace();
            reconnectionHandler.run();
        }
    }
}
