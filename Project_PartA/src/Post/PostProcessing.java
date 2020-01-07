package Post;
 
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
 
import AllModels.RawModel;
import renderEngine.Loader;
 
public class PostProcessing {
     
    private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 }; //Positions for the post-processing effect 
    private static RawModel quad;
    private static ContrastChanger contrast_changer; //For changing the contrast as a post processing effects
    
    
    
 
    public static void init(Loader loader){
        quad = loader.LoadToVAO(POSITIONS, 2);
        contrast_changer = new ContrastChanger();
    }   
     
    public static void doPostProcessing(int colorTexture)
    {
     //B key down does not work here
    	//if(Keyboard.isKeyDown(Keyboard.KEY_B))
    	{
    		//If B key is down then do Post Processing
    		start();
            contrast_changer.Render(colorTexture); //To Change Contrast as a Post-Processing effect
            
            end();
    	}
        
    }
     
    public static void cleanUp()
    {
     //Cleaning All here
    	contrast_changer.CleanAll();
    }
     
    private static void start(){
        GL30.glBindVertexArray(quad.getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
     
    private static void end(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
 
 
}
