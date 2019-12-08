package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import AllEntities.Entity;
import AllModels.RawModel;
import AllModels.TexturedModel;
import AllTerrains.Terrain;
import Textures.ModelTexture;
import Textures.TexturePackage;
import Tools.Mathematics;
import shaders.TerrainShader;

//Class for rendering the Terrain
public class TerrainRenderer {

	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f ProjectionMatrix)
	{
		this.shader = shader;
		shader.Start();
		shader.LoadProjectionMatrix(ProjectionMatrix);
		shader.LoadAllTextures(); //Connecting all textures
		shader.Start();
	}
	
	public void RenderTerrain(List<Terrain> terrains)
	{
		for(Terrain terrain : terrains)
		{
			PrepareTerrain(terrain);
			LoadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES,terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			UnBindTexturedModel();
			
			
		}
	}
	
	private void PrepareTerrain(Terrain terrain)
	{
		RawModel rawmodel = terrain.getModel();
		GL30.glBindVertexArray(rawmodel.getVAO());
		GL20.glEnableVertexAttribArray(0); //VAO is in index 0
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2); //For Lighting
		BindAllTextures(terrain);
		shader.LoadShineVariables(2, 1);

	}
	
	private void BindAllTextures(Terrain terrain)
	{
		//Binding all the textures here
		TexturePackage Texture_Pack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture_Pack.getSnow_Texture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Texture_Pack.getMud_Texture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	private void UnBindTexturedModel()
	{
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void LoadModelMatrix(Terrain terrain)
	{
		//Creating Matrix here
				Matrix4f T_Matrix = Mathematics.Create_TMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 0, 0, 0, 1); //Terrain cannot rotate
				shader.LoadTransformationMatrix(T_Matrix);//Loading transformation matrix
	}

	
}
