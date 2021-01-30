package edu.vanderbilt.cs.live3;

import static edu.vanderbilt.cs.live2.GeoHash.geohash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GeoDBFactory {

    public static GeoDB newDatabase(int bitsOfPrecision) {
        return new GeoDBFactory.GeoTree(bitsOfPrecision);
    }

    private static class GeoTree implements GeoDB {
        private class GeohashEntry {
            public final double latitude;
            public final double longitude;

            public GeohashEntry(
                double lat,
                double lng
            ) {
                latitude = lat;
                longitude = lng;
            }

            public double[] valueAsArray() {
                return new double[] { latitude, longitude };
            }
        }

        private final int resolution;
        private final List<GeohashEntry> geoTree;

        public GeoTree(int bitsOfPrecision) {
            resolution = bitsOfPrecision;
            geoTree = Arrays.asList(new GeohashEntry[(int)Math.pow(2, resolution)]);
        }

        @Override
        public void insert(
            double lat,
            double lon
        ) {
            int index = index(lat, lon);
            geoTree.set(index, new GeohashEntry(lat, lon));
        }

        @Override
        public boolean delete(
            double lat,
            double lon
        ) {
            int index = index(lat, lon);
            boolean geohashIsInDB = geoTree.get(index) != null;
            geoTree.set(index, null);
            return geohashIsInDB;
        }

        @Override
        public List<double[]> deleteAll(
            double lat,
            double lon,
            int bitsOfPrecision
        ) {
            int rangeStartIndex = rangeStartIndex(lat, lon, bitsOfPrecision);
            int rangeLength = rangeLength(bitsOfPrecision);
            List<double[]> deletedEntries =
                geoTree.stream()
                    .skip(rangeStartIndex)
                    .limit(rangeLength)
                    .filter(Objects::nonNull)
                    .map(GeohashEntry::valueAsArray)
                    .collect(Collectors.toList());

            for(int i = rangeStartIndex; i < rangeStartIndex + rangeLength; i++) {
                geoTree.set(i, null);
            }
            return deletedEntries;
        }

        @Override
        public boolean contains(
            double lat,
            double lon,
            int bitsOfPrecision
        ) {
            int rangeStartIndex = rangeStartIndex(lat, lon, bitsOfPrecision);
            int rangeLength = rangeLength(bitsOfPrecision);
            return geoTree.stream()
                .skip(rangeStartIndex)
                .limit(rangeLength)
                .anyMatch(Objects::nonNull);
        }

        @Override
        public List<double[]> nearby(
            double lat,
            double lon,
            int bitsOfPrecision
        ) {
            int rangeStartIndex = rangeStartIndex(lat, lon, bitsOfPrecision);
            int rangeLength = rangeLength(bitsOfPrecision);
            return geoTree.stream()
                .skip(rangeStartIndex)
                .limit(rangeLength)
                .filter(Objects::nonNull)
                .map(GeohashEntry::valueAsArray)
                .collect(Collectors.toList());
        }

        private int index(
            double latitude,
            double longitude
        ) {
            return rangeStartIndex(latitude, longitude, resolution);
        }

        private int rangeStartIndex(
            double latitude,
            double longitude,
            int precision
        ) {
            String geohashString =
                zeroPaddedGeohashString(latitude, longitude, precision);
            return Integer.parseInt(geohashString, 2);
        }

        private int rangeLength(
            int precision
        ) {
            return (int)Math.pow(2, (resolution - precision));
        }

        private String zeroPaddedGeohashString(
            double latitude,
            double longitude,
            int precision
        ) {
            boolean[] unpaddedGeohash = geohash(latitude, longitude, precision);
            boolean[] paddedGeohash = new boolean[resolution];
            System.arraycopy(unpaddedGeohash, 0, paddedGeohash, 0, precision);

            StringBuilder paddedGeohashString = new StringBuilder();
            for(boolean bit : paddedGeohash) {
                paddedGeohashString.append(bit ? '1' : '0');
            }
            return paddedGeohashString.toString();
        }
    }

}
