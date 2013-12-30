package de.ring0.hackspace.datatypes.sensors;

public class Wind extends Sensor{
    public WindProperties properties;

    public class WindProperties {
        public WindProperty speed;
        public WindProperty gust;
        public WindProperty direction;
        public WindProperty elevation;
    }
    public class WindProperty {
        public double value;
        public String unit;
    }
}
