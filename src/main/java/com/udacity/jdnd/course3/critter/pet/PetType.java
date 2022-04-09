package com.udacity.jdnd.course3.critter.pet;

/**
 * A example list of pet type metadata that could be included on a request to create a pet.
 */
public enum PetType {
    CAT("CAT"),
    DOG("DOG"),
    LIZARD("LIZARD"),
    BIRD("BIRD"),
    FISH("FISH"),
    SNAKE("SNAKE"),
    OTHER("OTHER");

    private String enumString;

    PetType(String enumString) {
        this.enumString = enumString;
    }

    public String getEnumString() {
        return enumString;
    }

    public static PetType fromEnumCode(String enumString) {
        for (PetType type: PetType.values()) {
            if (type.getEnumString().equals(enumString)) {
                return type;
            }
        }
        return PetType.OTHER;
    }
}
