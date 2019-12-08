package AllEntities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

//Represents Camera in our 3D world
public class Camera {
	
	private float DistFromPlayer = 80; //Zoom
	private float angle = 0; //Angle around the player
	
	//height will be with Pitch
	private Vector3f camera_position = new Vector3f(210,10,21);
	private float pitch = 10; //Determines Height of camera
	private float yaw  ; //Determines left or right position of camera
	private float roll;//Determines tilt of camera
	
	private Player player;
	
	public Camera(Player player)
	{
		this.player = player;
	}
	public void MoveCamera()
	{
		CheckZoom();
		CheckPitch();
		CheckAngle();
		float Horizontal_Dist = HorizontalDist();
		float Vertical_Dist = VerticalDist();
		CalcCameraPos(Horizontal_Dist, Vertical_Dist);
		this.yaw = 180 - (player.getRotY() + angle);
	}
	public Vector3f getCamera_position() {
		return camera_position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
	
	private void CheckZoom()
	{
		//How much zoom based on mouse wheel
		float zoom = Mouse.getDWheel() * 0.2f;
		DistFromPlayer -= zoom;
	}
	
	private void CheckPitch()
	{
		//Calculating and recaliberating the pitch
		if(Mouse.isButtonDown(1))
		{
			float Pitch_Change = Mouse.getDY() * 0.2f; //Multiplying for sensitivity reduction
			pitch -= Pitch_Change;
		}
	}
	
	private void CheckAngle()
	{
		//calculating the angle from the player
		if(Mouse.isButtonDown(0))
		{
			float angle_shift = Mouse.getDX() * 0.4f;
			angle -= angle_shift;
		}
	}
	
	private float HorizontalDist()
	{
		return (float) (DistFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float VerticalDist()
	{
		return (float) (DistFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void CalcCameraPos(float  Horiz, float Vert)
	{
		float alpha = player.getRotY() + angle;
		float offsetX = (float) (Horiz * Math.sin(Math.toRadians(alpha)));
		float offsetZ = (float) (Horiz * Math.cos(Math.toRadians(alpha)));
		camera_position.x = player.getPosition().x - offsetX; //Actual Position
		camera_position.z = player.getPosition().z - offsetZ; //Actual Position 
		camera_position.y = player.getPosition().y + Vert;
	}
	
	
}
