package ShiftingSpire;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@SpireInitializer
public class ShiftingSpire implements
        PostInitializeSubscriber,
        StartGameSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        StartActSubscriber,
        PostUpdateSubscriber,
        RenderSubscriber {

    private static final String MOD_NAME = "Shifting Spire";
    private static final String AUTHOR = "HachiHalation";
    private static final String DESCRIPTION = "Lets make Slay The Spire into a Spire(dungeon) Crawler!";

    public static final Logger logger = LogManager.getLogger(ShiftingSpire.class.getName());

    static Random stat_random;

    private static BuffHelper bhelper;
    private static InventoryScreen inventoryScreen;
    private static ArmoryRewardScreen armoryRewardScreen;
    static Inventory inventory;

    public static PlayerID player;


    public ShiftingSpire() {
        BaseMod.subscribe(this);
        stat_random = new Random();

        bhelper = new BuffHelper();
    }

    public static void initialize() {
        logger.info("Starting ShiftingSpire...");
        ShiftingSpire mod = new ShiftingSpire();
    }

    public static boolean closeModScreens(){
        if(inventoryScreen.open) {
            inventoryScreen.close();
            return true;
        }
        return false;
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Generating equipment info...");
        EquipmentHelper.initializeEquipment();
        inventory = new Inventory();

        BaseMod.addRelic(EquipmentHelper.generate(EquipmentID.LONGBLADE, 0), RelicType.RED);
        BaseMod.addRelic(EquipmentHelper.generate(EquipmentID.INFECTEDDAGGER, 0), RelicType.GREEN);
        inventory.saveInventory();
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(RelicStrings.class, "ShiftingSpireAssets/localization/Equipment-RelicStrings.json");
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = new Texture("ShiftingSpireAssets/relics/ggg.png");
        ModPanel settingPanel = new ModPanel();
        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, settingPanel);

        inventoryScreen = new InventoryScreen(inventory);
        BaseMod.addTopPanelItem(new InventoryTopBar(inventoryScreen));

        armoryRewardScreen = new ArmoryRewardScreen();

    }

    @Override
    public void receivePostUpdate() {
        if(inventoryScreen.open) inventoryScreen.update();
    }

    @Override
    public void receiveRender(SpriteBatch spriteBatch) {
        if(inventoryScreen.open) inventoryScreen.render(spriteBatch);
    }

    @Override
    public void receiveStartAct() {
        inventory.saveInventory();
    }

    @Override
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
