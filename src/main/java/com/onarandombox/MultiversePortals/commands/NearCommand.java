/**
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.
 * Multiverse 2 is licensed under the BSD License.
 * For more information please check the README.md file included
 * with this project
 */
package com.onarandombox.MultiversePortals.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;

/**
 * Displays portals within a radius of a player
 */
public class NearCommand extends PortalCommand {

    public NearCommand(MultiversePortals plugin) {
        super(plugin);
       this.setName("Display nearby portals");
       this.setCommandUsage("/mvp near " + ChatColor.GREEN + "{RADIUS}");
       this.setArgRange(1, 1);
       this.addKey("mvp near");
       this.addKey("mvpnear");
       this.setPermission("multiverse.portal.near", "Allows you to display nearby portals", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be run by players!");
            return;
        }

        int radius;

        try {
            radius = Integer.parseInt(args.get(0));
        } catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.RED + args.get(0) + " is not a number!");
            return;
        }

        int radiusSquared = radius * radius;

        World world = player.getWorld();
        Vector location = player.getLocation().toVector();

        List<MVPortal> nearby = new ArrayList<MVPortal>();
        List<MVPortal> portals = plugin.getPortalManager().getAllPortals();

        for (MVPortal portal : portals) {
            if (world.equals(portal.getWorld())) {
                Vector max = portal.getLocation().getMaximum();
                if (location.distanceSquared(max) <= radiusSquared) {
                    nearby.add(portal);
                }
            }
        }

        if (nearby.isEmpty()) {
            player.sendMessage(ChatColor.DARK_AQUA + "No portals found in a " +
                    ChatColor.AQUA + radius + ChatColor.DARK_AQUA + " radius.");
            return;
        }
        
        player.sendMessage(ChatColor.DARK_AQUA + "----" + ChatColor.AQUA +
                " Nearby Portals " + ChatColor.DARK_AQUA + "----");

        for (MVPortal portal : nearby) {
            player.sendMessage(ChatColor.GREEN + " - " + ChatColor.WHITE +
                    portal.getName() + ChatColor.AQUA + " (" + ChatColor.WHITE +
                    portal.getLocation() + ChatColor.DARK_AQUA + ")");
        }

        nearby.clear();
    }
}
