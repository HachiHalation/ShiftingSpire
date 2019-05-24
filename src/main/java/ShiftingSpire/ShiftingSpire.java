package ShiftingSpire;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;


// TODO: INVENTORY TOP BAR, ARMORY SCREEN, MULTIPLE PATCHES
public class ShiftingSpire implements
        PostInitializeSubscriber,
        StartGameSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        StartActSubscriber,
        PostUpdateSubscriber,
        RenderSubscriber {

    public static final String MOD_NAME = "Shifting Spire";
    public static final String AUTHOR = "HachiHalation";
    public static final String DESCRIPTION = "Lets make Slay The Spire into a Spire(dungeon) Crawler!";

    public static final Logger logger = LogManager.getLogger(ShiftingSpire.class.getName());

    protected static Random stat_random;

    private static BuffHelper bhelper;
    public static InventoryScreen inventoryScreen;
    public static ArmoryRewardScreen armoryRewardScreen;
    protected static Inventory inventory;

    public static PlayerID player;


    public ShiftingSpire() {
        BaseMod.subscribe(this);
        stat_random = new Random();

        bhelper = new BuffHelper();
    }

    public static  void initialize() {
        ShiftingSpire mod = new ShiftingSpire();
    }

    public static boolean closeModScreens(){
        if(inventoryScreen.open) {
            inventoryScreen.close();
            return true;
        }
        return false;
    }

    @java.lang.Override
    public void receiveEditRelics() {
        logger.info("Generating equipment info...");
        EquipmentHelper.initializeEquipment();
        inventory = new Inventory();

        BaseMod.addRelic(EquipmentHelper.generate(EquipmentID.LONGBLADE, 0), RelicType.RED);
        inventory.saveInventory();
    }

    @java.lang.Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(RelicStrings.class, "ShiftingSpireAssets/localization/Equipment-RelicStrings.json");
    }

    @java.lang.Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("ShiftingSpireAssets/relics/ggg.png");
        ModPanel settingPanel = new ModPanel();
        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, settingPanel);

        inventoryScreen = new InventoryScreen(inventory);
        BaseMod.addTopPanelItem(new InventoryTopBar(inventoryScreen));

        armoryRewardScreen = new ArmoryRewardScreen();

    }

    @java.lang.Override
    public void receivePostUpdate() {
        if(inventoryScreen.open) inventoryScreen.update();
    }

    @java.lang.Override
    public void receiveRender(SpriteBatch spriteBatch) {
        if(inventoryScreen.open) inventoryScreen.render(spriteBatch);
    }

    @java.lang.Override
    public void receiveStartAct() {
        inventory.saveInventory();
    }

    @java.lang.Override
    public void receiveStartGame() {
        player = detectPlayer();
        inventory.reequip(player);
    }

    private PlayerID detectPlayer() {
        if(AbstractDungeon.player instanceof Ironclad)
            return PlayerID.IRONCLAD;
        if(AbstractDungeon.player instanceof TheSilent)
            return PlayerID.SILENT;
        if(AbstractDungeon.player instanceof Defect)
            return PlayerID.DEFECT;
        return PlayerID.CUSTOM;

    }
}
