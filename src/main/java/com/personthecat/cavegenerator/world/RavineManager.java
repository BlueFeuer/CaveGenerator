package com.personthecat.cavegenerator.world;

import com.personthecat.cavegenerator.CaveInit;
import com.personthecat.cavegenerator.Main;
import com.personthecat.cavegenerator.config.ConfigFile;
import static com.personthecat.cavegenerator.util.CommonMethods.*;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

import java.util.Map;
import java.util.Optional;

public class RavineManager extends MapGenBase {

    private final Optional<MapGenBase> priorRavines;

    public RavineManager(Optional<MapGenBase> priorRavines) {
        this.priorRavines = priorRavines;
    }

    @Override
    public void generate(World world, int x, int z, ChunkPrimer primer) {
        // Again, these must be retrieved statically. Can't
        // Change this method's signature.
        // Generators were loaded on CaveManager#generate.
        Map<String, CaveGenerator> generators = Main.instance.loadGenerators(world);
        int dimension = world.provider.getDimension();

        if (ConfigFile.otherGeneratorEnabled) {
            // Generate simultaneously with one other generator.
            priorRavines.ifPresent((gen) ->
                gen.generate(world, x, z, primer)
            );
        } else if (!CaveInit.anyGeneratorEnabled(generators, dimension)) {
            // No generators are enabled for this dimension.
            // Allow the most recent mod in the queue to
            // generate and then move on.
            priorRavines.ifPresent((gen) ->
                gen.generate(world, x, z, primer)
            );
            return;
        }
        // Calls `recursiveGenerate()` recursively.
        super.generate(world, x, z, primer);
    }

    @Override
    protected void recursiveGenerate(World world, int chunkX, int chunkZ, int originalX, int originalZ, ChunkPrimer primer) {
        final int dimension = world.provider.getDimension();
        for (CaveGenerator gen : Main.instance.loadGenerators(world).values()) {
            Biome centerBiome = world.getBiome(centerCoords(chunkX, chunkZ));

            // Filter generators that aren't enabled under these conditions
            // and generate by probability.
            if (gen.canGenerate(dimension, centerBiome)) {
                gen.startRavines(rand, chunkX, chunkZ, originalX, originalZ, primer);
            }
        }
    }
}