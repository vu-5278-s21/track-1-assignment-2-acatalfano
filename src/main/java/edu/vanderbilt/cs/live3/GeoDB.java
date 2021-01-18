package edu.vanderbilt.cs.live3;

import java.util.List;

/**
 * Using your GeoHash implementation, create an implementation of this GeoDB
 * interface that allows you to insert, delete, and perform proximity searches
 * on latitude, longitude coordinate pairs.
 *
 * There are a LOT of ways to implement this. Future assignments will build off
 * of this implementation and also introduce new or change requirements. You will
 * have to maintain this code and refactor it as needed over the course of the
 * class.
 *
 * Your solution should provide better than O(n) lookup time for nearby(..) and
 * contains(..). This means that a list will not work. If your solution uses
 * a 'for loop' to iterate through hashes and check for matching prefixes when
 * you call nearby(), then it needs to be redesigned.
 *
 * You will also need to update the GeoDBFactory.
 *
 * There will be ambiguous requirements. Use your best judgement in determining
 * an appropriate interpretation. Keep these ambiguities in mind when you code
 * review others' solutions later in class.
 *
 */
public interface GeoDB {

    /**
     *
     * Your GeoDB implementation should take the maximum
     * number of bits of precision as a constructor parameter.
     * When you call this insert method, it should use the
     * maximum bits of precision when calculating the geohash
     * for the inserted data.
     *
     * Inserts a location into the database. No
     * duplicates are stored. If the position is already
     * present, it should be overwritten.
     *
     * @param lat
     * @param lon
     */
    public void insert(double lat, double lon);

    /**
     * Deletes the specified location from the database.
     *
     * Your GeoDB implementation should take the maximum
     * number of bits of precision as a constructor parameter.
     * When you call this insert method, it should use the
     * maximum bits of precision when calculating the geohash
     * to search for to delete the associated location(s).
     *
     * Returns true if an item was deleted.
     *
     *
     * @param lat
     * @param lon
     */
    public boolean delete(double lat, double lon);

    /**
     * Deletes all locations from the database that
     * match the provided latitude and longitude
     * up to the specified number of bits of precision
     * in their geohashes.
     *
     * For example, if you are using 3 bits of precision,
     * then the following two geohashes match:
     *
     * 0100001 => 010
     * 0101111 => 010
     *
     * With 4 bits of precision, they don't match:
     *
     * 0100001 => 0100
     * 0101111 => 0101
     *
     * Returns the list of deleted locations.
     *
     * If bitsOfPrecision == 0, then this method should delete everything.
     *
     * @param lat
     * @param lon
     */
    public List<double[]> deleteAll(double lat, double lon, int bitsOfPrecision);

    /**
     * Returns true if the database contains at least one location that
     * matches the provided latitude and longitude
     * up to the specified number of bits of precision
     * in its geohash.
     *
     * For example, if you are using 3 bits of precision,
     * then the following two geohashes match:
     *
     * 0100001 => 010
     * 0101111 => 010
     *
     * With 4 bits of precision, they don't match:
     *
     * 0100001 => 0100
     * 0101111 => 0101
     *
     * If bitsOfPrecision == 0, then this method should always return true.
     *
     * @param lat
     * @param lon
     */
    public boolean contains(double lat, double lon, int bitsOfPrecision);

    /**
     * Returns all locations in the database that
     * match the provided latitude and longitude
     * up to the specified number of bits of precision
     * in their geohashes.
     *
     * For example, if you are using 3 bits of precision,
     * then the following two geohashes match:
     *
     * 0100001 => 010
     * 0101111 => 010
     *
     * With 4 bits of precision, they don't match:
     *
     * 0100001 => 0100
     * 0101111 => 0101
     *
     * If bitsOfPrecision == 0, then this method should always return everything
     * in the database.
     *
     * @param lat
     * @param lon
     */
    public List<double[]> nearby(double lat, double lon, int bitsOfPrecision);
}
