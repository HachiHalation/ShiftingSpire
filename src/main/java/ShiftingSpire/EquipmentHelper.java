package ShiftingSpire;


import ShiftingSpire.relics.LongBlade;
import ShiftingSpire.relics.LongBladeHelper;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipmentHelper {
    private static HashMap<EquipmentID, Equipment> base;
    private static HashMap<EquipmentID, String[]> categs;
    private static HashMap<EquipmentID, HashMap<String, Integer>> costs;
    private static HashMap<EquipmentID, Texture> textures;

    public static void initializeEquipment() {
        base = new HashMap<EquipmentID, Equipment>();
        categs = new HashMap<EquipmentID, String[]>();
        costs = new HashMap<EquipmentID, HashMap<String, Integer>>();
        textures = new HashMap<EquipmentID, Texture>();

        ShiftingSpire.logger.info("Add LongBlade");
        categs.put(EquipmentID.LONGBLADE, LongBladeHelper.initializeCategories());
        costs.put(EquipmentID.LONGBLADE, LongBladeHelper.initializeCosts());
        base.put(EquipmentID.LONGBLADE, new LongBlade(0, allocatePoints(0,
                costs.get(EquipmentID.LONGBLADE), categs.get(EquipmentID.LONGBLADE))));
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

    public static Equipment createFromAttr(EquipmentID eid, int level, int[] attr) {
        return base.get(eid).makeType(level, attr);
    }

}

enum PlayerID{
    IRONCLAD,
    SILENT,
    DEFECT,
    CUSTOM
}