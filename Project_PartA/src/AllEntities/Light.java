package AllEntities;

import org.lwjgl.util.vector.Vector3f;

//Class for Lighting here
public class Light {

	private Vector3f position; //Position of the Light Source
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	public Light(Vector3f position, Vector3f color) {
		super();
		this.position = position;
		this.color = color;
	}
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }
	
	public Vector3f getPosition() {
		return position;
	}
	public Vector3f getAttenuation(){
        return attenuation;
    }
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public Vector3f getColor() {
		return color;
	}
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	//Per-Pixel lighting will be done in the Shader
}
