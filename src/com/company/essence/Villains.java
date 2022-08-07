package com.company.essence;

/**
 * Класс злодея
 */
public class Villains {
    private String name;
    private EvilnessFactor evilnessFactor;
    private int countMinions;

    public Villains(String name, String evilnessFactor, int countMinions) {
        this.name = name;
        switch (evilnessFactor) {
            case "super good" -> this.evilnessFactor = EvilnessFactor.SUPER_GOOD;
            case "good" -> this.evilnessFactor = EvilnessFactor.GOOD;
            case "bad" -> this.evilnessFactor = EvilnessFactor.BAD;
            case "evil" -> this.evilnessFactor = EvilnessFactor.EVIL;
            case "super evil" -> this.evilnessFactor = EvilnessFactor.SUPER_EVIL;
        }
        this.countMinions = countMinions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EvilnessFactor getEvilnessFactor() {
        return evilnessFactor;
    }

    public void setEvilnessFactor(EvilnessFactor evilnessFactor) {
        this.evilnessFactor = evilnessFactor;
    }

    public int getCountMinions() {
        return countMinions;
    }

    public void setCountMinions(int countMinions) {
        this.countMinions = countMinions;
    }
}
