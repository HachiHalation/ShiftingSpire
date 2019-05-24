package ShiftingSpire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.megacrit.cardcrawl.characters.Ironclad;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.AsyncSaver;

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
        HashMap<String, Object> map = new HashMap<>();
        map.put("inventory", inventory);
        map.put("ironclad", ironcladEquipped);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = gson.toJson(map);
        String filepath = "shiftingspire/saves/inventory.data";
        AsyncSaver.save(filepath, str);
    }

    void loadInventory() {
        Gson gson = new Gson();

        try {
            FileHandle file = Gdx.files.local("shiftingspire/saves/inventory.data");
            String str = file.readString();
            Inventory temp = gson.fromJson(str, Inventory.class);
            inventory = temp.inventory;
            ironcladEquipped = temp.ironcladEquipped;
        } catch (GdxRuntimeException e) {
            ShiftingSpire.logger.info("Bad/no inventory data file");
        }
    }

    void addToInventory(Equipment e) {
        inventory.add(e);
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
            if(mod == 0 && i != 0) ++linenum;
            e.targetX = x + mod*padx;
            e.targetY = y + mod*pady;
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
