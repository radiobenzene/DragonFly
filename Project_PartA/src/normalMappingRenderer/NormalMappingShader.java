package normalMappingRenderer;
 
import java.util.List;
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
 
import AllEntities.Light;
import shaders.ShaderProgram;
 
public class NormalMappingShader extends ShaderProgram{
     
    private static final int MAX_LIGHTS = 1;
     
    private static final String VERTEX_FILE = "src/normalMappingRenderer/normalMapVShader.txt";
    private static final String FRAGMENT_FILE = "src/normalMappingRenderer/normalMapFShader.txt";
     
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPositionEyeSpace[];
    private int location_lightColour[];
    private int location_attenuation[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;
    private int location_plane;
    private int location_modelTexture;
    private int location_normalMap;
 
    public NormalMappingShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void BindAttributes()
    {
    	//Vertex Shader in 
        super.BindAttributes(0, "position");
        super.BindAttributes(1, "textureCoordinates");
        super.BindAttributes(2, "normal");
        super.BindAttributes(3, "tangent");
    }
 
    @Override
    protected void GetAllUniformLocations() {
        location_transformationMatrix = super.GetUniformVarLocation("transformationMatrix");
        location_projectionMatrix = super.GetUniformVarLocation("projectionMatrix");
        location_viewMatrix = super.GetUniformVarLocation("viewMatrix");
        location_shineDamper = super.GetUniformVarLocation("shineDamper");
        location_reflectivity = super.GetUniformVarLocation("reflectivity");
        location_skyColour = super.GetUniformVarLocation("skyColour");
        location_numberOfRows = super.GetUniformVarLocation("numberOfRows");
        location_offset = super.GetUniformVarLocation("offset");
        location_plane = super.GetUniformVarLocation("plane");
        location_modelTexture = super.GetUniformVarLocation("modelTexture");
        location_normalMap = super.GetUniformVarLocation("normalMap");
        
        location_lightPositionEyeSpace = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for(int i=0;i<MAX_LIGHTS;i++){
            location_lightPositionEyeSpace[i] = super.GetUniformVarLocation("lightPositionEyeSpace[" + i + "]");
            location_lightColour[i] = super.GetUniformVarLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.GetUniformVarLocation("attenuation[" + i + "]");
        }
    }
     
    protected void connectTextureUnits()
    {
    	//For the sampler2D only
        super.LoadInt(location_modelTexture, 0); //Loading to Texture Unit 0
        super.LoadInt(location_normalMap, 1); //Loading to Texture Unit 1
    }
     
    protected void loadClipPlane(Vector4f plane){
        super.LoadVector(location_plane, plane);
    }
     
    
    protected void loadNumberOfRows(int numberOfRows){
        super.LoadFloat(location_numberOfRows, numberOfRows);
    }
     
    protected void loadOffset(float x, float y){
        super.Load2DVector(location_offset, new Vector2f(x,y));
    }
     
    protected void loadSkyColour(float r, float g, float b){
        super.LoadVector(location_skyColour, new Vector3f(r,g,b));
    }
     
    protected void loadShineVariables(float damper,float reflectivity){
        super.LoadFloat(location_shineDamper, damper);
        super.LoadFloat(location_reflectivity, reflectivity);
    }
     
    protected void loadTransformationMatrix(Matrix4f matrix){
        super.LoadMatrix(location_transformationMatrix, matrix);
    }
     
    protected void loadLights(List<Light> lights, Matrix4f viewMatrix){
        for(int i=0;i<MAX_LIGHTS;i++){
            if(i<lights.size()){
                super.LoadVector(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
                super.LoadVector(location_lightColour[i], lights.get(i).getColor());
                super.LoadVector(location_attenuation[i], lights.get(i).getAttenuation());
            }else{
                super.LoadVector(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
                super.LoadVector(location_lightColour[i], new Vector3f(0, 0, 0));
                super.LoadVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }
     
    protected void loadViewMatrix(Matrix4f viewMatrix){
        super.LoadMatrix(location_viewMatrix, viewMatrix);
    }
     
    protected void loadProjectionMatrix(Matrix4f projection){
        super.LoadMatrix(location_projectionMatrix, projection);
    }
     
    private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix){
        Vector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
        Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
        return new Vector3f(eyeSpacePos);
    }
     
     
 
}