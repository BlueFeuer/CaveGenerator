package com.personthecat.cavegenerator.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;

import java.util.LinkedHashMap;


public class BiomeCache {

    private static BiomeCacheMap<Long, Biome> cache = new BiomeCacheMap<>();

    // Really simple hash. Collisions *should* be far enough apart to the point they won't matter.
    private static long getHash(int x, int z, int dim) {
        return ((((431 + (long)x)*97429)+(long)z)*2713) + (long)dim;
    }

    // Calls to this should always be to the center block of the chunk.
    public static Biome getCachedBiome(int x, int z, WorldProvider world) {
        int dim = world.getDimension();
        long hash = getHash(x/16, z/16, dim);
        Biome b = cache.get(hash);
        if (b != null)
            return b;
        b = world.getBiomeProvider().getBiome(new BlockPos(x, 0, z));
        cache.put(hash, b);
        return b;
    }


}
