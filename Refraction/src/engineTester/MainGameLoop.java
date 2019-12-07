//Program Author - Aurangabadkar Uditangshu
//Term Project
//Group 341/1 CMC MSU

package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Camera camera = new Camera();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer(loader);
		List<Entity> entities = new ArrayList<Entity>();

		//Loading a dragon entity to entity list
		entities.add(new Entity(loadModel("dragon", loader), new Vector3f(4, 2, 0), 0, 0.4f));

		while(!Display.isCloseRequested())
		{
			//For continuos Movement of screen, please press D and A
			//Game Logic 
			camera.move();
			renderer.renderScene(entities, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	private static TexturedModel loadModel(String fileName, Loader loader)
	{
		//Simple Loader Function 
		RawModel model = OBJFileLoader.loadOBJ(fileName, loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture(fileName));
		return new TexturedModel(model, texture);
	}


}
