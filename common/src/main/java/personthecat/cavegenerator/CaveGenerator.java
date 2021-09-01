package personthecat.cavegenerator;

import lombok.extern.log4j.Log4j2;
import net.minecraft.server.MinecraftServer;
import personthecat.catlib.command.LibCommandRegistrar;
import personthecat.cavegenerator.commands.CommandCave;
import personthecat.cavegenerator.commands.arguments.FeatureArgument;
import personthecat.cavegenerator.config.Cfg;
import personthecat.cavegenerator.io.JarFiles;
import personthecat.cavegenerator.noise.CachedNoiseHelper;
import personthecat.cavegenerator.util.Reference;
import personthecat.overwritevalidator.annotations.OverwriteTarget;
import personthecat.overwritevalidator.annotations.PlatformMustInherit;

@Log4j2
@OverwriteTarget(required = true)
public class CaveGenerator {

    @PlatformMustInherit
    public void initCommon() {
        JarFiles.copyFiles();
        Cfg.register();
        LibCommandRegistrar.registerCommands(Reference.MOD_DESCRIPTOR, true, CommandCave.class);
        FeatureArgument.register();
    }

    @PlatformMustInherit
    public void serverStarting(final MinecraftServer server) {
        log.info("Loading cave generators");
        CaveRegistries.loadAll();
        CaveRegistries.COMMAND_SOURCE.create(server);
    }

    @PlatformMustInherit
    @SuppressWarnings("unused")
    public void serverStopping(final MinecraftServer server) {
        log.info("Unloading cave generators.");
        CaveRegistries.resetAll();
        CachedNoiseHelper.removeAll();
        CaveRegistries.COMMAND_SOURCE.clear();
    }
}
