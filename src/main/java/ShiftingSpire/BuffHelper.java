package ShiftingSpire;

import ShiftingSpire.relics.LongBlade;
import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BuffHelper implements PostPowerApplySubscriber {

    public BuffHelper(){
        BaseMod.subscribe(this);
    }
    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        if(target != null && target.isPlayer) {
            if(ShiftingSpire.player == PlayerID.IRONCLAD) {
                Equipment ironclad = ShiftingSpire.inventory.ironcladEquipped;
                switch(ShiftingSpire.inventory.ironcladEquipped.equipid) {
                    case LONGBLADE:
                        if(abstractPower.ID.equals("Strength"))
                            ((LongBlade) ironclad).applyStrMod();
                        if(abstractPower.ID.equals("Flex"))
                            ((LongBlade) ironclad).appleStrModDown();
                        break;

                }
            }
        }
    }
}
