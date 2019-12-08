package normalMappingRenderer;
 
import java.util.List;
import java.util.Map;
 
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
 
import AllEntities.Camera;
import AllEntities.Entity;
import AllEntities.Light;
import AllModels.RawModel;
import AllModels.TexturedModel;
import renderEngine.MasterRenderer;
import Textures.ModelTexture;
import Tools.*;
 
public class NormalMappingRenderer {
 
    private NormalMappingShader shader;
 
    public NormalMappingRenderer(Matrix4f projectionMatrix) {
        this.shader = new NormalMappingShader();
        shader.Start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.Stop();
    }
 
    public void render(Map<TexturedModel, List<Entity>> entities, List<Light> lights, Camera camera) {
        shader.Start();
        prepare(lights, camera);
        for (TexturedModel model : entities.keySet())
        {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) 
            {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawmodel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
        shader.Stop();
    }
     
    public void cleanUp(){
        shader.CleanAll();
    }
 
    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawmodel();
        GL30.glBindVertexArray(rawModel.getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        ModelTexture texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if (texture.isTransparent()) {
            MasterRenderer.DisableCulling();
        }
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().GetID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1); //For the NormalMap at texture unit 1
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getNormal_map());
    }
 
    private void unbindTexturedModel() {
        MasterRenderer.EnableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }
 
    private void prepareInstance(Entity entity) 
    {
        Matrix4f transformationMatrix = Mathematics.Create_TMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }
 
    private void prepare(List<Light> lights, Camera camera)
    {
        
        shader.loadSkyColour(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE);
        Matrix4f viewMatrix = Mathematics.CreateViewMatrix(camera);
         
        shader.loadLights(lights, viewMatrix);
        shader.loadViewMatrix(viewMatrix);
    }
 
}