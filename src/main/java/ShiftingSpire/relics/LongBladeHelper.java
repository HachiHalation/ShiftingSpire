package ShiftingSpire.relics;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

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
}
