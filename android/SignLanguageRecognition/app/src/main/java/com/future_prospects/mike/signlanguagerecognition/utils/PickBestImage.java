package com.future_prospects.mike.signlanguagerecognition.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class PickBestImage {
    private static final String TAG = PickBestImage.class.toString();

    public static byte[] pickImage(List<byte[]> list) {
        int max = Integer.MIN_VALUE;
        if (list.size() != 0) {
            byte[] maxPath = list.get(0);
            for (byte[] array : list) {
                if (opencvProcess(array)) {
                    return array;
                }
            }
            return maxPath;
        }
        return new byte[0];
    }

    private static boolean opencvProcess(byte[] array) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap image;
        image = BitmapFactory.decodeByteArray(array, 0, array.length);
        int l = CvType.CV_8UC1; //8-bit grey scale image
        Mat matImage = new Mat();
        Utils.bitmapToMat(image, matImage);
        Mat matImageGrey = new Mat();
        Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);

        Bitmap destImage;
        destImage = Bitmap.createBitmap(image);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        destImage.compress(Bitmap.CompressFormat.JPEG, 0, out);
        destImage = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        destImage = Bitmap.createScaledBitmap(destImage, 480, 240, false);
        Log.d(TAG, "size "+destImage.getWidth()+" "+destImage.getHeight());
        Mat dst2 = new Mat();
        Utils.bitmapToMat(destImage, dst2);
        Mat laplacianImage = new Mat();
        dst2.convertTo(laplacianImage, l);
        Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);
        Mat laplacianImage8bit = new Mat();
        laplacianImage.convertTo(laplacianImage8bit, l);

        Bitmap bmp = Bitmap.createBitmap(laplacianImage8bit.cols(), laplacianImage8bit.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(laplacianImage8bit, bmp);
        int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int maxLap = -16777216; // 16m
        for (int pixel : pixels) {
            if (pixel > maxLap) {
                maxLap = pixel;
            }
        }

        int soglia = -6118750;
        if (maxLap <= soglia) {
            System.out.println("is blur image");
        }
        soglia += 6118750;
        maxLap += 6118750;
        return maxLap <= soglia;
    }

}
