package com.personthecat.cavegenerator.world.feature;

import com.personthecat.cavegenerator.world.GeneratorController;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WorldContext {
    final int[][] heightmap;
    final GeneratorController gen;
    final Random rand;
    final int chunkX, chunkZ, offsetX, offsetZ;
    final World world;
    final List<Biome> proxyBiomes;

    public WorldContext(
        int[][] heightmap,
        GeneratorController gen,
        Random rand,
        int chunkX,
        int chunkZ,
        World world,
        List<Biome> proxyBiomes
    ) {
        this.heightmap = heightmap;
        this.gen = gen;
        this.rand = rand;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.offsetX = chunkX * 16 + 8;
        this.offsetZ = chunkZ * 16 + 8;
        this.world = world;
        this.proxyBiomes = proxyBiomes;
    }
}
