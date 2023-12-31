/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.vctests;

import codex.varchimp.Pull;
import codex.varchimp.Push;
import codex.varchimp.VarChimp;
import codex.varchimp.Var;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

/**
 *
 * @author gary
 */
public class TestGame extends SimpleApplication implements ActionListener {
    
    Spatial bird;
    float velocity = 0f;
    float jumpForce = 1f;
    float gravity = .02f;
    
    public static void main(String[] args) {
        new TestGame().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");
        
        VarChimp.initialize(this);
        
        bird = new Geometry("bird", new Box(1f, 1f, 1f));
        bird.setMaterial(createMaterial(ColorRGBA.Blue));
        rootNode.attachChild(bird);
        
        inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("start-varchimp", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(this, "jump", "start-varchimp");
        
        Node myNode = new Node();
        Var v = new Var(myNode, Vector3f.class, new Pull("getLocalTranslation"), new Push("setLocalTranslation"));
        VarChimp.get().register(v);
        
        //VarChimp.get().register(new Var(this, float.class, new Pull("getJumpForce"), new Push("setJumpForce")));
        
    }
    @Override
    public void simpleUpdate(float tpf) {
        cam.lookAt(bird.getWorldTranslation(), Vector3f.UNIT_Y);
        if (!VarChimp.get().isEnabled()) {
            velocity -= gravity;
            bird.move(0f, velocity, 0f);
        }
    }
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) return;
        if (name.equals("jump")) velocity = jumpForce;
        else if (name.equals("start-varchimp")) {
            VarChimp.get().setEnabled(!VarChimp.get().isEnabled());
        }
    }
    
    private Material createMaterial(ColorRGBA color) {
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", color);
        return m;
    }

    public Vector3f getPosition() {
        return bird.getLocalTranslation();
    }
    public void setPosition(Vector3f vec) {
        bird.setLocalTranslation(vec);
    }
    public float getVelocity() {
        return velocity;
    }
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
    public float getJumpForce() {
        return jumpForce;
    }
    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }
    public float getGravity() {
        return gravity;
    }
    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    
}
