package gigaherz.toolbelt;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;

public abstract class BeltFinder
{
    private static NonNullList<BeltFinder> instances = NonNullList.create();

    public static void addFinder(BeltFinder finder)
    {
        instances.add(0, finder);
    }

    public static void setFinderSlotContents(PlayerEntity player, String where, int slot, ItemStack stack)
    {
        for(BeltFinder finder : instances) {
            if (finder.getName().equals(where)) {
                finder.setToSlot(player, slot, stack);
            }
        }
    }

    public abstract String getName();

    public abstract LazyOptional<BeltGetter> findStack(PlayerEntity player);

    public static LazyOptional<BeltGetter> findBelt(PlayerEntity player)
    {
        return instances.stream()
                .map(f -> f.findStack(player))
                .filter(LazyOptional::isPresent)
                .findFirst()
                .orElseGet(LazyOptional::empty);
    }

    public static void sendSync(PlayerEntity player)
    {
        findBelt(player).ifPresent(BeltGetter::syncToClients);
    }

    public void setToSlot(LivingEntity player, int slot, ItemStack stack)
    {
    }

    public interface BeltGetter
    {
        ItemStack getBelt();

        void syncToClients();
    }
}
