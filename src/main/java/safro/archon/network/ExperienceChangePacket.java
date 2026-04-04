package safro.archon.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.item.ExperiencePouchItem;

public class ExperienceChangePacket {
    public static final Identifier ID = new Identifier(Archon.MODID, "experience_change");

    public static void send(int amount, boolean add) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(amount);
        buf.writeBoolean(add);
        ClientPlayNetworking.send(ID, buf);
    }

    public static void receive(ServerPlayerEntity player, PacketByteBuf buf) {
        int amount = buf.readInt();
        boolean add = buf.readBoolean();
        ItemStack stack = player.getMainHandStack();

        if (add) {
            addToPouch(amount, player, stack);
        } else {
            removeFromPouch(amount, player, stack);
        }
    }

    private static void addToPouch(int amount, ServerPlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof ExperiencePouchItem pouch) {
            int currentXp = ExperiencePouchItem.getExperience(stack);
            int maxXp = pouch.getMaxXp();
            int amountToAdd = Math.min(amount, maxXp - currentXp);
            
            int playerXp = ExperiencePouchItem.getTotalXp(player);
            amountToAdd = Math.min(amountToAdd, playerXp);

            if (amountToAdd > 0 && ExperiencePouchItem.canAddXp(player, stack, amountToAdd)) {
                ExperiencePouchItem.addExperience(stack, amountToAdd);
                player.addExperience(-amountToAdd);
            }
        }
    }

    private static void removeFromPouch(int amount, ServerPlayerEntity player, ItemStack stack) {
        if (ExperiencePouchItem.getExperience(stack) >= amount) {
            ExperiencePouchItem.grantExperience(stack, player, amount);
        } else {
            int amountToRemove = ExperiencePouchItem.getExperience(stack);
            if (amountToRemove > 0) {
                ExperiencePouchItem.grantExperience(stack, player, amountToRemove);
            }
        }
    }
}
