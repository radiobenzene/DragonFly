package Tools;

import java.util.Vector;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import AllEntities.Camera;

import java.util.*;
//Class for useful mathematical functions
public class Mathematics {
	public static Matrix4f Create_TMatrix(Vector3f translation, float rx, float ry, float rz, float scale)
	{
		//Creating Transpose Matrix here
		Matrix4f matrix = new Matrix4f();//creating new matrix here
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		//Rotating the matrix
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale),matrix,matrix);
		return matrix;
	}
	
	public static Matrix4f CreateViewMatrix(Camera camera)
	{
		//Creating ViewMatrix here
		Matrix4f ViewMatrix = new Matrix4f();
		ViewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), ViewMatrix, ViewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), ViewMatrix, ViewMatrix);
		Vector3f Camera_Position = camera.getCamera_position();
		Vector3f Negative_Camera_Position = new Vector3f(-Camera_Position.x, -Camera_Position.y, -Camera_Position.z);
		Matrix4f.translate(Negative_Camera_Position,ViewMatrix,ViewMatrix);
		return ViewMatrix;
	}
	
	
	//Using BarryCentric for TerrainCollision

	
}

  