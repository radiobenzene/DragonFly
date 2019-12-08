package AllTerrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import AllModels.RawModel;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TexturePackage;
import Tools.Mathematics;
import renderEngine.Loader;

//Class for representing Terrain in a game 
public class Terrain {
	
	private static final float SIZE = 900;
	private static final int VERTEX_COUNT = 128; 
	private static final float MAX_HEIGHT = 50;
	private static final float MIN_HEIGHT = -50;
	private static final float MAX_PIXEL_COLOR = 250 * 256 * 256; //Three color channels
	
	private float x;
	private float z;
	private float [] [] heights; //Table of heights for collision detection
	
	private RawModel model;//Actual Terrain Mesh
	private TexturePackage texturePack;
	private TerrainTexture BlendMap;
	
	
	public Terrain(int gridX, int gridZ, Loader loader, TexturePackage texture_pack, TerrainTexture Blendmap,String height_map)
	{
		this.texturePack = texture_pack;
		this.BlendMap = Blendmap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = GenerateTerrain(loader, height_map);
	}

	public void setX(float x) {
		this.x = x;
	}


	public float getZ() {
		return z;
	}


	public void setZ(float z) {
		this.z = z;
	}


	public RawModel getModel() {
		return model;
	}


	public void setModel(RawModel model) {
		this.model = model;
	}


	public TexturePackage getTexturePack() {
		return texturePack;
	}

	public void setTexturePack(TexturePackage texturePack) {
		this.texturePack = texturePack;
	}

	public TerrainTexture getBlendMap() {
		return BlendMap;
	}

	public void setBlendMap(TerrainTexture blendMap) {
		BlendMap = blendMap;
	}

	public static float getSize() {
		return SIZE;
	}

	public static int getVertexCount() {
		return VERTEX_COUNT;
	}



	//To Generate Terrain - Copied Text
	private RawModel GenerateTerrain(Loader loader, String height_map)
	{
		HeightsGenerator generator = new HeightsGenerator(); //To generate the Procedural terrain using Perlin Noise
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("res/" + height_map + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		int VERTEX_COUNT = 128; //To get the vertex count
		heights = new float [VERTEX_COUNT] [VERTEX_COUNT];
		//Generating a completely flat terrain
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++)
		{
			for(int j=0;j<VERTEX_COUNT;j++)
			{
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float Height = GetHeight(j,i,generator);
				heights[j][i] = Height;  
				vertices[vertexPointer*3+1] = Height; //Get Height of the vertex
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = CalcNormal(j,i, generator);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.LoadToVAO(vertices, textureCoords, normals, indices);
	}


	public float getX() {
		return x;
	}
	
	private float GetHeight(int x, int y, HeightsGenerator generator)
	{
		return generator.GenerateHeight(x, y);
	}
	
	private Vector3f CalcNormal(int x, int z, HeightsGenerator generator)
	{
		float heightL = GetHeight(x-1, z, generator);
		float heightR = GetHeight(x+1, z, generator);
		float heightD = GetHeight(x, z-1, generator);
		float heightU = GetHeight(x, z+1, generator);
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD-heightU);
		normal.normalise(); //For length to be 1
		return normal;
	}
	
	public float GetTerrainHeight(float globalX, float globalZ)
	{
		float TerrainX = globalX - this.x;
		float TerrainZ = globalZ - this.z;
		float GridSize = SIZE / ((float)heights.length - 1);
		int gridX = (int) Math.floor(TerrainX / GridSize);
		int gridZ = (int) Math.floor(TerrainZ / GridSize);
		
		if((gridX >= heights.length - 1) || (gridZ >= heights.length -1) || (gridX < 0) || (gridZ < 0))
		{
			return 0;
		}
		float xCoordinate = (TerrainX % GridSize) / GridSize;
		float zCoordinate = (TerrainZ % GridSize) / GridSize;
		float mapped; //Gets our return value
		if(xCoordinate <= (1 - zCoordinate))
		{
			mapped = BarryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
					heights[gridX + 1][gridZ], 0), new Vector3f(0,
					heights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
		}
		else
		{
			mapped = BarryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
					heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
					heights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
		}
		return mapped;
	}

	//Can be shifted to mathematics class
	//Using BarryCentric coordinate system
	public static float BarryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}


}
