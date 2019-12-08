
package skybox;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import AllEntities.Camera;
 
import shaders.ShaderProgram;
import Tools.Mathematics;
import renderEngine.ManageDisplay;
 
public class SkyBoxShader extends ShaderProgram{
 
   

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
     
    private static final float ROTATE_SPEED = 3f;
    private float rotation = 0;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_FogColor;
    
     
    public SkyBoxShader() 
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    public void loadProjectionMatrix(Matrix4f matrix){
        super.LoadMatrix(location_projectionMatrix, matrix);
    }
 
    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Mathematics.CreateViewMatrix(camera);
        
        //For the skybox interpolation
        //Skybox should move with camera
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += ROTATE_SPEED * ManageDisplay.GetTimeDiff();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);    
        super.LoadMatrix(location_viewMatrix, matrix);
    }
     
    @Override
    protected void GetAllUniformLocations() {
        location_projectionMatrix = super.GetUniformVarLocation("projectionMatrix");
        location_viewMatrix = super.GetUniformVarLocation("viewMatrix");
        location_FogColor = super.GetUniformVarLocation("FogColor");
    }
 
    @Override
    protected void BindAttributes() {
        super.BindAttributes(0, "position");
    }
    
    public void LoadFogColor(float r, float g, float b)
    {
    	super.LoadVector(location_FogColor, new Vector3f(r,g,b));
    }
 
}
