package de.ring0.hackspace.datatypes.field;

public class SpaceFeeds {
    public SpaceFeed blog;
    public SpaceFeed wiki;
    public SpaceFeed calendar;
    public SpaceFeed flickr;

    public class SpaceFeed {
        public String type;
        public String url;
    }
}
