package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.controllers;

public class LocationIdManager {
    public int getLocationId(String location) {
        switch (location) {
            case "Glasgow":
                return 2648579;
            case "London":
                return 2643743;
            case "New York":
                return 5128581;
            case "Oman":
                return 287286;
            case "Mauritius":
                return 934154;
            case "Bangladesh":
                return 1185241;
            default:
                return 0;
        }
    }
}
