package ShiftingSpire.relics;

import ShiftingSpire.Equipment;
import ShiftingSpire.EquipmentID;
import ShiftingSpire.powers.DemonicStrength;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.red.Flex;
import com.megacrit.cardcrawl.cards.red.LimitBreak;
import com.megacrit.cardcrawl.cards.red.Pummel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class LongBlade extends ShiftingSpire.Equipment {
    private static final String ID = "shiftingspire:LongBlade";
    private static final int[] weights = {1, 1, 5};
    private static final int STRENGTH_IDX = 0;
    private static final int STRENGTHMOD_IDX = 1;
    private static final int VULN_BUFF_IDX = 2;

    private int str;
    private int strmod;
    private int vulnbuff;

    private boolean bufflock = false;
    private boolean debufflock = false;

    public LongBlade(int level, int[] attr) {
        super(ID, LongBladeHelper.getTexture(), RelicTier.STARTER, LandingSound.SOLID,
                  level, EquipmentID.LONGBLADE, attr, weights);

        str = attr[STRENGTH_IDX];
        strmod = attr[STRENGTHMOD_IDX];
        vulnbuff = attr[VULN_BUFF_IDX];

        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    public void applyStrMod(){
        if(bufflock) {
            bufflock = false;
        } else {
            bufflock = true;
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new StrengthPower(AbstractDungeon.player, strmod), strmod));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public void appleStrModDown(){
        if(debufflock){
            debufflock = false;
        } else {
            debufflock = true;
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new LoseStrengthPower(AbstractDungeon.player, strmod), strmod));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public void atBattleStart() {
        bufflock = true;
        debufflock = false;

        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        if ( str > 0) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, str), str));
        }

        if(level >= 5)
            AbstractDungeon.actionManager.addToTop(new AddCardToDeckAction(new Flex()));
        if(level >= 10)
            AbstractDungeon.actionManager.addToTop(new AddCardToDeckAction(new Pummel()));
        if(level >= 15)
            AbstractDungeon.actionManager.addToTop(new AddCardToDeckAction(new LimitBreak()));
        if(level >= 20)
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player,
                    AbstractDungeon.player, new DemonicStrength(AbstractDungeon.player)));

    }

    public int getVulnBuff(){
        return vulnbuff*weights[VULN_BUFF_IDX];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LongBlade(this.level, this.attributes);
    }

    @Override
    public Equipment makeType(int level, int[] attributes) {
        return new LongBlade(level, attributes);
    }

}
