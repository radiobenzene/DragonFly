package Textures;

//For the different types of terrains
public class TexturePackage {
		
	private TerrainTexture Snow_Texture;
	private TerrainTexture Mud_Texture; //Red Component
	
	
	
	public TerrainTexture getSnow_Texture() {
		return Snow_Texture;
	}



	public TerrainTexture getMud_Texture() {
		return Mud_Texture;
	}



	public TexturePackage(TerrainTexture snow_Texture, TerrainTexture mud_Texture) {
		Snow_Texture = snow_Texture;
		Mud_Texture = mud_Texture;
	}



	
	
	
	
	
}
