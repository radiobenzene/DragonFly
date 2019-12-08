package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import AllEntities.Camera;
import AllModels.RawModel;
import renderEngine.Loader;

public class SkyBoxRenderer
{
	private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILE = {"right", "left", "top", "bottom", "back", "front"};
	
	private RawModel cube;
	private int Texture_ID;
	private SkyBoxShader shader;
	
	public SkyBoxRenderer(Loader loader, Matrix4f ProjectionMatrix)
	{
		cube = loader.LoadToVAO(VERTICES, 3); //Loading the cube
		Texture_ID = loader.LoadCubeMap(TEXTURE_FILE);
		shader = new SkyBoxShader();
		shader.Start();
		shader.loadProjectionMatrix(ProjectionMatrix);
		shader.Stop();
	}
	
	public void RenderCube(Camera camera, float r, float g, float b)
	{
		shader.Start();
		shader.loadViewMatrix(camera);
		shader.LoadFogColor(r, g, b);
		GL30.glBindVertexArray(cube.getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, Texture_ID); //Binding cube map here
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());   
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.Stop();
	}
	
}

