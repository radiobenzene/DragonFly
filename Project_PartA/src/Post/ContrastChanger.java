package Post;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

//Post Processing effect to Change color
public class ContrastChanger {
	
	private ImageRenderer renderer;
	private ContrastShader shader;
	
	public ContrastChanger()
	{
		shader = new ContrastShader();
		renderer = new ImageRenderer();
		
	}
	
	public void Render(int texture)
	{
		shader.Start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);                  
		renderer.renderQuad();
		shader.Stop();
	}
	public void CleanAll()
	{
		renderer.cleanUp();
		shader.CleanAll();
	}
}
