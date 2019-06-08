package ShiftingSpire.relics;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class InfectedDaggerHelper {
    public static Texture getTexture(int level){
        if(level < 5)
            return new Texture("ShiftingSpireAssets/relics/infecteddaggerbasic.png");
        if(level < 10)
            return new Texture("ShiftingSpireAssets/relics/infecteddagger5.png");

        return new Texture("ShiftingSpireAssets/relics/infecteddagger10.png");
    }

    public static String[] initializeCategories() {
        return new String[] {
                "Envenom",
                "Noxious Fumes",
                "Poison Modifier"
        };
    }

    public static HashMap<String, Integer> initializeCosts() {
        HashMap<String, Integer> costs = new HashMap<>();

        costs.put("Envenom", 2);
        costs.put("Noxious Fumes", 2);
        costs.put("Poison Modifier", 1);

        return costs;
    }

    public static String getStrikeID(){
        switch(ShiftingSpire.ShiftingSpire.player) {
            case IRONCLAD: return "Strike_R";
            case SILENT: return "Strike_G";
            case DEFECT: return "Strike_B";
            case CUSTOM: return null;
        }
        return null;
    }

}
