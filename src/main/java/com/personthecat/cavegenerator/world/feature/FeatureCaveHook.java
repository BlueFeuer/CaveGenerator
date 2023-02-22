package com.personthecat.cavegenerator.world.feature;

import com.personthecat.cavegenerator.Main;
import com.personthecat.cavegenerator.config.ConfigFile;
import com.personthecat.cavegenerator.data.ConditionSettings;
import com.personthecat.cavegenerator.noise.CachedNoiseHelper;
import com.personthecat.cavegenerator.world.BiomeSearch;
import com.personthecat.cavegenerator.world.GeneratorController;
import com.personthecat.cavegenerator.world.HeightMapLocator;
import com.personthecat.cavegenerator.world.generator.PrimerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.common.DimensionManager;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class FeatureCaveHook implements IWorldGenerator {

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGen, IChunkProvider chunkProv) {
        // Once again, there is no way to avoid retrieving this statically.
        final Map<String, GeneratorController> generators = Main.instance.loadGenerators(world);
        final int[][] heightmap = HeightMapLocator.getHeightFromWorld(world, chunkX, chunkZ);

        final int centerX = chunkX*16 + 8;
        final int centerZ = chunkZ*16 + 8;
        final Biome center = world.getBiomeProvider().getBiome(new BlockPos(centerX, 0, centerZ));

        // Get biome at the center of a chunk for all proxy dimensions to add to context
        List<Biome> proxyBiomes = new ArrayList<Biome>();
        for (int i = 0; i < ConfigFile.archaeneProxyDims.length; i++) {
            if(world.provider.getDimension() == ConfigFile.archaeneUseProxyDims[i]
                    && ConfigFile.archaeneProxyDims[i] != ConfigFile.archaeneUseProxyDims[i]) {
                final BiomeProvider provider = DimensionManager.getWorld(ConfigFile.archaeneProxyDims[i]).getBiomeProvider();
                proxyBiomes.add(provider.getBiome(new BlockPos(centerX, 0, centerZ)));
            }
        }

        for (GeneratorController generator : generators.values()) {
            final WorldContext ctx = new WorldContext(heightmap, generator, rand, chunkX, chunkZ, world, proxyBiomes);
            generator.featureGenerate(ctx);
        }
        CachedNoiseHelper.resetAll();
    }
}