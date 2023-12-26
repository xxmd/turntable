package io.github.xxmd;

public interface TurntableListener {
    void onRotateStart(float preRotateAngle);
    void onRotating(float rotateAngle);
    void onRotateEnd(float hasRotateAngle);
}
