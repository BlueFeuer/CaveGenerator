# CaveGenerator
A powerful and customizable replacement for Mojang's cave generator.

### Changes in this fork:

#### Universal fields:  
&nbsp;&nbsp;&nbsp;&nbsp;proxyDimension - Int, set the index from config to use for the dimension to check the biomes from in place of the current dimension.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Examples coming later, soft requires Dimensional Control and Just Enough Dimensions to use.  
#### Cavern fields:
&nbsp;&nbsp;&nbsp;&nbsp;archaeneHeight - Array of floats to add/subtract from the cavern's threshold based on Y level.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Every value in the array corresponds to a specific Y level, starting at Y=0.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example in `blue_templateS_archaeneheight.cave`, used in `canyons.cave`.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneWalls - Array of exactly 32 floats to add/subtract from the cavern's threshold based on distance to an invalid biome.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Distance is determined by an `archaeneWallArray`.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example in `blue_templates_archaenewalls`, used in `canyons.cave`.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneWallArray - Array of ints to determine how invalid biomes close off caverns.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Better explanation and spreadsheet to generate values coming soon™.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"Examples" in `blue_templates_archaenearrays`, used in `canyons.cave`.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneWallFixed - Completely changes cavern generation if set, uses an identical array to `archaeneWallArray`.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Better explanation and spreadsheet to generate values coming soon™.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneInterpolation - Boolean, whether the cavern should use interpolation or not.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example in `canyons.cave`.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneInterpX - Spacing between samples on the X axis.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Valid values are 1, 2, 3, 4, 5, 6, 7, 8, and 16. Default 3.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5 is a good middleground between performance and quality.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneInterpY - Spacing between samples on the Y axis.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Valid values are 1, 2, 3, 4, 8, and 16. Default 2.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4 is a good middleground between performance and quality.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneInterpZ - Spacing between samples on the Z axis.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Valid values are 1, 2, 3, 4, 5, 6, 7, 8, and 16. Default 3.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5 is a good middleground between performance and quality.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneInterpStretch - Float, attempts to 'slur' caverns upward a bit to make them taller.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;When sampling a cavern, if the previous sample down is higher, multiply the difference by this and add it to the current sample.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Example coming soon™.  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneDecorator - Boolean, completely disables the cavern/noise generation, and only checks for valid blocks to decorate.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Can have use an `archaeneWallArray` to better transition into invalid biomes, where values less than 20 are decorated randomly.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Examples in `canyons.cave`.  
#### Structure fields:
&nbsp;&nbsp;&nbsp;&nbsp;archaeneX - Int, sets a fixed X coordinate within a chunk for the structure to generate.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`archaeneX: 15 # Structure will always attempt to generate along the eastmost edge of a chunk.`  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneY - Int, sets a fixed Y coordinate within a chunk for the structure to generate.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`archaeneX: 128 # Structure will always attempt to generate at a Y of 128.`  
&nbsp;&nbsp;&nbsp;&nbsp;archaeneZ - Int, sets a fixed Z coordinate within a chunk for the structure to generate.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`archaeneZ: 15 # Structure will always attempt to generate along the southmost edge of a chunk.`  
&nbsp;&nbsp;&nbsp;&nbsp;rotation - Array of possible rotations, picked randomly. Also rotates all offsets/block checks.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0 = 270° rotation - x,y -> y,-x  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1 = 90° rotation - x,y -> -y,x  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2 = 180° rotation - x,y -> -x,-y  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3 = 0° rotation - default  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`rotation: [0, 1, 2] # Structure is attempted to be generated rotated either 90, 180, or 270 degrees from it's normal orientation.`  
&nbsp;&nbsp;&nbsp;&nbsp;(-) rotateRandomly - Removed  
#### Misc changes:  
&nbsp;&nbsp;&nbsp;&nbsp;Caverns only decorate a block if they successfully replace it.  
