
package Post;
 
import shaders.ShaderProgram;
 
public class ContrastShader extends ShaderProgram {
 
    private static final String VERTEX_FILE = "src/Post/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/Post/FragmentShader.txt";
     
    public ContrastShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
 
    @Override
    protected void GetAllUniformLocations() {   
    }
 
    @Override
    protected void BindAttributes() {
        super.BindAttributes(0, "position");
    }
 
}