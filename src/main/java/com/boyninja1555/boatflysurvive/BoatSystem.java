package com.boyninja1555.boatflysurvive;

import org.bukkit.Server;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class BoatSystem {
    private static final Map<UUID, UUID> flyers = new HashMap<>();
    private static final List<UUID> flyingBoats = new ArrayList<>();

    public static void attemptFly(Player player, ChestBoat boat) {
        Inventory inventory = boat.getInventory();
        if (inventory.isEmpty()) return;
        if (!inventory.contains(_Globals.FUEL)) return;
        flyers.put(player.getUniqueId(), boat.getUniqueId());
        flyingBoats.add(boat.getUniqueId());
        boat.setGravity(false);
        boat.setMaxSpeed(_Globals.BOAT_SPEED);
    }

    public static void cutEngine(Player player, ChestBoat boat) {
        boat.setGravity(true);
        boat.setMaxSpeed(.4);
        flyers.remove(player.getUniqueId());
        flyingBoats.remove(boat.getUniqueId());
    }

    public static boolean isPlayerFlying(Player player) {
        return flyers.containsKey(player.getUniqueId());
    }

    public static boolean isBoatFlying(ChestBoat boat) {
        return flyingBoats.contains(boat.getUniqueId());
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
            velocity.setY(targetY);
            boat.setVelocity(velocity);
        });
    }

    public static void tickAmbience() {
        Server server = BoatflySurvive.server;
        flyers.forEach((playerId, boatId) -> {
            if (!(server.getEntity(boatId) instanceof ChestBoat boat)) return;
            World world = boat.getWorld();
            world.playSound(boat.getLocation(), _Globals.ENGINE_SOUND, SoundCategory.AMBIENT, 1f, 1f);
            world.spawnParticle(_Globals.ENGINE_PARTICLE, boat.getLocation(), 20);
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

        int slot = inventory.first(_Globals.FUEL);
        if (slot == -1) return;

        ItemStack fuel = inventory.getItem(slot);
        if (fuel == null) return;

        ItemStack usedFuel = new ItemStack(_Globals.FUEL_USED, fuel.getAmount());
        inventory.setItem(slot, usedFuel);
    }
}
