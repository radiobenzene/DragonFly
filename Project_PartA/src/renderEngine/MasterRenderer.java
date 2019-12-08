package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import AllEntities.Camera;
import AllEntities.Entity;
import AllEntities.Light;
import AllModels.TexturedModel;
import AllTerrains.Terrain;
import normalMappingRenderer.NormalMappingRenderer;
//import NormalMapRenderer.NormalMappingRenderer;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyBoxRenderer;

//Class for much more efficient rendering of entities
public class MasterRenderer {
	
	private static final float Field_Of_View = 80;
	private static final float Near_Plane = 0.1f;
	private static final float Far_Plane = 1000; //Decides how far we can see
	
	public static final float RED = 0.54f;
	public static final float BLUE = 0.65f;
	public static final float GREEN = 0.58f;
	
	private Matrix4f ProjectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	// private TerrainRenderer terrainRenderer;
	  //  private TerrainShader terrainShader = new TerrainShader();
	    
	private NormalMappingRenderer normalMapRenderer;
	private TerrainRenderer terrain_renderer;
	private TerrainShader terrain_shader = new TerrainShader();
	//Creating Hash Map for rendering multiple entities
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> NormalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>(); //List of Terrains
	
	public static void EnableCulling()
	{
		//Enables Culling
		GL11.glEnable(GL11.GL_CULL_FACE);//To stop rendering triangles on the other side
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void DisableCulling()
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	private SkyBoxRenderer skyboxRenderer;
	
	public MasterRenderer(Loader loader)
	{
		GL11.glEnable(GL11.GL_CULL_FACE);//To stop rendering triangles on the other side
		GL11.glCullFace(GL11.GL_BACK);//To stop rendering faces away from view culling back face
		CreateProjectionMatrix();
		renderer = new EntityRenderer(shader,ProjectionMatrix);
		terrain_renderer = new TerrainRenderer(terrain_shader, ProjectionMatrix);
		skyboxRenderer = new SkyBoxRenderer(loader, ProjectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(ProjectionMatrix);
	}
	
	//Skybox Renderer here
	
	
	

	 public void render(List<Light> lights, Camera camera) {
	        Prepare();
	        shader.Start();
	       // shader.loadClipPlane(clipPlane);
	        shader.LoadSkyColor(RED, GREEN, BLUE);
	        shader.LoadLights(lights);
	        shader.LoadViewMatrix(camera);
	        renderer.Render(entities);
	        shader.Stop();
	        normalMapRenderer.render(NormalMapEntities, lights, camera);
	        terrain_shader.Start();
	        terrain_shader.LoadSkyColor(RED, GREEN, BLUE);
	        terrain_shader.LoadLights(lights);
	        terrain_shader.LoadViewMatrix(camera);
	        terrain_renderer.RenderTerrain(terrains);
	        terrain_shader.Stop();
	        skyboxRenderer.RenderCube(camera, RED, GREEN, BLUE);
	        terrains.clear();
	        entities.clear();
	        NormalMapEntities.clear();
	    }
	public void ProcessTerrain(Terrain terrain)
	{
		//Processing Terrains
		terrains.add(terrain);
	}
	public void Process(Entity entity)
	{
		//Processing entites into HashMap using sorting
		TexturedModel EntityModel = entity.getModel();
		List<Entity> batch = entities.get(EntityModel);
		if(batch != null)
		{
			//If Batch exists
			batch.add(entity);
		}
		else
		{
			//Creating batch
			List<Entity> NewBatch = new ArrayList<Entity>();
			NewBatch.add(entity);
			entities.put(EntityModel, NewBatch);
		}
		
	}
	
	
	public void renderScene(List<Entity> entities, List<Entity> normalEntities, List<Terrain> terrains, List<Light> lights,Camera camera) {
        for (Terrain terrain : terrains) 
        {
            ProcessTerrain(terrain);
        }
        for (Entity entity : entities)
        {
            Process(entity);
        }
        for(Entity entity : normalEntities)
        {
            ProcessNormalMap(entity);
        }
        render(lights, camera);
    }
 
	public void ProcessNormalMap(Entity entity)
	{
		//Processing entites into HashMap using sorting
		TexturedModel EntityModel = entity.getModel();
		List<Entity> batch = NormalMapEntities.get(EntityModel);
		if(batch != null)
		{
			//If Batch exists
			batch.add(entity);
		}
		else
		{
			//Creating batch
			List<Entity> NewBatch = new ArrayList<Entity>();
			NewBatch.add(entity);
			NormalMapEntities.put(EntityModel, NewBatch);
		}
		
	}
	public void CleanAll()
	{
		shader.CleanAll();
		normalMapRenderer.cleanUp();
		terrain_shader.CleanAll();
	}
	
	public void Prepare()
	{
		//Prepares OPENGL to render every frame
		GL11.glEnable(GL11.GL_DEPTH_TEST);//Testing while rotation
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT); //Clearing Depth Buffer every single frame
		GL11.glClearColor(RED, GREEN, BLUE, 1.0f); //Here is the sky color
	}
	
	private void CreateProjectionMatrix()
	{
		//Creating Projection Matrix
		float aspect_ratio = (float) Display.getWidth() / (float) Display.getHeight();
		float scale_y = (float) ((1f / Math.tan(Math.toRadians(Field_Of_View) / 2f))) * aspect_ratio;
		float scale_x = scale_y / aspect_ratio;
		float Frustrum_Length = Far_Plane - Near_Plane;
		
		ProjectionMatrix = new Matrix4f();
		ProjectionMatrix.m00 = scale_x;
		ProjectionMatrix.m11 = scale_y;
		ProjectionMatrix.m22 = -((Far_Plane + Near_Plane)/Frustrum_Length);
		ProjectionMatrix.m23 = -1;
		ProjectionMatrix.m32 = -((2 * Far_Plane * Near_Plane)/Frustrum_Length);
		ProjectionMatrix.m33 = 0;
		
	}
}
