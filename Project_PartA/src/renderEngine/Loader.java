package renderEngine;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import AllModels.RawModel;
import Textures.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

//Class for Loading 3D models in memory
public class Loader {
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	public int LoadCubeMap(String[] TextureFiles)
	{
		int Texture_ID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, Texture_ID);
		for(int i = 0; i < TextureFiles.length; i++)
		{
			//target is for interpolation of the cube map
			TextureData data = decodeTextureFile("cube/" + TextureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());      
			
		}
		
		//Makes the texture mippmapped
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(Texture_ID);
		return Texture_ID;
	}
	
	
	public RawModel LoadToVAO(float[] positions,float[] TextureCoords,float[] normals, int[] index)
	{
		int vaoID = CreateVAO();//Creating new VAO here
		bindIndicesBuffer(index);
		StoreInList(0,3,positions);
		StoreInList(1,2,TextureCoords);//
		StoreInList(2,3,normals); //Each normal is e 3d vector
		UnbindVAO();
		return new RawModel(vaoID,index.length);//x y z vertex
	}
	
	public RawModel LoadToVAO(float[] positions,float[] TextureCoords,float[] normals, float[] tangents, int[] index)
	{
		int vaoID = CreateVAO();//Creating new VAO here
		bindIndicesBuffer(index);
		StoreInList(0,3,positions);
		StoreInList(1,2,TextureCoords);//
		StoreInList(2,3,normals); //Each normal is e 3d vector
		StoreInList(3,3,tangents); //Storing our tangents at the 3rd attribute list
		UnbindVAO();
		return new RawModel(vaoID,index.length);//x y z vertex
	}
	
	
	public RawModel LoadToVAO(float[] positions, int dimensions)
	{
		int vaoID = CreateVAO();
		this.StoreInList(0, dimensions, positions);
		UnbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	
	public int LoadTexture(String FileName)
	{
		//Function to load the Texture on a surface
		//Texture texture = null;
		Texture texture = null;
		try {
			
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + FileName + ".png")); //Opening Texture file fro src folder
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D); //Generating low resolution 
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -2.3f);
		   } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println("Tried to load texture " + FileName + ".png , didn't work");
	            System.exit(-1);
		}
		textures.add(texture.getTextureID()); //Adding texture into Textures List
		return texture.getTextureID();
	}
	
	public void CleanAll()
	{
		for(int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
			
		}
		for(int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures)
		{
			GL11.glDeleteTextures(texture); //Deleting textures at end of game
		}
	}
	
	private int CreateVAO()
	{
		//Creating VAO here
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID); //Binding here
		return vaoID;
	}
	
	private void StoreInList(int attrNum, int CoordSize, float[] data)
	{
		//Stores the data in the attribute list as a vbo
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //Binding here
		FloatBuffer buffer = StoreDatainFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//Never changing data so static;
		GL20.glVertexAttribPointer(attrNum, CoordSize, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //Unbinding here 
	}
	
	private void UnbindVAO()
	{
		//Unbinding VAO here
		GL30.glBindVertexArray(0); //Unbinding here
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = StoreInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer StoreInIntBuffer(int[] data)
	{
		 IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		 buffer.put(data);
		 buffer.flip();//To Read
		 return buffer;
		 
	}
	private FloatBuffer StoreDatainFloatBuffer(float[] data)
	{
		//Converts to FloatBuffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();//Finished writing to it
		return buffer;
	}

}
