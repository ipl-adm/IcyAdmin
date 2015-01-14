package com.multi.icyadmin.handlers;

import com.multi.icyadmin.Core;
import com.multi.icyadmin.data.NodeActionsEnum;
import com.multi.icyadmin.utils.FileProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

/**
 * Created by MultiMote on 08.01.2015.
 */
public class ActionHandler {

    public boolean clientCheck(String targetName, EntityPlayer player, EntityPlayer target, NodeActionsEnum action) {
        return !(action.isRequiresTarget() && checkTarget(targetName, target, player, false));
    }

    public void serverWork(String targetName, EntityPlayer player, EntityPlayer target, NodeActionsEnum action) {
        if (!Core.proxy.canPlayerUsePanel(player)) {
            printNoPerms(player);
            FileProcessor.appendToAdminLog(player.getCommandSenderName() + " tries to use command, but no perms.");
            return;
        }

        PlayerProps props = PlayerProps.get(player);

        if (action.getProp() != null) {
            props.toggleProp(action.getProp());
        }

        if (action.isRequiresTarget() && checkTarget(targetName, target, player, true)) return;

        if (action == NodeActionsEnum.THROW_UP) {
            target.attackEntityFrom(DamageSource.cactus, 100);
        }

        //FileProcessor.appendToAdminLog(player.getCommandSenderName() + " toggled " + action);


    }


    public boolean checkTarget(String name, EntityPlayer checkfor, EntityPlayer sender, boolean server) {
        if (name == null) {
            printChatError(sender, "Select target player!", server);
            return true;
        }
        if (name.trim().equals("")) {
            printChatError(sender, "Select target player!", server);
            return true;
        }
        if (checkfor == null && server) {
            printChatError(sender, "Player must be online!", server);
            return true;
        }
        return false;
    }

    public void printChatError(EntityPlayer player, String msg, boolean server) {
        player.addChatMessage(new ChatComponentText("[IcyErr" + (server ? "Server" : "Client") + "] " + msg).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }

    public void printNoPerms(EntityPlayer player) {
        player.addChatMessage(new ChatComponentTranslation("icyadmin.permissions").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
    }
}
