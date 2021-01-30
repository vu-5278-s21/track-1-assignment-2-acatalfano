package edu.vanderbilt.cs.live2;

public class GeoHash {
	protected static final double[] LATITUDE_RANGE = { -90, 90 };
	protected static final double[] LONGITUDE_RANGE = { -180, 180 };

	public static boolean[] geohash1D(
		double valueToHash,
		double[] valueRange,
		int bitsOfPrecision
	) {
		geohash1DValidateArgs(valueToHash, valueRange, bitsOfPrecision);

		double normalizedUpperRange = Math.abs(valueRange[1] - valueRange[0]);
		double normalizedValue =
			valueToHash - Math.min(valueRange[0], valueRange[1]);

		return recursiveGeohash1D(normalizedValue, normalizedUpperRange, bitsOfPrecision);
	}

	public static boolean[] geohash2D(
		double v1,
		double[] v1range,
		double v2,
		double[] v2range,
		int bitsOfPrecision
	) {
		int longitudePrecision = bitsOfPrecision / 2;
		boolean[] longitudeGeohash =
			GeoHash.geohash1D(v2, v2range, longitudePrecision);

		int latitudePrecision = (int)Math.ceil(bitsOfPrecision / 2.0);
		boolean[] latitudeGeohash = GeoHash.geohash1D(v1, v1range, latitudePrecision);
		boolean[] geohash = new boolean[bitsOfPrecision];

		for(int srcIndex = 0 , destIndex = 0;
			destIndex < bitsOfPrecision;
			srcIndex++, destIndex += 2
		) {
			geohash[destIndex] = latitudeGeohash[srcIndex];

			if (srcIndex < longitudeGeohash.length) {
				geohash[destIndex + 1] = longitudeGeohash[srcIndex];
			}
		}

		return geohash;
	}

	public static boolean[] geohash(
		double lat,
		double lon,
		int bitsOfPrecision
	) {
		return geohash2D(lat, LATITUDE_RANGE, lon, LONGITUDE_RANGE, bitsOfPrecision);
	}

	private static void geohash1DValidateArgs(
		double valueToHash,
		double[] valueRange,
		int bitsOfPrecision
	) {

		if (valueRange == null || valueRange.length != 2) {
			throw new IllegalArgumentException(
				"value range must be an array of 2 doubles"
			);
		}

		if (bitsOfPrecision < 0) {
			throw new IllegalArgumentException(
				"precision must be a positive integer"
			);
		}

		if (valueToHash < Math.min(valueRange[0], valueRange[1])
			|| valueToHash > Math.max(valueRange[0], valueRange[1])
		) {
			throw new IllegalArgumentException(
				"value must be within the provided range"
			);
		}
	}

	private static boolean[] recursiveGeohash1D(
		double normalizedValue,
		double normalizedUpperRange,
		int precision
	) {
		boolean[] geohash = new boolean[precision];

		if (precision != 0) {
			boolean[] partialGeohash;
			double nextUpperRange = normalizedUpperRange / 2;

			if (normalizedValue >= nextUpperRange) {
				partialGeohash = recursiveGeohash1D(
					normalizedValue - nextUpperRange, nextUpperRange,
					precision - 1
				);
				geohash[0] = true;
			} else {
				partialGeohash = recursiveGeohash1D(
					normalizedValue, nextUpperRange, precision - 1
				);
				geohash[0] = false;
			}
			System.arraycopy(
				partialGeohash, 0, geohash, 1, partialGeohash.length
			);
		} else {
			// BASE CASE -- no additional action
			;
		}
		return geohash;
	}
}
