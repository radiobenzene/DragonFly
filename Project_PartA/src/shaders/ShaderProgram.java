package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.lwjgl.util.vector.*;
//Generic shader program to be used as a layout plan for specific shader program
public abstract class ShaderProgram {
	private int programID;
	private int VertexShaderID;
	private int FragmentShaderID;
	
	private static FloatBuffer MatrixBuffer = BufferUtils.createFloatBuffer(16); //MatrixBuffer to load matrices
	public ShaderProgram(String VertexFile, String FragmentFile)
	{
		
		VertexShaderID = LoadShader(VertexFile, GL20.GL_VERTEX_SHADER);
		FragmentShaderID = LoadShader(FragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, VertexShaderID);//Attaching vertex shader
		GL20.glAttachShader(programID, FragmentShaderID);//Attaching Fragment Shader
		BindAttributes(); //Can be removed
		GL20.glLinkProgram(programID);//Linking the program here
		GL20.glValidateProgram(programID);//Validating the program here
		GetAllUniformLocations();
	}
	
	protected abstract void GetAllUniformLocations();
	
	protected int GetUniformVarLocation(String Variable_Name)
	{
		//Function to get Location of Uniform Variable
		return GL20.glGetUniformLocation(programID, Variable_Name);
	}
	
	protected void LoadInt(int location, int value)
	{
		GL20.glUniform1i(location, value);
	}
	protected void LoadFloat(int location, float value)
	{
		//Loading a float variable into Uniform Location
		GL20.glUniform1f(location, value);
		
	}
	
	 protected void LoadVector(int location, Vector3f vector){
	        GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	    }
	     
	    protected void LoadVector(int location, Vector4f vector){
	        GL20.glUniform4f(location,vector.x,vector.y,vector.z, vector.w);
	    }
	     
	    protected void Load2DVector(int location, Vector2f vector){
	        GL20.glUniform2f(location,vector.x,vector.y);
	    }
	
	protected void LoadBoolean(int location, boolean bool_value)
	{
		//Loading boolean value to uniform location
		float value = 0;
		if(bool_value)
		{
			value = 1;
		}
		GL20.glUniform1f(location, value);
	}
	
	protected void LoadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(MatrixBuffer);
		MatrixBuffer.flip(); //To Read from buffer
		GL20.glUniformMatrix4(location, false, MatrixBuffer); //Do not want to transpose matrix so false
	}
	public void Start()
	{
		//Starting the program
		GL20.glUseProgram(programID);
	}
	
	public void Stop()
	{
		//Stopping the program here
		GL20.glUseProgram(0);
	}
	
	public void CleanAll()
	{
		Stop();
		GL20.glDetachShader(programID, VertexShaderID); //Detaching vertex shader
		GL20.glDetachShader(programID, FragmentShaderID); //Detaching fragment shader
		GL20.glDeleteShader(VertexShaderID); //Deleting vertex shader 
		GL20.glDeleteShader(FragmentShaderID); //Deleting Fragment shader
		GL20.glDeleteProgram(programID); //Deleting the program
	}
	
	protected abstract void BindAttributes();
	
	protected void BindAttributes(int attribute, String VariableName)
	{
		GL20.glBindAttribLocation(programID, attribute, VariableName);
	}
	
	@SuppressWarnings("deprecation")
	private static int LoadShader(String file, int type)
	{
		StringBuilder shaderSource = new StringBuilder();
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(Exception e){
			System.err.println("Could not open and read file");
			e.printStackTrace();
			System.exit(-1);
		}
		int ShaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(ShaderID, shaderSource);
		GL20.glCompileShader(ShaderID);
		if((GL20.glGetShader(ShaderID,  GL20.GL_COMPILE_STATUS)) == GL11.GL_FALSE)
		{
			System.out.println(GL20.glGetShaderInfoLog(ShaderID, 500));
			System.err.println("Could not compiler the given shader");
			System.exit(-1);
		}
		return ShaderID;
	}
}
