package com.nhl.nhl_spring_app.model;

public class NhlTeam {
    private String abbreviation;
    private String name;
    private String logoUrl;

    public NhlTeam(String abbreviation, String name) {
        this.abbreviation = abbreviation;
        this.name = name;
        this.logoUrl = "https://assets.nhle.com/logos/nhl/svg/" + abbreviation + "_light.svg";
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
