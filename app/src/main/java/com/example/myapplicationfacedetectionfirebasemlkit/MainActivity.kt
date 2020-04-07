package com.example.myapplicationfacedetectionfirebasemlkit

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationfacedetectionfirebasemlkit.helper.GraphicOverlay
import com.example.myapplicationfacedetectionfirebasemlkit.helper.RectOverlay
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.wonderkiln.camerakit.*


class MainActivity : AppCompatActivity() {

    private var button: Button? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var cameraView: CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cameraView = findViewById(R.id.camera_view)
        graphicOverlay = findViewById(R.id.graphic_overlay)
        button = findViewById(R.id.detect_face_btn)

        cameraView!!.facing = CameraKit.Constants.FACING_FRONT
        cameraView!!.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {}
            override fun onError(cameraKitError: CameraKitError) {}
            override fun onImage(cameraKitImage: CameraKitImage) {
                var bitmap: Bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView!!.width, cameraView!!.height, false)
                //cameraView!!.stop()

                processFaceDetection(rotateImage(bitmap, 180))
            }
            override fun onVideo(cameraKitVideo: CameraKitVideo) {}
        })
        startTimer()
        button!!.setOnClickListener(View.OnClickListener {
            cameraView!!.start()
            cameraView!!.captureImage()
            graphicOverlay!!.clear()
        })

    }
    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg =
            Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }
    private fun startTimer() {
        cameraView!!.start()
        cameraView!!.captureImage()
        graphicOverlay!!.clear()
        val timer = object: CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                startTimer()
            }
        }
        timer.start()
    }
    private fun processFaceDetection(bitmap: Bitmap?) {


        val firebaseVisionImage: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap!!)
        val firebaseVisionFaceDetectorOptions =
            FirebaseVisionFaceDetectorOptions.Builder()
                .build()
        val firebaseVisionFaceDetector: FirebaseVisionFaceDetector = FirebaseVision
            .getInstance().getVisionFaceDetector(firebaseVisionFaceDetectorOptions)

        firebaseVisionFaceDetector.detectInImage(firebaseVisionImage)
            .addOnSuccessListener { firebaseVisionFaces -> getFaceResults(firebaseVisionFaces) }
            .addOnFailureListener {
                // Log Error
            }
    }

    private fun getFaceResults(firebaseVisionFaces: List<FirebaseVisionFace>) {
        var counter = 0
        for (face in firebaseVisionFaces) {
            val rect = face.boundingBox

            graphicOverlay!!.clear()
            val rectOverlay = RectOverlay(graphicOverlay, rect)
            graphicOverlay!!.add(rectOverlay)
            counter++
        }
    }

    override fun onPause() {
        super.onPause()

        cameraView!!.stop()
    }

    override fun onResume() {
        cameraView!!.start()
        super.onResume()
    }

}
