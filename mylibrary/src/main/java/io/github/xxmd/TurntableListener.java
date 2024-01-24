package io.github.xxmd;

public interface TurntableListener {
    void onRotateStart(float preRotateAngle);
    void onRotating(float rotateAngle, float slope);
    void onRotateEnd(float hasRotateAngle);
}
