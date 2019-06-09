package ShiftingSpire;


import ShiftingSpire.relics.InfectedDaggerHelper;
import ShiftingSpire.relics.LongBladeHelper;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.HashSet;

public class EquipmentHelper {
    private static HashMap<EquipmentID, String[]> categs;
    private static HashMap<EquipmentID, HashMap<String, Integer>> costs;
    private static HashMap<EquipmentID, Equipment> base;
    private static HashMap<PlayerID, HashSet<EquipmentID>> canequip;
    private static HashMap<EquipmentID, Texture> textures;

    public static void initializeEquipment() {
        base = new HashMap<>();
        categs = new HashMap<>();
        costs = new HashMap<>();
        canequip = new HashMap<>();
        for(PlayerID p : PlayerID.values()) {
            canequip.put(p, new HashSet<>());
        }
        textures = new HashMap<>();

        LongBladeHelper.initialize(categs, costs, base, canequip);
        InfectedDaggerHelper.initialize(categs, costs, base, canequip);

    }

    public static int[] allocatePoints(int level, HashMap<String, Integer> costs, String[] categ) {
        int points = (int) Math.floor(Math.pow(level, 1.5));
        int[] result = new int[categ.length];
        while(points > 0) {
            int idx = ShiftingSpire.stat_random.nextInt(categ.length);
            int cost = costs.get(categ[idx]);
            if(points - cost >= 0) {
                points -= cost;
                result[idx]++;
            }
        }
        return result;
    }

    public static Equipment generate(EquipmentID eid, int level) {
        return base.get(eid).makeType(level, allocatePoints(level, costs.get(eid), categs.get(eid)));
    }

    public static Equipment genRandom(int level) {
        return generate(EquipmentID.values()[ShiftingSpire.stat_random.nextInt(base.size())], level);
    }

    public static Equipment createFromAttr(EquipmentID eid, int level, int[] attr) {
        return base.get(eid).makeType(level, attr);
    }

    public static Equipment createFromData(InvenData data) {
        return createFromAttr(data.eid, data.level, data.attributes);
    }

    public static boolean canBeEquipped(Equipment e) {
        return canequip.get(ShiftingSpire.player).contains(e.equipid);
    }

}

