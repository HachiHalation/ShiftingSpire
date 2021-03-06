package ShiftingSpire;


import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.Arrays;
import java.util.Objects;

public abstract class Equipment extends CustomRelic {
    protected int[] attributes;
    int[] weights;
    EquipmentID equipid;
    protected int level;

    public Equipment(String id, Texture texture, RelicTier tier, LandingSound sfx,
                     int level, EquipmentID eid, int[] attributes, int[] weights) {
        super(id, texture, tier, sfx);
        this.equipid = eid;
        this.level = level;
        this.attributes = attributes;
        this.weights = weights;
    }

    @Override
    public String getUpdatedDescription() {
        if(level == 0)
            return DESCRIPTIONS[0];
        StringBuilder desc = new StringBuilder();
        desc.append(DESCRIPTIONS[1]).append(level).append(DESCRIPTIONS[2]);

        int des = 3;
        for(int i = 0; i < attributes.length; ++i) {
            if(attributes[i] != 0)
                desc.append(DESCRIPTIONS[des++]).append(attributes[i] * weights[i]).append(DESCRIPTIONS[des++]);
            else des+=2;
        }

        if(level >= 5)
            desc.append(DESCRIPTIONS[des++]);
        if(level >= 10)
            desc.append(DESCRIPTIONS[des++]);
        if(level >= 15)
            desc.append(DESCRIPTIONS[des++]);
        if(level >= 20)
            desc.append(DESCRIPTIONS[des++]);

        return desc.toString();
    }

    @Override
    public void obtain() {
        toInventory();
    }

    public void toInventory() {
        ShiftingSpire.inventory.addToInventory(this);
        this.relicTip();
        UnlockTracker.markRelicAsSeen(this.relicId);
    }

    public InvenData save() {
        return new InvenData(equipid, level, attributes);
    }

    public abstract Equipment makeType(int level, int[] attributes);


    @Override
    public String toString() {
        return  equipid + "{" +
                "attributes=" + Arrays.toString(attributes) +
                ", weights=" + Arrays.toString(weights) +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return level == equipment.level &&
                Arrays.equals(attributes, equipment.attributes) &&
                equipid == equipment.equipid;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(equipid, level);
        result = 31 * result + Arrays.hashCode(attributes);
        result = 31 * result + Arrays.hashCode(weights);
        return result;
    }
}
