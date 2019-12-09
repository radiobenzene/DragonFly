package entityRenderer;

import org.lwjgl.util.vector.Matrix4f;

import toolbox.Maths;

import entities.Camera;
import shaders.ShaderProgram;

public class StaticShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "/entityRenderer/vertexShader.txt"; //Path to Vertex Shader
	private static final String FRAGMENT_FILE = "/entityRenderer/fragmentShader.txt"; //Path to Fragment Shader
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_modelTexture;
	private int location_cameraPosition;
	private int location_enviroMap;

	public StaticShader() 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() 
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() 
	{
		//Getting all locations from shader
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_modelTexture = super.getUniformLocation("modelTexture");
		this.location_cameraPosition = super.getUniformLocation("cameraPosition");
		location_enviroMap = super.getUniformLocation("enviroMap");
		
	}
	
	protected void connectTextureUnits()
	{
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_enviroMap, 1);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}
	
	public void loadProjectionMatrix(Matrix4f projection)
	{
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	

}
