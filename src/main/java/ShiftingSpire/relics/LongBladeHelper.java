package ShiftingSpire.relics;

import ShiftingSpire.ShiftingSpire;
import ShiftingSpire.Equipment;
import com.badlogic.gdx.graphics.Texture;
import ShiftingSpire.EquipmentID;

import java.util.HashMap;

import static ShiftingSpire.EquipmentHelper.allocatePoints;

/*
WHEN MAKING A NEW EQUIPMENT DO THE FOLLOWING:
    RELIC STRINGS
    TEXTURES
    COPY THIS FILE AND ADJUST
    ADD INIT TO EQUIPMENTHELPER
    ADD USING BASEMOD IN SHIFTINGSPIRE @ RECEIVEEDITRELICS
*/

public class LongBladeHelper {
    public static Texture getTexture(int level){
        if(level < 5)
            return new Texture("ShiftingSpireAssets/relics/longbladebasic.png");
        if(level < 10)
            return new Texture("ShiftingSpireAssets/relics/longblade5.png");
        
        return new Texture("ShiftingSpireAssets/relics/longblade10.png");
    }

    public static String[] initializeCategories() {
        return new String[] {
                "Strength",
                "Strength Modifier",
                "Vuln Modifier"
        };
    }

    public static HashMap<String, Integer> initializeCosts() {
        HashMap<String, Integer> costs = new HashMap<>();

        costs.put("Strength", 3);
        costs.put("Strength Modifier", 2);
        costs.put("Vuln Modifier", 1);

        return costs;
    }

    public static void initialize(HashMap<EquipmentID, String[]> categs, HashMap<EquipmentID, HashMap<String, Integer>> costs, HashMap<EquipmentID, Equipment> base) {
        ShiftingSpire.logger.info("Add LongBlade");
        categs.put(EquipmentID.LONGBLADE, LongBladeHelper.initializeCategories());
        costs.put(EquipmentID.LONGBLADE, LongBladeHelper.initializeCosts());
        base.put(EquipmentID.LONGBLADE, new LongBlade(0, allocatePoints(0,
                costs.get(EquipmentID.LONGBLADE), categs.get(EquipmentID.LONGBLADE))));
    }
}
