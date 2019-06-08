package ShiftingSpire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.AsyncSaver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Inventory {

    private HashSet<Equipment> inventory;

    private HashMap<PlayerID, Equipment> equipped;

    Inventory(){
        inventory = new HashSet<>();
        equipped = new HashMap<>();
        loadInventory();
    }



    void saveInventory() {
        ShiftingSpire.logger.info("Saving inventory...");
        HashMap<String, ArrayList<InvenData>> map = new HashMap<>();

        ArrayList<InvenData> items = new ArrayList<>();
        for(Equipment e: inventory) {
            items.add(e.save());
        }

        map.put("inventory", items);
        items = new ArrayList<>();
        items.add(equipped.get(PlayerID.IRONCLAD).save());
        map.put("ironclad", items);
        items = new ArrayList<>();
        items.add(equipped.get(PlayerID.SILENT).save());
        map.put("silent", items);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = gson.toJson(map);
        String filepath = "shiftingspire/saves/inventory.data";
        AsyncSaver.save(filepath, str);
    }

    void loadInventory() {
        Gson gson = new Gson();

        ShiftingSpire.logger.info("Loading inventory file...");
        FileHandle file = Gdx.files.local("shiftingspire/saves/inventory.data");
        if(!file.exists()) {
            File dir = new File("shiftingspire/saves");
            dir.mkdirs();
            File save = new File("shiftingspire/saves/inventory.data");
            try {
                save.createNewFile();
            } catch (IOException e) {
                ShiftingSpire.logger.info("Failed to create data file");
                e.printStackTrace();
            }
        }
        else {
            try {
                String str = file.readString();
                Type mapType = new TypeToken<HashMap<String, ArrayList<InvenData>>>() {}.getType();
                HashMap<String, ArrayList<InvenData>> map = gson.fromJson(str, mapType);
                ShiftingSpire.logger.info("MAP: " + map.toString());
                ArrayList<InvenData> items = map.get("inventory");
                for(InvenData i : items) {
                    addToInventory(EquipmentHelper.createFromData(i));
                }
                equipped.put(PlayerID.IRONCLAD, EquipmentHelper.createFromData(map.get("ironclad").get(0)));
                equipped.put(PlayerID.SILENT, EquipmentHelper.createFromData(map.get("silent").get(0)));
                ShiftingSpire.logger.info("INVENTORY: " + inventory.toString());

            } catch (GdxRuntimeException e) {
                ShiftingSpire.logger.info("Bad/no inventory data file");
            } catch (JsonSyntaxException j) {
                ShiftingSpire.logger.info("Bad inventory file");
                file.delete();
            }
        }

        if(!equipped.containsKey(PlayerID.IRONCLAD))
            equipped.put(PlayerID.IRONCLAD, EquipmentHelper.generate(EquipmentID.LONGBLADE, 0));
        if(!equipped.containsKey(PlayerID.SILENT))
            equipped.put(PlayerID.SILENT, EquipmentHelper.generate(EquipmentID.INFECTEDDAGGER, 0)); //TODO: Other classes

    }

    void addToInventory(Equipment e) {
        inventory.add(e);
        e.isObtained = true;
    }

    void equip(Equipment e, PlayerID player) {
        Equipment toremove = equipped.get(player);
        unequip(toremove);
        equipped.put(player, (Equipment) e.makeCopy());
        e.instantObtain();
    }

    public void reequip(PlayerID player){
        Equipment e = equipped.get(player);
        unequip(e);
        equip(e, player);

    }

    private void unequip(Equipment e) {
        Equipment eq = (Equipment) AbstractDungeon.player.getRelic(e.relicId);

        if(eq == e) {
            ShiftingSpire.logger.info("Removing " + e.relicId + "\n");
            boolean result = AbstractDungeon.player.loseRelic(e.relicId);
            if(result)
                ShiftingSpire.logger.info("Removed succesfully");
            else
                ShiftingSpire.logger.info("FAILED!");
        }
        //TODO: other things after unequip
    }

    public Equipment getEquip(PlayerID player) {
        return equipped.get(player);
    }

    public void render(SpriteBatch sb) {
        for(Equipment e: inventory)
            e.render(sb);
    }

    void drawInventory(float x, float y, float padx, float pady, int perline) {
        int linenum = 0;
        int i = 0;
        for(Equipment e: inventory) {
            int mod = i % perline;

            if(mod == 0 && i != 0)
                ++linenum;

            e.targetX = x + mod*padx;
            e.targetY = y - linenum*pady;
            ++i;
        }
    }

    Equipment updatePositions(float x, float y, float padx, float pady, int perline) {
        Equipment hovered = null;
        drawInventory(x, y, padx, pady, perline);
        for(Equipment e: inventory) {
            e.update();
            if(e.hb.hovered) hovered = e;
        }
        return hovered;
    }

}

class InvenData {
    int level;
    EquipmentID eid;
    int[] attributes;
    public InvenData(EquipmentID eid, int level, int[] attributes) {
        this.level = level;
        this.eid = eid;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "InvenData{" +
                "level=" + level +
                ", eid=" + eid +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }
}
