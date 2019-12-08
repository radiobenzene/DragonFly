package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import AllEntities.Camera;
import AllEntities.Light;
import Tools.Mathematics;

//Class for Terrain Shaders
public class TerrainShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/shaders/TerrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/TerrainFragmentShader.txt";
	private static final int MAX_LIGHTS = 1;
	
	//For Uniform variables
	private int location_TMatrix; //Location of the transformation matrix
	private int location_ProjectionMatrix;//Location of the Projection matrix
	private int location_ViewMatrix; //Location of the view matrix
	private int location_LightPosition[]; //Location of the Light Source
	private int location_LightColor[]; //Location of the Light color
	private int location_ShineDamper;
	private int location_reflectivity;
	private int location_sky_color;
	private int location_SnowTexture;
	private int location_MudTexture;
	private int location_BlendMap;
	
	
	public TerrainShader()
	{
		super(VERTEX_FILE,FRAGMENT_FILE);
	}
	protected void BindAttributes()
	{
		super.BindAttributes(0, "position");//Vertices
		super.BindAttributes(1, "textureCoords");
		super.BindAttributes(2, "normal");
	}
	@Override
	protected void GetAllUniformLocations() {
		
		location_TMatrix = super.GetUniformVarLocation("TransformationMatrix");
		location_ProjectionMatrix = super.GetUniformVarLocation("ProjectionMatrix");
		location_ViewMatrix = super.GetUniformVarLocation("ViewMatrix");
		location_ShineDamper = super.GetUniformVarLocation("ShineDamper");
		location_reflectivity = super.GetUniformVarLocation("reflectivity");
		location_sky_color = super.GetUniformVarLocation("sky");
		location_SnowTexture = super.GetUniformVarLocation("SnowTexture");
		location_MudTexture = super.GetUniformVarLocation("RedTexture");
		location_BlendMap = super.GetUniformVarLocation("BlendMap");
		location_LightPosition = new int[MAX_LIGHTS];
		location_LightColor = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++)
		{
			location_LightPosition[i] = super.GetUniformVarLocation("LightPosition[" + i + "]");   
			location_LightColor[i] = super.GetUniformVarLocation("LightColor[" + i + "]"); 
		}
	}
	
	
	public void LoadAllTextures()
	{
		super.LoadInt(location_SnowTexture, 0);
		super.LoadInt(location_MudTexture, 1);
		super.LoadInt(location_BlendMap, 2);
	}
	public void LoadSkyColor(float r, float g, float b)
	{
		super.LoadVector(location_sky_color, new Vector3f(r, g, b));
	}
	public void LoadShineVariables(float damper, float reflectivity)
	{
		//For Specular Lighting
		super.LoadFloat(location_ShineDamper, damper);
		super.LoadFloat(location_reflectivity,reflectivity);
	}
	
	public void LoadTransformationMatrix(Matrix4f matrix)
	{
		super.LoadMatrix(location_TMatrix, matrix);
	}
	
	public void LoadLights(List<Light> lights)
	{
		for(int i=0;i<MAX_LIGHTS;i++)
        {
            if(i<lights.size())
            {
                super.LoadVector(location_LightPosition[i], lights.get(i).getPosition());
                super.LoadVector(location_LightColor[i], lights.get(i).getColor());
               // super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
            }else
            {
                super.LoadVector(location_LightPosition[i], new Vector3f(0, 0, 0));
                super.LoadVector(location_LightColor[i], new Vector3f(0, 0, 0));
                //super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
	}
	public void LoadProjectionMatrix(Matrix4f matrix)
	{
		super.LoadMatrix(location_ProjectionMatrix, matrix);
	}
	
	public void LoadViewMatrix(Camera camera)
	{
		Matrix4f ViewMatrix = Mathematics.CreateViewMatrix(camera);
		super.LoadMatrix(location_ViewMatrix, ViewMatrix);
	}
}
