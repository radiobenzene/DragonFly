package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import AllEntities.Camera;
import AllEntities.Entity;
import AllModels.RawModel;
import AllModels.TexturedModel;
import AllTerrains.Terrain;
import Textures.ModelTexture;
import Tools.Mathematics;
import shaders.StaticShader;

//EntityRenederer renders only entity model from VAO
public class EntityRenderer {
	
	
	
	private StaticShader shader;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	 private List<Terrain> terrains = new ArrayList<Terrain>();
	public EntityRenderer(StaticShader shader, Matrix4f ProjectionMatrix)
	{
		this.shader = shader;
		shader.Start();
		shader.LoadProjectionMatrix(ProjectionMatrix);
		shader.Stop();
	}
	
	
	public void Render(Map<TexturedModel,List<Entity>> entities)
	{
		//For efficient Rendering
		for(TexturedModel model: entities.keySet())
		{
			//For each textured model
			PrepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			
			for(Entity entity:batch)
			{
				//For each batch
				PrepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES,model.getRawmodel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			UnBindTexturedModel();
		}
		//UnBindTexturedModel();
		
	}
	
	private void PrepareTexturedModel(TexturedModel model)
	{
		
		RawModel rawmodel = model.getRawmodel();
		GL30.glBindVertexArray(rawmodel.getVAO());
		GL20.glEnableVertexAttribArray(0); //VAO is in index 0
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2); //For Lighting
		ModelTexture texture = model.getTexture();
		if(texture.isTransparent())
		{
			MasterRenderer.DisableCulling();
		}
		shader.LoadFakeLightingVariables(texture.isFakeLighting());
		shader.LoadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0); //Can change to GL_TEXTURE0
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.GetID());
	}
	
	private void UnBindTexturedModel()
	{
		MasterRenderer.EnableCulling();
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void PrepareInstance(Entity entity)
	{
		//Creating Matrix here
				Matrix4f T_Matrix = Mathematics.Create_TMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
				shader.LoadTransformationMatrix(T_Matrix);//Loading transformation matrix
	}

	


	private void ProcessTerrain(Terrain terrain) {
		 terrains.add(terrain);
		
	}
	 public void processEntity(Entity entity) {
	        TexturedModel entityModel = entity.getModel();
	        List<Entity> batch = entities.get(entityModel);
	        if (batch != null) {
	            batch.add(entity);
	        } else {
	            List<Entity> newBatch = new ArrayList<Entity>();
	            newBatch.add(entity);
	            entities.put(entityModel, newBatch);
	        }
	    }

	
}
