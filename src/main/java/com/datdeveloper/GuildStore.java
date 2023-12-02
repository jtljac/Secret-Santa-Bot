package com.datdeveloper;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing the data for each guild
 */
public class GuildStore {
    /** A list of the users partaking in the secret santa */
    public List<String> partakers = new ArrayList<>();

    public void setPartakers(List<String> partakers) {
        this.partakers = partakers;
    }
}
