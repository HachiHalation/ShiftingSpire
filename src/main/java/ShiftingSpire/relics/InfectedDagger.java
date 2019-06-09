package ShiftingSpire.relics;

import ShiftingSpire.Equipment;
import ShiftingSpire.EquipmentID;
import ShiftingSpire.powers.DemonicStrength;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.green.Catalyst;
import com.megacrit.cardcrawl.cards.green.CorpseExplosion;
import com.megacrit.cardcrawl.cards.green.DeadlyPoison;
import com.megacrit.cardcrawl.cards.red.Flex;
import com.megacrit.cardcrawl.cards.red.LimitBreak;
import com.megacrit.cardcrawl.cards.red.Pummel;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.NoxiousFumesPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class InfectedDagger extends ShiftingSpire.Equipment{
    private static final String ID = "shiftingspire:InfectedDagger";
    private static final int[] weights = {1, 1, 5};
    private static final int ENV_INDEX = 0;
    private static final int NOX_INDEX = 1;
    private static final int POISON_INDEX = 2;

    private int envenom;
    private int noxious;
    private int poison_buff;

    public InfectedDagger(int level, int[] attributes) {
        super(ID, InfectedDaggerHelper.getTexture(level), RelicTier.STARTER, LandingSound.SOLID, level, EquipmentID.INFECTEDDAGGER, attributes, weights);

        envenom = attributes[ENV_INDEX];
        noxious = attributes[NOX_INDEX];
        poison_buff = attributes[POISON_INDEX];

        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        if(envenom > 0)
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnvenomPower(AbstractDungeon.player, envenom), envenom));
        if(noxious > 0)
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NoxiousFumesPower(AbstractDungeon.player, noxious), noxious));

        if(level >= 5){
            CardGroup deck = AbstractDungeon.player.drawPile;
            String id = InfectedDaggerHelper.getStrikeID();
            if(id != null) {
                for (int i = 0; i < 3; ++i) {
                    AbstractCard strike = deck.findCardById(id);
                    AbstractCard poison = new DeadlyPoison();
                    if (strike.upgraded) poison.upgrade();
                    AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(strike, deck, true));
                    AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(poison, 1, true, true));
                }
            }
        }
        if(level >= 10)
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(new Catalyst(), 1, true, true));
        if(level >= 15){
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(new CorpseExplosion(), 1, true, true)); }
        if(level >= 20);
            // TODO: PURE POISON AND DECK TO POISON


    }

    public void applyPoisonBuff(int amount, AbstractCreature target){
        int add = (new Double(Math.floor(amount * (1 + (poison_buff*weights[POISON_INDEX])/100D)))).intValue();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, add), add, true, AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new InfectedDagger(this.level, this.attributes);
    }

    @Override
    public Equipment makeType(int level, int[] attributes) {
        return new InfectedDagger(level, attributes);
    }
}
