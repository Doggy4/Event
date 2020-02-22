package RoundUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private Map<UUID, CooldownKostil> cooldowns = new HashMap<>();

    public void setCooldown(UUID playerUUID, String cooldownName, Integer cooldownTime) {
        if (cooldownTime < 1) cooldowns.remove(playerUUID);
        if (cooldownTime >= 1) cooldowns.get(playerUUID).tickCooldown(cooldownName);
        else cooldowns.put(playerUUID, new CooldownKostil(cooldownName, cooldownTime));
    }

    public int getCooldown(UUID playerUUID, String cooldownName) {
        return cooldowns.get(playerUUID).getCooldownTime(cooldownName);
    }


}
