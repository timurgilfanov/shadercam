package com.android.texample2.programs;

import android.opengl.GLES20;
import android.util.Log;

import com.android.texample2.AttributeVariable;


public class Program {

    private static final String TAG = "Program";

    private int programHandle;

    public Program(String vertexShaderCode, String fragmentShaderCode, AttributeVariable[] programVariables) {
        int vertexShaderHandle = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShaderHandle = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        programHandle = createProgram(vertexShaderHandle, fragmentShaderHandle, programVariables);
    }

    public int getHandle() {
        return programHandle;
    }

    private static int createProgram(int vertexShaderHandle, int fragmentShaderHandle, AttributeVariable[] variables) {
        int mProgram = GLES20.glCreateProgram();

        if (mProgram != 0) {
            GLES20.glAttachShader(mProgram, vertexShaderHandle);
            GLES20.glAttachShader(mProgram, fragmentShaderHandle);

            for (int i = 0; i < variables.length; i++) {
                GLES20.glBindAttribLocation(mProgram, i, variables[i].getName());
            }

            GLES20.glLinkProgram(mProgram);

            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus, 0);

            if (linkStatus[0] == 0) {
                Log.v(TAG, GLES20.glGetProgramInfoLog(mProgram));
                GLES20.glDeleteProgram(mProgram);
                mProgram = 0;
            }
        }

        if (mProgram == 0) {
            throw new RuntimeException("Error creating program.");
        }
        return mProgram;
    }

    private static int loadShader(int type, String shaderCode) {
        int shaderHandle = GLES20.glCreateShader(type);

        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderCode);
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.v(TAG, "Shader fail info: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }


        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader " + type);
        }
        return shaderHandle;
    }
}