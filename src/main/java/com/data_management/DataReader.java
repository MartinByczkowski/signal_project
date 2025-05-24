package com.data_management;

import java.io.IOException;

/**
 * Interface for reading data from various sources into the data storage system.
 * Implementations of this interface can read data from different sources such as
 * files, databases, or network streams.
 */
public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;
}
