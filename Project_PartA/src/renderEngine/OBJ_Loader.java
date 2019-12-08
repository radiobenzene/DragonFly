package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import AllModels.RawModel;

//This class loads a 3D object that has an OBJ extension
//Parsing the OBJ File
public class OBJ_Loader {
	public static RawModel LoadOBJModel(String FileName, Loader loader)
	{
		FileReader file_reader = null;
		try {
			file_reader = new FileReader(new File("res/" + FileName + ".obj"));//Loading OBJ file here
		} catch (FileNotFoundException e) {
			System.err.println("Cannot Load Object File(OBJ Format");
			e.printStackTrace();
		}
		
		
		//To read the file information
		BufferedReader reader = new BufferedReader(file_reader);
		String line;
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		//To read through arrays
		float[] VertexArray = null;
		float[] NormalArray = null;
		float[] TextureArray = null;
		int[] IndexArray = null;
		
		try
		{
			
			while(true)
			{
				line = reader.readLine();
				String[] current_line = line.split(" ");
				
				if(line.startsWith("v "))
				{
					Vector3f vertex = new Vector3f(Float.parseFloat(current_line[1]), Float.parseFloat(current_line[2]), Float.parseFloat(current_line[3]));
					vertices.add(vertex);
					
					
				}
				else if(line.startsWith("vt "))
				{
					//Texture Coordinate
					Vector2f texture = new Vector2f(Float.parseFloat(current_line[1]), Float.parseFloat(current_line[2]));
					textures.add(texture);
				}
				else if(line.startsWith("vn "))
				{
					//If Normal
					Vector3f normal = new Vector3f(Float.parseFloat(current_line[1]), Float.parseFloat(current_line[2]), Float.parseFloat(current_line[3]));
					normals.add(normal);
					
				}
				else if(line.startsWith("f "))
				{
					//If face
					TextureArray = new float[vertices.size() * 2];
					NormalArray = new float[vertices.size() * 3];
					break; //Breaking out of the loop
				}
			}
			
			//Specially for the face lines
			while(line != null)
			{
				if(!line.startsWith("f "))
				{
					//Line does not start with f
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				ProcessVertex(vertex1, indices, textures, normals, TextureArray, NormalArray);
				ProcessVertex(vertex2, indices, textures, normals, TextureArray, NormalArray);
				ProcessVertex(vertex3, indices, textures, normals, TextureArray, NormalArray);
				line = reader.readLine();
				
			}
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//Converting Lists to arrays here
		VertexArray = new float[vertices.size() * 3];
		IndexArray = new int[indices.size()];
		
		int vertex_index = 0;
		for(Vector3f vertex: vertices)
		{
			VertexArray[vertex_index++] = vertex.x;
			VertexArray[vertex_index++] = vertex.y;
			VertexArray[vertex_index++] = vertex.z;
		}
		for(int i = 0; i < indices.size(); i++)
		{
			IndexArray[i] = indices.get(i);
		}
		return loader.LoadToVAO(VertexArray, TextureArray, NormalArray, IndexArray);
	}
	
	private static void ProcessVertex(String[] Vertex_Data, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] TextureArray, float[] NormalArray)
	{
		int CurrentVertex = Integer.parseInt(Vertex_Data[0]) -1;
		indices.add(CurrentVertex);
		Vector2f CurrentTexture = textures.get(Integer.parseInt(Vertex_Data[1]) - 1);
		TextureArray[CurrentVertex * 2] = CurrentTexture.x;
		TextureArray[CurrentVertex * 2 + 1] = 1 - CurrentTexture.y; //Starting from Top-Left Direction in OpenGL
		Vector3f CurrentNormal = normals.get(Integer.parseInt(Vertex_Data[2])-1);
		NormalArray[CurrentVertex * 3] = CurrentNormal.x;
		NormalArray[CurrentVertex * 3 + 1] = CurrentNormal.y;
		NormalArray[CurrentVertex * 3 + 2] = CurrentNormal.z;	
	}
}

