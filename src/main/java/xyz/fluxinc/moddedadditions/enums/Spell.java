package xyz.fluxinc.moddedadditions.enums;

public enum Spell {

    FIREBALL,
    TELEPORT,
    ARROWS,
    FREEZE,
    HEAL;

    public static Spell getSpell(int modelId) {
        switch (modelId) {
            case 1:  return FIREBALL;
            case 2:  return TELEPORT;
            case 3:  return ARROWS;
            case 4:  return FREEZE;
            case 5:  return HEAL;
            default: return null;
        }
    }

    public static int getModelId(Spell spell) {
        switch (spell) {
            case FIREBALL:  return 1;
            case TELEPORT:  return 2;
            case ARROWS:    return 3;
            case FREEZE:    return 4;
            case HEAL:      return 5;
            default:        return 0;
        }
    }


}