package AllTerrains;

import java.util.Random;

//Heights Generator uses Perlin Noise to create terrains
public class HeightsGenerator {
	private static final float AMPLITUDE = 70.0f; //Determines how high and low the terrain can be
	private Random random = new Random();
	private int seed; //For random
	
	public static final int RANDOM_NUM = 100000000;
	public HeightsGenerator()
	{ 
		//Constructor to initizalise seed
		this.seed = random.nextInt(RANDOM_NUM);
	}
	
	public float GenerateHeight(int x, int z)
	{
		//Function to generate Height
		float total = GetInterpolatedNoise(x/16f,z/16f) * AMPLITUDE;
		total += GetInterpolatedNoise(x/8f,z/8f) * AMPLITUDE/3f;
		total += GetInterpolatedNoise(x/4f,z/4f) * AMPLITUDE/9f;
		return total;
	}
	
	private float GetNoise(int x, int z)
	{
		//Function that returns a random number between 1 and -1
		random.setSeed(x * 54132 + z * 18271 + seed);
		return random.nextFloat() * 2f - 1f;
		
	}
	
	private float GetSmoothNoise(int x, int z)
	{
		//returns average height
		float corners = (GetNoise(x-1,z-1) + GetNoise(x+1,z-1) + GetNoise(x-1,z+1) + GetNoise(x+1,z+1))/16f;            
		float sides = (GetNoise(x-1,z) + GetNoise(x,z-1) + GetNoise(x+1,z) + GetNoise(x,z+1))/8f;     
		float center = (GetNoise(x,z))/4f;
		return corners + sides + center;	
	}
	
	private float Interpolate(float a, float b, float blend)
	{
		//Using Cosine Interpolation instead of Linear Interpolation
		double angle = blend * Math.PI;
		float f = (float)(1f - Math.cos(angle)) * 0.5f; //Creating blend factor here
		return a * (1f-f) + b * f; //Usual Linear interpolation 
	}
	
	private float GetInterpolatedNoise(float x, float z)
	{
		int intX = (int)x;
		int intZ = (int)z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		
		float v1 = GetSmoothNoise(intX, intZ);
		float v2 = GetSmoothNoise(intX + 1, intZ);
		float v3 = GetSmoothNoise(intX, intZ + 1);
		float v4 = GetSmoothNoise(intX + 1, intZ + 1);
		
		float interpolate1 = Interpolate(v1,v2,fracX);
		float interpolate2 = Interpolate(v3,v4,fracX);
		
		return Interpolate(interpolate1, interpolate2, fracZ);
	}
}


