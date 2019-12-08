package AllModels;
//Class represents a 3D model stored in memory
public class RawModel {
	private int vaoID; //VAO is the vao ID
	private int VertexCount;
	public RawModel(int vao, int vertexcount)
	{
		this.vaoID = vao;
		this.VertexCount = vertexcount;
	}
	public int getVAO() 
	{
		//Getters for VAO
		return vaoID;
	}
	public int getVertexCount() 
	{
		//Getter for VertexCount
		return VertexCount;
	}
	
}
