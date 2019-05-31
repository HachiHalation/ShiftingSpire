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

    Equipment ironcladEquipped;
    //TODO: other classes

    Inventory(){
        inventory = new HashSet<>();
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
        items.add(ironcladEquipped.save());
        map.put("ironclad", items);
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
                ironcladEquipped = EquipmentHelper.createFromData(map.get("ironclad").get(0));
                ShiftingSpire.logger.info("INVENTORY: " + inventory.toString());

            } catch (GdxRuntimeException e) {
                ShiftingSpire.logger.info("Bad/no inventory data file");
            } catch (JsonSyntaxException j) {
                ShiftingSpire.logger.info("Bad inventory file");
                file.delete();
            }
        }

        if(ironcladEquipped == null)
            ironcladEquipped = EquipmentHelper.generate(EquipmentID.LONGBLADE, 0); //TODO: Other classes

    }

    void addToInventory(Equipment e) {
        inventory.add(e);
        e.isObtained = true;
    }

    void equip(Equipment e, PlayerID player) {
        if(player == PlayerID.IRONCLAD) {
            unequip(ironcladEquipped);
            ironcladEquipped = (Equipment) e.makeCopy();
            ironcladEquipped.instantObtain();
        }
    }

    public void reequip(PlayerID player){
        Equipment e = null;
        if(player == PlayerID.IRONCLAD)
            e = ironcladEquipped;
        unequip(e);
        equip(e, player);

    }

    private void unequip(Equipment e) {
        Equipment eq = (Equipment) AbstractDungeon.player.getRelic(e.relicId);
        if(eq == e)
            AbstractDungeon.player.loseRelic(e.relicId);
        //TODO: other things after unequip
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
            ShiftingSpire.logger.info("DEBUG:: \n" +
                    "inven num:" + i + "\n" +
                    "x: " + e.targetX + " padx: "+ padx + " currx: " + e.currentX + "\n" +
                    "y: " + e.targetY + " pady:" + pady + " curry: " + e.currentY + "\n");
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
