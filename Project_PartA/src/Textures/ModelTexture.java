package Textures;
//Class to place Texture on the model
public class ModelTexture {
	//Shine damper is for Specular Lighting
	private int TextureID;
	private float ShineDamper = 1;
	private float reflectivity = 0;
	private boolean isTransparent = false; //For transparency only
	private boolean FakeLighting = false;
	 private int numberOfRows = 1;
	 
	 private int normal_map; //Each texture can have a normal map
	 
	public int getNumberOfRows() {
	    return numberOfRows;
	}
	public void setShineDamper(float shineDamper) {
		ShineDamper = shineDamper;
	}

	public int getNormal_map() 
	{
		return normal_map;
	}
	public void setNormal_map(int normal_map) 
	{
		this.normal_map = normal_map;
	}
	public float getReflectivity() {
		return reflectivity;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	public boolean isFakeLighting() {
		return FakeLighting;
	}

	public void setFakeLighting(boolean fakeLighting) {
		FakeLighting = fakeLighting;
	}

	public void setTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public ModelTexture(int ID)
	{
		this.TextureID = ID;//Initializing Texture ID in constructor
	}

	public int GetID()
	{
		return this.TextureID; //Getting ID of Texture
	}

	public float getShineDamper() {
		return ShineDamper;
	}

	public ModelTexture getTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	

	

	
}
