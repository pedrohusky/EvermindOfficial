package com.example.evermemo.EverAudioVisualizerHandlers;

import android.opengl.GLES20;

import androidx.annotation.NonNull;

/**
 * Abstract shape implementation.
 */
abstract class EverGLShape {

    protected static final String VERTEX_POSITION = "vPosition";
    protected static final String VERTEX_COLOR = "vColor";
    protected static final int COORDS_PER_VERTEX = 3;
    protected static final int SIZE_OF_FLOAT = 4;
    protected static final int SIZE_OF_SHORT = 2;
    private static final String VERTEX_SHADER_CODE =
            "attribute vec4 " + VERTEX_POSITION + ";" +
                    "void main() {" +
                    "  gl_Position = " + VERTEX_POSITION + ";" +
                    "}";
    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 " + VERTEX_COLOR + ";" +
                    "void main() {" +
                    "  gl_FragColor = " + VERTEX_COLOR + ";" +
                    "}";
    /**
     * Shape color.
     */
    private final float[] color;

    /**
     * Program associated with shape.
     */
    private final int program;

    public EverGLShape(float[] color) {
        this.color = color;
        int vertexShader = EverGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
        int fragmentShader = EverGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    protected float[] getColor() {
        return color;
    }

    public void setColor(@NonNull float[] color) {
        System.arraycopy(color, 0, this.color, 0, this.color.length);
    }

    protected int getProgram() {
        return program;
    }
}
