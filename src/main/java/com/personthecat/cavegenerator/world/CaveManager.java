package com.personthecat.cavegenerator.world;

import java.util.Random;

import com.personthecat.cavegenerator.CaveInit;
import com.personthecat.cavegenerator.util.RandomChunkSelector;
import com.personthecat.cavegenerator.util.SimplexNoiseGenerator3D;
import com.personthecat.cavegenerator.util.Values;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorSimplex;

public class CaveManager extends MapGenBase
{
	protected static SimplexNoiseGenerator3D noise;
	protected static NoiseGeneratorSimplex noise2D1;
	protected static NoiseGeneratorSimplex noise2D2;
	protected static NoiseGeneratorSimplex noise2D3;

	protected static RandomChunkSelector selector;
	
	@Override
	public void generate(World world, int x, int z, ChunkPrimer primer)
	{
		int dimension = world.provider.getDimension();
		
		if (!CaveInit.isAnyGeneratorEnabledForDimension(dimension))
		{
			ReplaceVanillaCaveGen.previousCaveGen.generate(world, x, z, primer);
			
			return;
		}
		
		setupNoiseGenerators(world); //Only happens 1x / world.
		setGeneratorReferences(world, x, z);
		noiseGenerate(world, dimension, x, z, primer);
		super.generate(world, x, z, primer); //recursiveGenerate()
	}
	
	protected void noiseGenerate(World world, int dimension, int chunkX, int chunkZ, ChunkPrimer primer)
	{
		for (CaveGenerator generator : CaveInit.GENERATORS.values())
		{
			Biome centerBiome = world.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
			
			if (generator.canGenerate(centerBiome, dimension))
			{				
				if (generator.cavernsEnabled || generator.stoneLayers.length > 0)
				{
					generator.addNoiseFeatures(chunkX, chunkZ, primer);
				}
			}
		}
	}
	
	/**
	 * A few processes here could be simplified while producing similar results,
	 * but they would still break seeds. */
	@Override
	protected void recursiveGenerate(World world, int chunkX, int chunkZ, int originalX, int originalZ, ChunkPrimer primer)
	{
		for (CaveGenerator generator : CaveInit.GENERATORS.values())
		{
			//Only test the biome 1x.
			Biome centerBiome = world.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
			
			if (generator.canGenerate(centerBiome, world.provider.getDimension()))
			{
				int caveFrequency = 0;
				
				if (generator.tunnelFrequency != 0)
				{
					caveFrequency = rand.nextInt(rand.nextInt(rand.nextInt(generator.tunnelFrequency) + 1) + 1);
				}
				
				//Do it in this order to maintain seeds as much as possible, just for now.
				if (generator.spawnIsolatedInverseChance != 0 && rand.nextInt(generator.spawnIsolatedInverseChance) != 0)
				{
					caveFrequency = 0;
				}

				for (int i = 0; i < caveFrequency; i++)
				{
					double x = (chunkX * 16) + rand.nextInt(16),
					       y = rand.nextInt(rand.nextInt(generator.maxHeight - generator.minHeight) + generator.minHeight),
					       z = (chunkZ * 16) + rand.nextInt(16);
					
					int branches = 1;
					
					if (this.rand.nextInt(generator.spawnInSystemInverseChance) == 0)
					{
						generator.addRoom(rand.nextLong(), originalX, originalZ, primer, x, y, y);
						branches += rand.nextInt(4);
					}
					
					runGenerator(generator, branches, primer, originalX, originalZ, x, y, z);
				}
			}
		}
	}
    
    private void runGenerator(CaveGenerator generator, int numDirections, ChunkPrimer primer, int originalX, int originalZ, double x, double y, double z)
    {
    	for (int j = 0; j < numDirections; j++)
		{
			float slopeXZ = generator.startingSlopeXZ,
         	      slopeY = generator.startingSlopeY,
         	      scale = generator.startingScale;
         	
        	slopeXZ += generator.startingSlopeXZRandFactor *(rand.nextFloat() * Values.PI_TIMES_2);
        	slopeY += generator.startingSlopeYRandFactor * (rand.nextFloat() - 0.5F);
        	scale += generator.startingScaleRandFactor * (rand.nextFloat() * 2.0F + rand.nextFloat());

        	if (this.rand.nextInt(10) == 0) //Randomly increase size. From vanilla. Needed for seeds.
        	{
        		scale *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
        	}

        	generator.addTunnel(rand.nextLong(), originalX, originalZ, primer, x, y, z, scale, slopeXZ, slopeY, 0, generator.startingDistance, generator.startingScaleY);
        }
    }
    
    /**
     * Needs to happen regardless of the chance in case selection is inconsistent.
     */
    protected void setGeneratorReferences(World world, int chunkX, int chunkZ)
    {
    	if (world != null)
    	{
        	int dimension = world.provider.getDimension();    		
    		
    		for (CaveGenerator generator : CaveInit.GENERATORS.values())
        	{
        		generator.world = world;
        		generator.rand = new Random(world.getSeed());
        		generator.range = range;
        	}
    	}
    }
    
    private static World previousWorld;    
    
    /**
     * Avoid setting up generators every single time generate() is called.
     */
    protected void setupNoiseGenerators(World world)
    {
    	if (world != null && !world.equals(previousWorld))
    	{
    		previousWorld = world;
    		
    		initStaticGenerators(world);
			
			StoneReplacer.setWorld(world);
    	}
    }
    
    private static void initStaticGenerators(World world)
    {
    	long seed = world.getSeed();
    	
    	noise = new SimplexNoiseGenerator3D(seed);
    	noise2D1 = new NoiseGeneratorSimplex(new Random(seed));
    	noise2D2 = new NoiseGeneratorSimplex(new Random(seed >> 2));
    	noise2D3 = new NoiseGeneratorSimplex(new Random(seed >> 4));
    	
    	selector = new RandomChunkSelector(seed);
    }
}