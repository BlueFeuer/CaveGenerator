package com.personthecat.cavegenerator.model;

import com.personthecat.cavegenerator.data.ShellSettings;
import com.personthecat.cavegenerator.noise.DummyGenerator;
import fastnoise.FastNoise;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfiguredShell {

    public static final ConfiguredShell EMPTY_SHELL = new ConfiguredShell();

    public final ShellSettings cfg;
    public final double sphereRadius;
    public final float noiseThreshold;
    public final List<Decorator> decorators;

    public ConfiguredShell(ShellSettings cfg, World world) {
        this.cfg = cfg;
        this.sphereRadius = cfg.sphereRadius;
        this.noiseThreshold = cfg.noiseThreshold.orElse(((float) cfg.sphereRadius + 0.0001F) / 10.0F);
        this.decorators = map(cfg.decorators, d -> new Decorator(d, world));
    }

    private ConfiguredShell() {
        this.cfg = ShellSettings.builder().build();
        this.sphereRadius = 0.0;
        this.noiseThreshold = 0.0F;
        this.decorators = Collections.emptyList();
    }

    private static <T, U> List<U> map(List<T> list, Function<T, U> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static class Decorator {

        public final ShellSettings.Decorator cfg;
        public final FastNoise noise;

        private Decorator(ShellSettings.Decorator cfg, World world) {
            this.cfg = cfg;
            this.noise = cfg.noise.map(n -> n.getGenerator(world)).orElse(new DummyGenerator(0F));
        }

        public boolean matches(IBlockState state) {
            if (cfg.matchers.isEmpty()) {
                return state.isOpaqueCube();
            }
            return cfg.matchers.contains(state);
        }

        /**
         * Returns true if the replacement doesn't have noise or
         * if its noise at the given coords meets the threshold.
         */
        public boolean testNoise(int x, int y, int z, int chunkX, int chunkZ) {
            int actualX = (chunkX * 16) + x;
            int actualZ = (chunkZ * 16) + z;
            return testNoise(actualX, y, actualZ);
        }

        /** Variant of testNoise() that uses absolute coordinates. */
        private boolean testNoise(int x, int y, int z) {
            return noise.GetBoolean(x, y, z);
        }
    }
}
