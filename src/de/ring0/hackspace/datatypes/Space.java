package de.ring0.hackspace.datatypes;

import java.util.HashMap;

public class Space {
	public String api;
	public String statusapi;
	public String name;
	public String space;
	public String logo;
	public SpaceIcons icon;
	public String url;
	public String address;
	public SpaceContact contact;
	public float lat;
	public float lon;
	public String[] cam;
	public HashMap<String,String> stream;
	public boolean open;
	public String status;
	public long lastchange;
	public SpaceEvent[] events;
	public SpaceSensors sensors;
	public SpaceFeed[] feeds;
	
	public Space() {}
}