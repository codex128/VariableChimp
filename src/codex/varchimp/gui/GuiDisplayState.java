/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.varchimp.gui;

import codex.varchimp.VarChimp;
import codex.varchimp.VarChimpAppState;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.core.GuiMaterial;
import com.simsilica.lemur.core.UnshadedMaterialAdapter;
import com.simsilica.lemur.event.BasePickState;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.input.InputMapper;
import java.awt.Point;

/**
 *
 * @author gary
 */
public class GuiDisplayState extends VarChimpAppState {
    
    public static final String
            VC_PICK_LAYER = "varchimp-pick-layer",
            VC_VIEW_NAME = "varchimp-view",
            BACKGROUND_NAME = "background-blocker";
    
    private ViewPort vp;
    private Node root;
    private Point windowSize;
    private GuiMaterial backmat;
    private int usage = 0;
    
    @Override
    protected void initialize(Application app) {
        
        windowSize = new Point(app.getContext().getSettings().getWidth(),
                app.getContext().getSettings().getHeight());
        
        root = new Node("varchimp[guiRoot]");
        root.setCullHint(Spatial.CullHint.Never);
        root.setQueueBucket(RenderQueue.Bucket.Gui);
        root.addControl(new CursorEventControl());
        root.addControl(new MouseEventControl());
        createViewPort();
        
        setBackgroundColor(new ColorRGBA(0f, 0f, 0f, .7f));
        
    }
    @Override
    protected void stateCleanup(Application app) {}
    @Override
    protected void stateEnabled() {}
    @Override
    protected void stateDisabled() {}
    @Override
    public void update(float tpf) {
        if (vp != null) {
            root.updateLogicalState(tpf);
            root.updateGeometricState();
        }
    }
    
    public void enableGui() {
        if (isInitialized() && usage++ == 0) {
            GuiGlobals.getInstance().requestFocus(root);
            InputMapper im = GuiGlobals.getInstance().getInputMapper();
            im.activateGroup(VarChimp.PASSIVE_INPUT);
            root.attachChild(createBackgroundBlocker(-10f, null));
            listeners.stream().forEach(l -> l.onGuiEnabled());
        }
    }
    public void releaseGui() {
        if (isInitialized() && --usage <= 0) {
            GuiGlobals.getInstance().releaseFocus(root);
            InputMapper im = GuiGlobals.getInstance().getInputMapper();
            im.deactivateGroup(VarChimp.PASSIVE_INPUT);
            root.detachChildNamed(BACKGROUND_NAME);
            usage = 0;
            listeners.stream().forEach(l -> l.onGuiDisabled());
        }
    }
    public int getNumGuiUsers() {
        return usage;
    }
    
    private void setViewPort(ViewPort vp) {
        assert vp != null;
        BasePickState pick = getState(BasePickState.class, true);
        if (this.vp != null) {
            pick.removeCollisionRoot(root);
            this.vp.detachScene(root);
            getApplication().getRenderManager().removePostView(this.vp);
        }
        this.vp = vp;
        this.vp.attachScene(root);
        pick.addCollisionRoot(root, this.vp, VC_PICK_LAYER);
        pick.setPickLayerOrder(VC_PICK_LAYER, BasePickState.PICK_LAYER_GUI, BasePickState.PICK_LAYER_SCENE);
    }
    public void createViewPort() {
        if (!isInitialized()) {
            return;
        }
        Camera cam = new Camera(windowSize.x, windowSize.y);
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(windowSize.x/2, windowSize.y/2, 100f));
        cam.lookAtDirection(new Vector3f(0f, 0f, -1f), Vector3f.UNIT_Y);
        ViewPort vp = getApplication().getRenderManager().createPostView(VC_VIEW_NAME, cam);
        vp.setClearFlags(false, false, false);
        setViewPort(vp);
    }
    
    public void setBackgroundColor(ColorRGBA color) {
        if (!isInitialized());
        else if (backmat != null) backmat.setColor(color);
        else createBackgroundMaterial(color);
    }
    private Geometry createBackgroundBlocker(float z, ColorRGBA color) {
        Geometry blocker = new Geometry(BACKGROUND_NAME, new Quad(windowSize.x, windowSize.y));        
        blocker.setMaterial(createBackgroundMaterial(color).getMaterial());
        blocker.setLocalTranslation(0f, 0f, z);
        return blocker;
    }
    private GuiMaterial createBackgroundMaterial(ColorRGBA color) {
        if (backmat != null) {
            if (color != null) {
                backmat.setColor(color);
            }
            return backmat;
        }
        backmat = new UnshadedMaterialAdapter(new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));
        backmat.setColor(color);
        backmat.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        backmat.getMaterial().setTransparent(true);
        backmat.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        return backmat;
    }
    
    public ViewPort getViewPort() {
        return vp;
    }
    public Node getGuiRoot() {
        return root;
    }
    public Point getWindowSize() {
        return windowSize;
    }
    
}
