package AllEntities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import AllModels.TexturedModel;
import AllTerrains.Terrain;
import renderEngine.ManageDisplay;

//Everything for the player
public class Player extends Entity {

	
	private static final float RUN_SPEED = 10;
	private static final float TURN_SPEED = 20; //Degrees Per Second
	private static final float GRAVITY = -30;
	private static final float JUMP = 30;
	private static final float TERRAIN_HEIGHT = 0;
	private float Curr_Speed = 0; 
	private float Curr_TurnSpeed = 0;
	private float UpSpeed = 0;
	
	private boolean isJumping = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public void MovePlayer(Terrain terrain)
	{
		//For moving player
		CheckKeystroke();
		super.IncreaseRotation(0, Curr_TurnSpeed * ManageDisplay.GetTimeDiff(), 0);
		float dist = Curr_Speed * ManageDisplay.GetTimeDiff();
		float dx = (float) (dist * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (dist * Math.cos(Math.toRadians(super.getRotY())));
		super.IncreasePosition(dx, 0, dz); //Increasing the Player Position
		UpSpeed += GRAVITY * ManageDisplay.GetTimeDiff();
		super.IncreasePosition(0, UpSpeed * ManageDisplay.GetTimeDiff(), 0);
		float TerrainHeight = terrain.GetTerrainHeight(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < TerrainHeight)
		{
			//Stop Falling 
			UpSpeed = 0;
			isJumping = false;
			super.getPosition().y = TerrainHeight;
		}
	}
	
	private void JumpPlayer()
	{
		if(!isJumping)
		{
			this.UpSpeed = JUMP;
			isJumping = true;
		}
		
	}
	public void CheckKeystroke()
	{
		//Checking Inputs from keyboards
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			this.Curr_Speed -= RUN_SPEED;
			
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S) )
		{
			this.Curr_Speed += RUN_SPEED;
		}
		else
		{
			this.Curr_Speed = 0;
		}
		
		//For Turning Player
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			this.Curr_TurnSpeed += TURN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			this.Curr_TurnSpeed -= TURN_SPEED;
		}
		else
		{
			this.Curr_TurnSpeed = 0;
		}
		
		//For Jumping
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			JumpPlayer();
		}
	}
	
}
