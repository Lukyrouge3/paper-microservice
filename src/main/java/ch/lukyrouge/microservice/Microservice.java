package ch.lukyrouge.microservice;

import ch.lukyrouge.microservice.api.ServiceEndpoint;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

class Test {
    String hello;
}

public final class Microservice extends JavaPlugin {

    public static Microservice MS;

    @Override
    public void onEnable() {
        // Plugin startup logic
        MS = this;
        ServiceEndpoint se = new ServiceEndpoint("", "http://localhost:3000");
        try {
            Test t = se.get("", Test.class);
            this.getLogger().log(Level.INFO, "Test: " + t.hello);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
