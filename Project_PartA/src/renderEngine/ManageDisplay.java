package renderEngine;
//Class to Manage Display Setting
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.ContextAttribs;

public class ManageDisplay {
	
	private static long LastFrameTime;
	private static float omega; //Time taken to render
	
	
	public static void CreateDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3,2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(1280,900));
			Display.create(new PixelFormat(),attribs);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, 1280, 900);
		LastFrameTime = GetCurrTime();
	}
	public static void UpdateDisplay()
	{
		Display.sync(220);
		Display.update();
		long CurrFrameTime = GetCurrTime();
		omega = (CurrFrameTime - LastFrameTime) / 1000f; //Getting Time diff here in Seconds
		LastFrameTime = CurrFrameTime; //For next frame calculations
	}
	
	public static float GetTimeDiff()
	{
		return omega;//get Time Difference
	}
	public static void CloseDisplay()
	{
		Display.destroy();
	}
	
	private static long GetCurrTime()
	{
		//Getting current time
		return Sys.getTime() * 1000/ Sys.getTimerResolution(); //Multiply by 1000 for time in ms
	}
}
