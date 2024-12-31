package net.justonedev.mc.biggerCreakings;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final String KEY_SCALE_MIN = "%s.size_multipliers.min";
    private static final String KEY_SCALE_MAX = "%s.size_multipliers.max";
    private static final String KEY_DAMAGE_MULTIPLIER = "%s.damage_multiplier";

    public static void load(BiggerCreakings plugin) {
        File f = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        if (!f.exists()) {
            // Default Values are already set
            cfg.set(KEY_SCALE_MIN.formatted("creaking"), BiggerCreakings.DEFAULT_SCALE_MIN);
            cfg.set(KEY_SCALE_MAX.formatted("creaking"), BiggerCreakings.DEFAULT_SCALE_MAX);
            cfg.set(KEY_DAMAGE_MULTIPLIER.formatted("creaking"), BiggerCreakings.DEFAULT_DAMAGE_MULTIPLIER);
            saveCfg(f, cfg);
            return;
        }

        Map<String, Object> updateThese = new HashMap<>();

        plugin.scaleMin = getOrDefaultDouble(KEY_SCALE_MIN.formatted("creaking"), cfg, updateThese, BiggerCreakings.DEFAULT_SCALE_MIN);
        plugin.scaleMax = getOrDefaultDouble(KEY_SCALE_MAX.formatted("creaking"), cfg, updateThese, BiggerCreakings.DEFAULT_SCALE_MAX);
        plugin.damageMultiplier = getOrDefaultDouble(KEY_DAMAGE_MULTIPLIER.formatted("creaking"), cfg, updateThese, BiggerCreakings.DEFAULT_DAMAGE_MULTIPLIER);

        if (updateThese.isEmpty()) return;
        cfg = YamlConfiguration.loadConfiguration(f);	// Reload config
        for (Map.Entry<String, Object> entry : updateThese.entrySet()) {
            cfg.set(entry.getKey(), entry.getValue());
        }
        saveCfg(f, cfg);
    }

    private static double getOrDefaultDouble(String key, YamlConfiguration cfg, Map<String, Object> updateThese, double defaultValue)
    {
        if (cfg.isSet(key)) return cfg.getDouble(key);
        updateThese.put(key, defaultValue);
        return defaultValue;
    }

    private static void saveCfg(File f, YamlConfiguration cfg) {
        try {
            cfg.save(f);
        } catch (IOException ignored) {}
    }

}
