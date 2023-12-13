package com.shyber.glass.sample.apidemo.opengl

import android.opengl.GLES20
import android.opengl.Matrix
import android.os.SystemClock
import com.google.android.glass.timeline.GlRenderer
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.egl.EGLConfig

/**
 * Renders a 3D OpenGL Cube on a [LiveCard].
 */
class CubeRenderer : GlRenderer {
    private val mMVPMatrix: FloatArray = FloatArray(16)
    private val mProjectionMatrix: FloatArray = FloatArray(16)
    private val mViewMatrix: FloatArray = FloatArray(16)
    private val mRotationMatrix: FloatArray = FloatArray(16)
    private val mFinalMVPMatrix: FloatArray = FloatArray(16)
    private var mCube: Cube? = null
    private var mCubeRotation = 0f
    private var mLastUpdateMillis: Long = 0
    override fun onSurfaceCreated(config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glClearDepthf(1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glDepthFunc(GLES20.GL_LEQUAL)
        mCube = Cube()
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        val ratio = width.toFloat() / height
        GLES20.glViewport(0, 0, width, height)
        // This projection matrix is applied to object coordinates in the onDrawFrame() method.
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f)
        // modelView = projection x view
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Apply the rotation.
        Matrix.setRotateM(mRotationMatrix, 0, mCubeRotation, 1.0f, 1.0f, 1.0f)
        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mFinalMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0)

        // Draw cube.
        mCube?.draw(mFinalMVPMatrix)
        updateCubeRotation()
    }

    /** Updates the cube rotation.  */
    private fun updateCubeRotation() {
        if (mLastUpdateMillis != 0L) {
            val factor =
                (SystemClock.elapsedRealtime() - mLastUpdateMillis) / FRAME_TIME_MILLIS
            mCubeRotation += CUBE_ROTATION_INCREMENT * factor
        }
        mLastUpdateMillis = SystemClock.elapsedRealtime()
    }

    companion object {
        /** Rotation increment per frame.  */
        private const val CUBE_ROTATION_INCREMENT = 0.6f

        /** The refresh rate, in frames per second.  */
        private const val REFRESH_RATE_FPS = 60

        /** The duration, in milliseconds, of one frame.  */
        private val FRAME_TIME_MILLIS =
            TimeUnit.SECONDS.toMillis(1) / REFRESH_RATE_FPS.toFloat()
    }

    init {

        // Set the fixed camera position (View matrix).
        Matrix.setLookAtM(
            mViewMatrix,
            0,
            0.0f,
            0.0f,
            -4.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            1.0f,
            0.0f
        )
    }
}