/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.fctests;

import codex.fieldchimp.FieldChimpAppState;
import codex.fieldchimp.NumberVar;
import codex.fieldchimp.Pull;
import codex.fieldchimp.Push;
import codex.fieldchimp.gui.FloatContainer;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

/**
 *
 * @author gary
 */
public class TestGame extends SimpleApplication implements ActionListener {
    
    FieldChimpAppState fieldChimp;
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
        
        fieldChimp = new FieldChimpAppState();
        fieldChimp.registerAllFactories(new FloatContainer(null));
        stateManager.attach(fieldChimp);
        //fieldChimp.setEnabled(true);
        
        bird = new Geometry("bird", new Box(1f, 1f, 1f));
        bird.setMaterial(createMaterial(ColorRGBA.Blue));
        rootNode.attachChild(bird);
        
        inputManager.addMapping("jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("start-fieldchimp", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addListener(this, "jump", "start-fieldchimp");
        
        fieldChimp.register(new NumberVar(this, float.class, new Pull("getX"), new Push("setX")));
        fieldChimp.register(new NumberVar(this, float.class, new Pull("getJumpForce"), new Push("setJumpForce")));
        
    }
    @Override
    public void simpleUpdate(float tpf) {
        cam.lookAt(bird.getWorldTranslation(), Vector3f.UNIT_Y);
        if (!fieldChimp.isEnabled()) {
            velocity -= gravity;
            bird.move(0f, velocity, 0f);
        }
    }
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) return;
        if (name.equals("jump")) velocity = jumpForce;
        else if (name.equals("start-fieldchimp")) {
            fieldChimp.setEnabled(!fieldChimp.isEnabled());
        }
    }
    
    private Material createMaterial(ColorRGBA color) {
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", color);
        return m;
    }

    public float getX() {
        return bird.getLocalTranslation().x;
    }
    public void setX(float x) {
        Vector3f v = bird.getLocalTranslation();
        v.setX(x);
        bird.setLocalTranslation(v);
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
