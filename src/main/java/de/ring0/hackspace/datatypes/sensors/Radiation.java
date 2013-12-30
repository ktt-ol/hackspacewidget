package de.ring0.hackspace.datatypes.sensors;

public class Radiation {
    public RadiationLevelSensor[] alpha;
    public RadiationLevelSensor[] beta;
    public RadiationLevelSensor[] gamma;
    public RadiationLevelSensor[] beta_gamma;

    public class RadiationLevelSensor extends Sensor {
        public double value;
        public double dead_time;
        public double conversion_factor;
    }
}
