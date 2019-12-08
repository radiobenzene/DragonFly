//Program Author - Uditangshu Aurangabadkar
//Term Project
//341/1 Group
//CMC MSU

package TestEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import AllEntities.Camera;
import AllEntities.Entity;
import AllEntities.Light;
import AllEntities.Player;
import AllModels.RawModel;
import AllModels.TexturedModel;
import AllTerrains.Terrain;
import Post.Fbo;
import Post.PostProcessing;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TexturePackage;
import normalMappingObjConverter.NormalMappedObjLoader;
//import postProcessing.Fbo;
//import postProcessing.PostProcessing;
import renderEngine.Loader;
import renderEngine.ManageDisplay;
import renderEngine.MasterRenderer;
import renderEngine.OBJ_Loader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;

public class MainGamePartB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ManageDisplay.CreateDisplay();
		Loader loader = new Loader();
		
		 
		
				 /***********************DRAGON INITIALIZATION****************************************************/
					 Loader Dragon_loader = new Loader();
					 RawModel Dragon_Model = OBJ_Loader.LoadOBJModel("dragon", Dragon_loader);
					 TexturedModel Dragon_textured_model = new TexturedModel(Dragon_Model, new ModelTexture(Dragon_loader.LoadTexture("mud")));
					 ModelTexture Dragon_Texture = Dragon_textured_model.getTexture();//For Specular Lighting
					 Dragon_Texture.setReflectivity(100);
					 Dragon_Texture.setShineDamper(100);
					 Entity dragon_entity = new Entity(Dragon_textured_model, new Vector3f(100,0,-100),0,0,0,8);
				  /***********************************************************************************************/
			 
				  /******************ALL TERRAINS ********************************/
			        TerrainTexture SnowTexture = new TerrainTexture(loader.LoadTexture("snow"));
			        TerrainTexture MudTexture = new TerrainTexture(loader.LoadTexture("mud"));
			        TerrainTexture BlendMap = new TerrainTexture(loader.LoadTexture("blend_map"));
			        TexturePackage texture_pack = new TexturePackage(SnowTexture, MudTexture);
			        //Creating Terrain
					// Terrain terrain = new Terrain(0,-1,loader,texture_pack, BlendMap, "heightmap"); //0 -1
					Terrain terrain2 = new Terrain(-1,-1,loader,texture_pack, BlendMap, "heightmap"); //-1 -1
					 
					 Terrain terrain = new Terrain(0, -1, loader, texture_pack, BlendMap, "heightmap");
				        List<Terrain> terrains = new ArrayList<Terrain>();
				        terrains.add(terrain);
				        //terrains.add(terrain2);
			         /***************************************************************/
		 
		 	/*************************************TREES**********************************************************/
			 TexturedModel Trees = new TexturedModel(OBJ_Loader.LoadOBJModel("christmas_tree", loader), new ModelTexture(loader.LoadTexture("blue"))); //Creating a textured_model here   
			 //Terrain terrain;
			 List<Entity> entities = new ArrayList<Entity>();
		     Random random = new Random();
		     
		     float x = random.nextFloat() * 800 - 400;
		     float z = random.nextFloat() * -600;
		     float y = terrain.GetTerrainHeight(x, z);
		     
		     for(int i=0;i<0;i++)
		      {
		        entities.add(new Entity(Trees, new Vector3f(random.nextFloat() * 800 - 400,y,random.nextFloat() * -600),0,random.nextFloat() * 360,0,10)); //3 is default	 
		      }
		      ModelTexture Tree_texture = Trees.getTexture();//For Specular Lighting
			  Tree_texture.setReflectivity(1);
			  Tree_texture.setShineDamper(100);
			/*************************************************************************************************/
			  
			  /***********************************NORMAL MAPPED OBJECTS****************************************/
			  List<Entity> normalMapEntities = new ArrayList<Entity>();
			  TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader), new ModelTexture(loader.LoadTexture("barrel_texture")));
			  barrelModel.getTexture().setNormal_map(loader.LoadTexture("barrel_normal"));
			  barrelModel.getTexture().setShineDamper(10);
			  barrelModel.getTexture().setReflectivity(0.5f);
			  normalMapEntities.add(new Entity(barrelModel, new Vector3f(75,20,-75),0,0,0,1f));
			  /**********************************************************************************************/
			  
			  /***************************POST PROCESSING EFFECTS*************************************/
			  Fbo fbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
	          PostProcessing.init(loader);
	          /**************************************************************************************/
	          
	          /*******************PLAYER INITIALIZATION**********************************************/
		        TexturedModel player_model = new TexturedModel(OBJ_Loader.LoadOBJModel("dragon", loader), new ModelTexture(loader.LoadTexture("microsoft_logo")));
		        Player player = new Player(player_model, new Vector3f(10,0,-10),0,5,5,1);
				Camera camera = new Camera(player);//Setting up a new camera
		        ModelTexture player_texture = player_model.getTexture();
		        player_texture.setShineDamper(100);   
		        player_texture.setReflectivity(100);
	        /**********************************************************************/

	         /********************************LIGHTING AND RENDERING***************************************/
				 Light LightSource = new Light(new Vector3f(10000,10000,10000), new Vector3f(1,1,1));
				 List<Light> lights = new ArrayList<Light>();
				 lights.add(LightSource);
				 MasterRenderer renderer = new MasterRenderer(loader);
			 /*********************************************************************************************/
			 
			 
			 /***********************************************MAIN GAME ***********************************************/
				while(!Display.isCloseRequested())
				{
					//game logic
					
					fbo.bindFrameBuffer();
					if(Keyboard.isKeyDown(Keyboard.KEY_B))
					{
						PostProcessing.doPostProcessing(fbo.getColourTexture());
					}
					fbo.unbindFrameBuffer();
					
					player.MovePlayer(terrain);
					player.MovePlayer(terrain2);
					camera.MoveCamera();
					
					renderer.ProcessTerrain(terrain);
					renderer.ProcessTerrain(terrain2);
					renderer.Process(player);
					
					renderer.renderScene(entities, normalMapEntities, terrains, lights, camera);
					//renderer.renderScene(normalMapEntities, normalEntities, terrains, lights, camera, clipPlane);
					//renderer.render(lights, camera);
					
					ManageDisplay.UpdateDisplay();
				}
			
			/***************************************************************************************************/
			/*****************************CLEAN_ALL*************************************************************/
			PostProcessing.cleanUp();
			fbo.cleanUp();
			renderer.CleanAll();
			loader.CleanAll();
			ManageDisplay.CloseDisplay();
			/***************************************************************************************************/
		}

}

