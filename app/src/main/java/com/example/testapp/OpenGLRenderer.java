package com.example.testapp;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;

public class OpenGLRenderer implements Renderer {
    private Context context;
    private int programId;
    private FloatBuffer vertexData;
    private int aPositionLocation;
    private int uColorLocation;
    private int uMatrixLocation;
    private long time;
    private float[] mProjectionMatrix = new float[16];;
    private float[] mViewMatrix = new float[16];
    private float[] mMatrix = new float[16];

    public OpenGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glEnable(GL_DEPTH_TEST);
        glClearColor(0f, 0f, 0f, 1f);

        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        createViewMatrix();
        bindMatrix();
    }

    private void prepareData(float t) {
        float d_halved = (float) Math.sqrt(2);
        float[] vertices = {(float) (d_halved * Math.cos(t * 0.0002f)), -3, (float) (d_halved * Math.sin(t * 0.0002f)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI / 2)), -3, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI / 2)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI * 3 / 2)), -3, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI * 3 / 2)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI)), -3, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI)), -1, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI / 2)), -3, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI / 2)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI / 2)), -1, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI / 2)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f)), -3, (float) (d_halved * Math.sin(t * 0.0002f)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f)), -1, (float) (d_halved * Math.sin(t * 0.0002f)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI * 3 / 2)), -3, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI * 3 / 2)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI * 3 / 2)), -1, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI * 3 / 2)) - 4,
                (float) (d_halved * Math.cos(t * 0.0002f + Math.PI)), -1, (float) (d_halved * Math.sin(t * 0.0002f + Math.PI)) - 4};
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }

    private void bindData() {
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
        uColorLocation = glGetUniformLocation(programId, "u_Color");
        // координаты
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 12, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 15;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 10;
        float eyeZ = 2;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = -1;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        createViewMatrix();
        bindMatrix();

        prepareData(time);
        bindData();

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 12);

        time = SystemClock.uptimeMillis();
        if (time >= 63000) {time -= 63000;}
    }


}