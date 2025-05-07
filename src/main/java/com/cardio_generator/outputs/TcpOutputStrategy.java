package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Implementation of the OutputStrategy interface that sends patient health data
 * over a TCP connection. This class creates a TCP server that listens on a specified port
 * and sends data to connected clients.
 * 
 * <p>The server accepts a single client connection and sends health data to that client
 * when the {@link #output(int, long, String, String)} method is called. If no client is
 * connected, the output operation is silently ignored.</p>
 * 
 * <p>Note: This implementation does not automatically handle reconnections if a client
 * disconnects. Use the {@link #close()} method to properly release all resources when
 * this output strategy is no longer needed.</p>
 */
public class TcpOutputStrategy implements OutputStrategy {

    /**
     * The server socket that listens for client connections.
     */
    private ServerSocket serverSocket;

    /**
     * The socket representing the connected client.
     */
    private Socket clientSocket;

    /**
     * PrintWriter used to send data to the connected client.
     */
    private PrintWriter out;

    /**
     * Constructs a new TCP output strategy that listens on the specified port.
     * Initializes a server socket and starts a separate thread to accept client connections.
     * 
     * <p>The constructor creates a server socket bound to the specified port and
     * starts a background thread that waits for a client to connect. When a client
     * connects, a PrintWriter is created to send data to that client.</p>
     *
     * @param port The TCP port number to listen on
     * @throws RuntimeException If an IOException occurs while creating the server socket
     *                         or accepting client connections (wrapped in a RuntimeException)
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Outputs patient health data to the connected TCP client.
     * The data is formatted as a comma-separated string in the format: patientId,timestamp,label,data.
     * 
     * <p>This method sends the formatted data to the client if one is connected. If no client
     * is connected (i.e., if the PrintWriter is null), the method silently returns without
     * sending any data or throwing an exception.</p>
     *
     * @param patientId The unique identifier of the patient
     * @param timestamp The time when the data was recorded (in milliseconds since epoch)
     * @param label     A descriptive label for the data (e.g., "ECG", "Blood Pressure")
     * @param data      The actual health data as a string
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }

    /**
     * Closes all resources used by this TCP output strategy.
     * 
     * <p>This method closes the PrintWriter, client socket, and server socket
     * in that order. It handles any IOExceptions that might occur during the
     * closing process by printing the stack trace.</p>
     * 
     * <p>After this method is called, this TcpOutputStrategy instance can no longer
     * be used to send data. Any subsequent calls to {@link #output(int, long, String, String)}
     * will have no effect.</p>
     */
    public void close() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }

            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                clientSocket = null;
            }

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                serverSocket = null;
            }

            System.out.println("TCP Server closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
