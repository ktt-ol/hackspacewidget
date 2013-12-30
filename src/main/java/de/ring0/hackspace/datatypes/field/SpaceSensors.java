package de.ring0.hackspace.datatypes.field;

import android.os.NetworkOnMainThreadException;

import de.ring0.hackspace.datatypes.sensors.AccountBalance;
import de.ring0.hackspace.datatypes.sensors.Barometer;
import de.ring0.hackspace.datatypes.sensors.BeverageSupply;
import de.ring0.hackspace.datatypes.sensors.DoorLocked;
import de.ring0.hackspace.datatypes.sensors.Humidity;
import de.ring0.hackspace.datatypes.sensors.NetworkConnections;
import de.ring0.hackspace.datatypes.sensors.PeopleNowPresent;
import de.ring0.hackspace.datatypes.sensors.PowerConsumption;
import de.ring0.hackspace.datatypes.sensors.Radiation;
import de.ring0.hackspace.datatypes.sensors.Temperature;
import de.ring0.hackspace.datatypes.sensors.TotalMemberCount;
import de.ring0.hackspace.datatypes.sensors.Wind;

public class SpaceSensors {
	public Temperature[] temperature;
    public DoorLocked[] door_locked;
    public Barometer[] barometer;
    public Radiation[] radiation;
    public Humidity[] humidity;
    public BeverageSupply[] beverage_supply;
    public PowerConsumption power_consumption;
	public Wind[] wind;
    public NetworkConnections[] network_connections;
	public AccountBalance[] account_balance;
    public TotalMemberCount[] total_member_count;
    public PeopleNowPresent[] people_now_present;
}
