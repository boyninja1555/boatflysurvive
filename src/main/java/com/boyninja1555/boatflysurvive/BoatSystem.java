package com.boyninja1555.boatflysurvive;

import org.bukkit.Server;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoatSystem {
    private static final Map<UUID, UUID> flyers = new HashMap<>();

    public static void attemptFly(Player player, ChestBoat boat) {
        Inventory inventory = boat.getInventory();
        if (inventory.isEmpty()) return;
        if (!inventory.contains(_Globals.FUEL)) return;
        flyers.put(player.getUniqueId(), boat.getUniqueId());
        boat.setGravity(false);
    }

    public static void cutEngine(Player player, ChestBoat boat) {
        boat.setGravity(true);
        flyers.remove(player.getUniqueId());
    }

    public static void tickControls() {
        Server server = BoatflySurvive.server;
        flyers.forEach((playerId, boatId) -> {
            Player player = server.getPlayer(playerId);
            if (player == null) return;
            if (!(server.getEntity(boatId) instanceof ChestBoat boat)) return;
            float pitch = player.getPitch();
            double targetY = -pitch / 90 * .12;
            Vector velocity = boat.getVelocity();
            velocity.setY(targetY * .7 + targetY * .3);
            boat.setVelocity(velocity);
        });
    }

    public static void tickAmbience() {
        Server server = BoatflySurvive.server;
        flyers.forEach((playerId, boatId) -> {
            if (!(server.getEntity(boatId) instanceof ChestBoat boat)) return;
            boat.getWorld().playSound(boat.getLocation(), _Globals.ENGINE_SOUND, SoundCategory.AMBIENT, 1f, 1f);
        });
    }

    public static void tickFuels() {
        Server server = BoatflySurvive.server;
        flyers.forEach((playerId, boatId) -> {
            Player player = server.getPlayer(playerId);
            if (player == null) return;
            if (!(server.getEntity(boatId) instanceof ChestBoat boat)) return;
            tickFuel(player, boat);
        });
    }

    private static void tickFuel(Player player, ChestBoat boat) {
        Inventory inventory = boat.getInventory();
        if (!inventory.contains(_Globals.FUEL)) {
            cutEngine(player, boat);
            return;
        }

        inventory.removeItem(ItemStack.of(_Globals.FUEL, 1));
    }
}
