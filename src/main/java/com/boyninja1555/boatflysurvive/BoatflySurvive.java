package com.boyninja1555.boatflysurvive;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class BoatflySurvive extends JavaPlugin {
    public static BoatflySurvive use;
    public static Server server;
    public static Logger logger;

    @Override
    public void onEnable() {
        use = this;
        server = getServer();
        logger = getLogger();
        getServer().getPluginManager().registerEvents(new BoatToggler(), this);
        getServer().getScheduler().runTaskTimer(this, BoatSystem::tickControls, 0L, _Globals.TICKS_BETWEEN_CONTROLS);
        getServer().getScheduler().runTaskTimer(this, BoatSystem::tickAmbience, 0L, _Globals.TICKS_BETWEEN_AMBIENCE);
        getServer().getScheduler().runTaskTimer(this, BoatSystem::tickFuels, 0L, _Globals.TICKS_BETWEEN_CONSUMPTION);
    }
}
