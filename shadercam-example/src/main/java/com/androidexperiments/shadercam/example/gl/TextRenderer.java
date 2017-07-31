package com.androidexperiments.shadercam.example.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.android.texample2.domain.Font;
import com.android.texample2.programs.BatchTextProgram;
import com.android.texample2.programs.Program;
import com.androidexperiments.shadercam.gl.CameraRenderer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static com.android.texample2.domain.FontBuilder.createFont;
import static com.android.texample2.programs.FontProgramAdapter.createFontProgram;

/**
 * Example renderer that changes colors and tones of camera feed
 * based on touch position.
 */
public class TextRenderer extends CameraRenderer {
    private String text = "Hello, World!";
    private Font font;

    private int width = 100;                           // Updated to the Current Width + Height in onSurfaceChanged()
    private int height = 100;
    private float[] mProjMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mVPMatrix = new float[16];
    private Context context;

    /**
     * By not modifying anything, our default shaders will be used in the assets folder of shadercam.
     * <p>
     * Base all shaders off those, since there are some default uniforms/textures that will
     * be passed every time for the camera coordinates and texture coordinates
     */
    public TextRenderer(Context context, SurfaceTexture previewSurface, int width, int height) {
        super(context, previewSurface, width, height);

        //other setup if need be done here
        this.context = context;
    }

    @Override
    protected void onSetupComplete() {
        super.onSetupComplete();
        final String s = GLES20.glGetString(GLES20.GL_SHADING_LANGUAGE_VERSION);
        // Set the background frame color
//        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        Program program = BatchTextProgram.createBatchTextProgram();
        font = createFont().program(createFontProgram(program))
                .assets(context.getAssets())
                .font("Roboto-Regular.ttf")
                .size(60)
                .build();

        // enable texture + alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * we override {@link #setUniformsAndAttribs()} and make sure to call the super so we can add
     * our own uniforms to our shaders here. CameraRenderer handles the rest for us automatically
     */
    @Override
    protected void setUniformsAndAttribs() {
        super.setUniformsAndAttribs();

//        int offsetRLoc = GLES20.glGetUniformLocation(mCameraShaderProgram, "offsetR");
//        int offsetGLoc = GLES20.glGetUniformLocation(mCameraShaderProgram, "offsetG");
//        int offsetBLoc = GLES20.glGetUniformLocation(mCameraShaderProgram, "offsetB");
//
//        GLES20.glUniform1f(offsetRLoc, offsetR);
//        GLES20.glUniform1f(offsetGLoc, offsetG);
//        GLES20.glUniform1f(offsetBLoc, offsetB);
    }

    @Override
    public void draw() {
        super.draw();

//        glClear(GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

//        font.drawTexture(width, height, mVPMatrix);

//        font.begin(1.0f, 1.0f, 1.0f, 1.0f, mVPMatrix);
//        {
//            font.startDrawing("Test String 3D!").at(0f, 0f, 0f).centerXY().rotateY((float) -30).draw();
//            font.startDrawing("Diagonal 1").at(40.0f, 40.0f).rotateZ(40.0f).draw();
//            font.startDrawing("Column 1").at(100.0f, 100.0f).rotateZ(90.0f).draw();
//        }
//        font.end();
//
        font.begin(0.0f, 0.0f, 1.0f, 1.0f, mVPMatrix);
        {
            font.draw(System.currentTimeMillis() + "", 150, 0);
//            font.draw("More Lines...", 150, -font.getScaledCharHeight());
//            font.startDrawing("The End.").at(50.0f, 200.0f).rotateZ(180.0f).draw();
        }
        font.end();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void onSurfaceChanged(int width, int height) {
        glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        // Take into account device orientation
        if (width > height) {
            Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        } else {
            Matrix.frustumM(mProjMatrix, 0, -1, 1, -1 / ratio, 1 / ratio, 1, 10);
        }

        this.width = width;
        this.height = height;

        int useForOrtho = Math.min(width, height);

        //TODO: Is this wrong?
        Matrix.orthoM(mVMatrix, 0, -useForOrtho / 2, useForOrtho / 2, -useForOrtho / 2, useForOrtho / 2, 0.1f, 100f);
    }
}
