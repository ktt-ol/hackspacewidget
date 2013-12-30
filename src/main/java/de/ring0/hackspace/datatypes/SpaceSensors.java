package de.ring0.hackspace.datatypes;

import de.ring0.hackspace.datatypes.sensors.Barometer;
import de.ring0.hackspace.datatypes.sensors.HumiditySensor;
import de.ring0.hackspace.datatypes.sensors.TemperatureSensor;
import de.ring0.hackspace.datatypes.sensors.WifiConnection;
import de.ring0.hackspace.datatypes.sensors.WindSensor;

public class SpaceSensors {
	public TemperatureSensor[] temperature;
	public HumiditySensor[] humidity;
	public Barometer[] barometer;
	public WindSensor[] wind;
	public WifiConnection[] wifi;
}
