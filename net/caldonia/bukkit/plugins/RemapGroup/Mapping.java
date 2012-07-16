package net.caldonia.bukkit.plugins.RemapGroup;

import java.util.regex.Pattern;

public class Mapping {
    private String map;
    private String from;
    private String to;
    private String direct;
    private String announce;

    public Mapping (String map, String from, String to, String direct, String announce) {
        this.map = map;
        this.from = from;
        this.to = to;
        this.direct = direct;
        this.announce = announce;
    }

    public String getTo() {
        return to;
    }

    public String getRawDirect() {
        return direct;
    }

    public String getRawAnnounce() {
        return announce;
    }

    public String getDirect(String playerName) {
        if (direct == null)
            return null;

        return substitute(direct, playerName);
    }

    public String getAnnounce(String playerName) {
        if (announce == null)
            return null;

        return substitute(announce, playerName);
    }

    public String getInfo() {
        return "(" + map + ") " + from + " -> " + to;
    }

    private String substitute(String text, String playerName) {
        String output = text.replaceAll(Pattern.quote("$player"), playerName);
        return output.replaceAll(Pattern.quote("$group"), to);
    }
}
