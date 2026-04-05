package safro.archon.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.item.ExperiencePouchItem;
import safro.archon.util.XpUtil;

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

        if (stack.getItem() instanceof ExperiencePouchItem) {
            if (add) {
                addToPouch(amount, player, stack);
            } else {
                removeFromPouch(amount, player, stack);
            }
        }
    }

    private static void addToPouch(int amount, ServerPlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof ExperiencePouchItem pouch) {
            int playerTotalXp = XpUtil.getPlayerXP(player);
            if (playerTotalXp <= 0) {
                return;
            }

            int currentPouchXp = ExperiencePouchItem.getExperience(stack);
            int maxPouchXp = pouch.getMaxXp();
            int spaceLeftInPouch = maxPouchXp - currentPouchXp;

            if (spaceLeftInPouch <= 0) {
                return;
            }

            int amountToAdd = Math.min(amount, spaceLeftInPouch);
            amountToAdd = Math.min(amountToAdd, playerTotalXp);

            if (amountToAdd > 0) {
                ExperiencePouchItem.addExperience(stack, amountToAdd);
                XpUtil.addPlayerXP(player, -amountToAdd);
            }
        }
    }

    private static void removeFromPouch(int amount, ServerPlayerEntity player, ItemStack stack) {
        int currentPouchXp = ExperiencePouchItem.getExperience(stack);

        if (currentPouchXp <= 0) {
            return;
        }

        int amountToRemove = Math.min(amount, currentPouchXp);

        if (amountToRemove > 0) {
            ExperiencePouchItem.addExperience(stack, -amountToRemove);
            XpUtil.addPlayerXP(player, amountToRemove);
        }
    }
}