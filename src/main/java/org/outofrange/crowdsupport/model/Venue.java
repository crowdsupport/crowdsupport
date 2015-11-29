package org.outofrange.crowdsupport.model;

import java.nio.file.Path;

public class Venue extends IdentifiedEntity {
    private City city;
    private String name;
    private Path imagePath;
}
