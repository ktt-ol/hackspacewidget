package de.ring0.hackspace.datatypes;

import java.util.Map;

import de.ring0.hackspace.datatypes.field.SpaceContact;
import de.ring0.hackspace.datatypes.field.SpaceEvent;
import de.ring0.hackspace.datatypes.field.SpaceFeeds;
import de.ring0.hackspace.datatypes.field.SpaceFED;
import de.ring0.hackspace.datatypes.field.SpaceCache;
import de.ring0.hackspace.datatypes.field.SpaceRadioShow;
import de.ring0.hackspace.datatypes.field.SpaceLocation;
import de.ring0.hackspace.datatypes.field.SpaceSensors;
import de.ring0.hackspace.datatypes.field.SpaceState;

public class Space {
	public int id;
	public String api;
	public String space;
	public String logo;
	public String url;
    public String spaceapi;
	public SpaceLocation location;
    public SpaceFED spacefed;
	public String[] cam;
	public Map<String,String> stream;
	public SpaceState state;
	public SpaceEvent[] events;
    public SpaceContact contact;
    public SpaceSensors sensors;
	public SpaceFeeds feeds;
    public SpaceCache cache;
    public String[] projects;
    public SpaceRadioShow radio_shows;

	
	public Space() {}
}