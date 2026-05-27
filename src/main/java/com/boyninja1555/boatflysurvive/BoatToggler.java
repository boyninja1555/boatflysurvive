package com.boyninja1555.boatflysurvive;

import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;

public class BoatToggler implements Listener {

    @EventHandler
    public void onBoatEnter(VehicleEnterEvent event) {
        if (!(event.getVehicle() instanceof ChestBoat boat)) return;
        if (!(event.getEntered() instanceof Player player)) return;
        BoatSystem.attemptFly(player, boat);
    }

    @EventHandler
    public void onBoatExit(VehicleExitEvent event) {
        if (!(event.getVehicle() instanceof ChestBoat boat)) return;
        if (!(event.getExited() instanceof Player player)) return;
        BoatSystem.cutEngine(player, boat);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!(player.getVehicle() instanceof ChestBoat boat)) return;
        BoatSystem.attemptFly(player, boat);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!(player.getVehicle() instanceof ChestBoat boat)) return;
        BoatSystem.cutEngine(player, boat);
    }

    @EventHandler
    public void onFuelChanged(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(player.getVehicle() instanceof ChestBoat boat)) return;
        if (!boat.getInventory().equals(inventory)) return;
        if (inventory.contains(_Globals.FUEL)) BoatSystem.attemptFly(player, boat);
        else BoatSystem.cutEngine(player, boat);
    }
}
