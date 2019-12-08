package AllModels;

import Textures.ModelTexture;

//Class that represents the textured model
public class TexturedModel {
	
	private RawModel rawmodel;
	private  ModelTexture texture;
	
	
	public TexturedModel(RawModel raw_model, ModelTexture model_texture)
	{
		this.rawmodel = raw_model;
		this.texture = model_texture;
	}


	public RawModel getRawmodel() {
		return rawmodel;
	}


	public   ModelTexture getTexture() {
		return texture;
	}
	/*
	public static int getTextureID()
	{
		return texture.GetID();
	}
	*/

	
}
