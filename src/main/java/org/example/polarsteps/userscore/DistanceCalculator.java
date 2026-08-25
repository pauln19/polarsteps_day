package org.example.polarsteps.userscore;

public class DistanceCalculator {
    private static final double EARTH_RADIUS_KM = 6_371.0088;

    public static double calculateDistanceInKm(double lat1, double lon1, double lat2, double lon2) {

        double dLat = Math.toRadians((lat2 - lat1));
        double dLong = Math.toRadians((lon2 - lon1));

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = haversine(dLat) + Math.cos(lat1) * Math.cos(lat2) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
