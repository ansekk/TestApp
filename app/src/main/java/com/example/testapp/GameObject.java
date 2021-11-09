package com.example.testapp;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

public class GameObject {
    Vector<Float> pos;
    Vector<Vector<Float>> triangles;
    Vector<Float> rot;
    Vector<Float> forward;
    Vector<Float> up;
    Vector<Float> right;

    public GameObject(Vector<Vector<Float>> new_triangles) {
        triangles = new_triangles;
        pos = new Vector<Float>(Arrays.asList(0.0f, 0.0f, 0.0f));
        rot = new Vector<Float>(Arrays.asList(0.0f, 0.0f, 0.0f));
        forward = new Vector<Float>(Arrays.asList(1.0f, 0.0f, 0.0f));
        up = new Vector<Float>(Arrays.asList(0.0f, 1.0f, 0.0f));
        right = new Vector<Float>(Arrays.asList(0.0f, 0.0f, 1.0f));
    }
    public GameObject(Vector<Vector<Float>> new_triangles, float rotX, float rotY, float rotZ) {
        triangles = new_triangles;
        pos = new Vector<Float>(Arrays.asList(0.0f, 0.0f, 0.0f));
        rot = new Vector<Float>(Arrays.asList(rotX, rotY, rotZ));
    }
    public GameObject(Vector<Vector<Float>> new_triangles, Vector<Float> new_forward, Vector<Float> new_up) {
        triangles = new_triangles;
        pos = new Vector<Float>(Arrays.asList(0.0f, 0.0f, 0.0f));
        forward = new_forward;
        up = new_up;
    }
    public GameObject(Vector<Vector<Float>> new_triangles, Vector<Float> new_pos) {
        triangles = new_triangles;
        pos = new_pos;
        rot = new Vector<Float>(Arrays.asList(0.0f, 0.0f, 0.0f));
    }
    public GameObject(Vector<Vector<Float>> new_triangles, Vector<Float> new_pos, float rotX, float rotY, float rotZ) {
        triangles = new_triangles;
        pos = new_pos;
        rot = new Vector<Float>(Arrays.asList(rotX, rotY, rotZ));
    }
    public GameObject(Vector<Vector<Float>> new_triangles, Vector<Float> new_pos, Vector<Float> new_forward, Vector<Float> new_up) {
        triangles = new_triangles;
        pos = new_pos;
        forward = new_forward;
        up = new_up;
    }
}
