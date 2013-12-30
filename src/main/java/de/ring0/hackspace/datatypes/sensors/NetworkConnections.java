package de.ring0.hackspace.datatypes.sensors;

public class NetworkConnections extends Sensor {
    public double value;
    public NetworkMachines machines;

    public class NetworkMachines {
        public String name;
        public String mac;
    }
}
