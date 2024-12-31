package net.justonedev.mc.biggerCreakings;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public final class BiggerCreakings extends JavaPlugin implements Listener {

    public static double DEFAULT_SCALE_MIN = 1.1;
    public static double DEFAULT_SCALE_MAX = 1.6;
    public static double DEFAULT_DAMAGE_MULTIPLIER = 2.2;

    public double scaleMin = DEFAULT_SCALE_MIN;
    public double scaleMax = DEFAULT_SCALE_MAX;
    public double damageMultiplier = DEFAULT_DAMAGE_MULTIPLIER;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config.load(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        setCommandExecutor("reloadbiggercreakings", this);
    }

    private void setCommandExecutor(String command, CommandExecutor executor) {
        var cmd = getCommand(command);
        if (cmd == null) return;
        cmd.setExecutor(executor);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getEntity().getType() != EntityType.CREAKING) return;

        Attributable attributable = e.getEntity();

        AttributeInstance scaleAttribute = attributable.getAttribute(Attribute.SCALE);
        if (scaleAttribute != null) {
            scaleAttribute.setBaseValue(getRandom(scaleMin, scaleMax));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() != EntityType.CREAKING) return;
        e.setDamage(e.getDamage() * damageMultiplier);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            String msg = Bukkit.spigot().getConfig().getString("messages.unknown-command");
            if (msg == null) msg = "§4You are not allowed to use this command!";
            sender.sendMessage(msg);
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage("§eArguments will be ignored");
        }
        Config.load(this);
        DecimalFormat format = new DecimalFormat("0.#");
        sender.sendMessage("§aConfiguration reloaded. Updated Creaking Size Multiplier: §2[%s, %s]§a | Damage Multiplier: §c%sx".formatted(format.format(scaleMin), format.format(scaleMax), format.format(damageMultiplier)));
        return true;
    }

    private static double getRandom(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
