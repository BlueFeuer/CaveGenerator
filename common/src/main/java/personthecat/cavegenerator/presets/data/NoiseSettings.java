package personthecat.cavegenerator.presets.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.hjson.JsonObject;
import personthecat.catlib.data.FloatRange;
import personthecat.catlib.data.Range;
import personthecat.catlib.util.HjsonMapper;
import personthecat.cavegenerator.noise.CachedNoiseGenerator;
import personthecat.cavegenerator.noise.DummyGenerator;
import personthecat.fastnoise.FastNoise;
import personthecat.fastnoise.data.*;
import personthecat.fastnoise.generator.PerlinNoise;

import java.util.Optional;
import java.util.Random;

import static java.util.Optional.empty;
import static personthecat.catlib.util.Shorthand.full;

/**
 * This is a variant of {@link NoiseRegionSettings} which contains additional fields
 * specifically relevant to 3-dimensional noise generation. I am looking into how
 * I can remove one or both of them, as they are both almost completely redundant.
 */
@FieldNameConstants
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class NoiseSettings {

    /** A seed used for <b>producing</b> new seeds from a given value. */
    @Default Optional<Integer> seed = empty();

    /** The target waveform frequency produced by the generator. */
    @Default float frequency = 1.0F;

    /** The threshold of acceptable values produced by the generator. */
    @Default FloatRange threshold = Range.of(0.0F);

    /** Scales the noise produced * x. */
    @Default float stretch = 1.0F;

    /** The scale of gaps produced in fractal patterns. */
    @Default float lacunarity = 1.0F;

    /** The octave gain for fractal noise types. */
    @Default float gain = 0.5F;

    /** The maximum amount to warp coordinates when warping is enabled. */
    @Default float warpAmplitude = 1.0F;

    /** The frequency used in warping input coordinates. */
    @Default float warpFrequency = 1.0F;

    /** The maximum amount a cellular point can move off grid. (x-axis) */
    @Default float jitterX = 0.45F;

    /** The maximum amount a cellular point can move off grid. (y-axis) */
    @Default float jitterY = 0.45F;

    /** The maximum amount a cellular point can move off grid. (z-axis) */
    @Default float jitterZ = 0.45F;

    /** The number of generation passes, i.e. the resolution. */
    @Default int octaves = 1;

    /** The vertical offset applied to the noise generator. */
    @Default int offset = 0;

    /** Whether to invert the acceptable values generated. */
    @Default boolean invert = false;

    /** Whether to cache the output for equivalent generators in the current chunk. */
    @Default boolean cache = false;

    /** Whether to treat this noise generator as a single value, improving performance. */
    @Default boolean dummy = false;

    /** The output to use if this generator is a dummy. */
    @Default float dummyOutput = 1.0F;

    /** The amplitude of the ping pong fractal type, when used. */
    @Default float pingPongStrength = 2.0F;

    /** The type of noise generator to run. */
    @Default NoiseType type = NoiseType.SIMPLEX;

    /** Determines how the noise will be fractalized, if applicable. */
    @Default FractalType fractal = FractalType.FBM;

    /** The type of warping applied to input coordinates. */
    @Default DomainWarpType warp = DomainWarpType.NONE;

    /** The type of distance function used with cellular noise types. */
    @Default CellularDistanceType distFunc = CellularDistanceType.EUCLIDEAN;

    /** The return type from cellular noise types. */
    @Default CellularReturnType returnType = CellularReturnType.DISTANCE2;

    /** The noise type used when returnType is set to {@code NoiseLookup}. */
    @Default NoiseType cellularLookup = NoiseType.SIMPLEX;

    public static NoiseSettings from(final JsonObject json, final NoiseSettings defaults) {
        return copyInto(json, defaults.toBuilder());
    }

    public static NoiseSettings from(final JsonObject json) {
        return copyInto(json, builder());
    }

    private static NoiseSettings copyInto(final JsonObject json, final NoiseSettingsBuilder builder) {
        return new HjsonMapper<>("<noise>", NoiseSettingsBuilder::build)
            .mapInt(Fields.seed, (b, i) -> b.seed(full(i)))
            .mapFloat(Fields.frequency, NoiseSettingsBuilder::frequency)
            .mapFloatRange(Fields.threshold, NoiseSettingsBuilder::threshold)
            .mapFloat(Fields.stretch, NoiseSettingsBuilder::stretch)
            .mapFloat(Fields.lacunarity, NoiseSettingsBuilder::lacunarity)
            .mapFloat(Fields.gain, NoiseSettingsBuilder::gain)
            .mapFloat(Fields.warpAmplitude, NoiseSettingsBuilder::warpAmplitude)
            .mapFloat(Fields.warpFrequency, NoiseSettingsBuilder::warpFrequency)
            .mapFloat("jitter", (b, i) -> b.jitterX(i).jitterY(i).jitterZ(i))
            .mapFloat(Fields.jitterX, NoiseSettingsBuilder::jitterX)
            .mapFloat(Fields.jitterY, NoiseSettingsBuilder::jitterY)
            .mapFloat(Fields.jitterZ, NoiseSettingsBuilder::jitterZ)
            .mapInt(Fields.octaves, NoiseSettingsBuilder::octaves)
            .mapInt(Fields.offset, NoiseSettingsBuilder::offset)
            .mapBool(Fields.invert, NoiseSettingsBuilder::invert)
            .mapBool(Fields.cache, NoiseSettingsBuilder::cache)
            .mapBool(Fields.dummy, NoiseSettingsBuilder::dummy)
            .mapFloat(Fields.dummyOutput, NoiseSettingsBuilder::dummyOutput)
            .mapFloat(Fields.pingPongStrength, NoiseSettingsBuilder::pingPongStrength)
            .mapNoiseType(Fields.type, NoiseSettingsBuilder::type)
            .mapFractalType(Fields.fractal, NoiseSettingsBuilder::fractal)
            .mapEnum(Fields.warp, DomainWarpType.class, NoiseSettingsBuilder::warp)
            .mapDistFunc(Fields.distFunc, NoiseSettingsBuilder::distFunc)
            .mapReturnType(Fields.returnType, NoiseSettingsBuilder::returnType)
            .mapNoiseType(Fields.cellularLookup, NoiseSettingsBuilder::cellularLookup)
            .create(builder, json);
    }

    /** Converts these settings into a regular {@link FastNoise} object. */
    public FastNoise getGenerator(final Random rand, final long seed) {
        if (dummy) {
            return new DummyGenerator(dummyOutput);
        }
        final NoiseDescriptor cfg = FastNoise.createDescriptor()
            .seed(getSeed(rand, seed))
            .noise(type)
            .frequency(frequency)
            .fractal(fractal)
            .octaves(octaves)
            .invert(invert)
            .gain(gain)
            .cellularReturn(returnType)
            .warpAmplitude(warpAmplitude)
            .warpFrequency(warpFrequency)
            .warp(warp)
            .lacunarity(lacunarity)
            .noiseLookup(FastNoise.createDescriptor().noise(cellularLookup))
            .distance(distFunc)
            .jitterX(jitterX)
            .jitterY(jitterY)
            .jitterZ(jitterZ)
            .threshold(threshold.min, threshold.max)
            .frequencyY(frequency / stretch)
            .offset(offset)
            .pingPongStrength(pingPongStrength);

        final FastNoise generator = cfg.generate();

        return cache ? new CachedNoiseGenerator(cfg, generator) : generator;
    }

    private int getSeed(final Random rand, final long seed) {
        return this.seed.map(num -> {
            final FastNoise simple = new PerlinNoise(new Random(seed).nextInt());
            return Float.floatToIntBits(simple.getNoise(num));
        }).orElseGet(rand::nextInt);
    }
}