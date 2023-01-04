package com.ly.ocr.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.apache.pdfbox.jbig2.Bitmap;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class OcrService {

    private final String DATA_PATH = "/home/luiz/dev/Tess4J/tessdata/";
    private final int ENGINE_MODE = 2;
    private final int PAGE_MODE = 1;
    private final String LANG = "por";

    public String ocrRead(String image){

        Tesseract tesseract = new Tesseract();
        tesseract.setOcrEngineMode(ENGINE_MODE);
        tesseract.setPageSegMode(PAGE_MODE);
        tesseract.setLanguage(LANG);
        tesseract.setDatapath(DATA_PATH);



        byte[] imageByte = Base64.getDecoder().decode(image);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(bis);
            bis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    /*
        OpenCV.loadLocally();
        Imgcodecs imageCodecs = new Imgcodecs();
        Mat original = imageCodecs.imread("/home/luiz/Downloads/cupom2.png");
        Mat gray = new Mat(original.rows(), original.cols(), original.type());
        Mat blur = new Mat(original.rows(), original.cols(), original.type());
        Mat unSharp = new Mat(original.rows(), original.cols(), original.type());
        Mat binary = new Mat(original.rows(), original.cols(), original.type());
        Mat detectedEdges = new Mat(original.rows(), original.cols(), original.type());
        MatOfInt params = new MatOfInt(Imgcodecs.IMWRITE_PNG_COMPRESSION);

        Imgproc.cvtColor(original, gray, Imgproc.COLOR_RGB2GRAY, 0);
        Imgcodecs.imwrite("/home/luiz/Downloads/grey.png", gray, params);

        Imgproc.GaussianBlur(gray, blur, new Size(3, 3), 3);
        Imgcodecs.imwrite("/home/luiz/Downloads/blur.png", blur, params);

        Core.addWeighted(blur, 0.5, blur, 0.5, 0, unSharp);
        Imgcodecs.imwrite("/home/luiz/Downloads/unSharp.png", unSharp, params);

        Imgproc.threshold(unSharp,binary,127,255,Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite("/home/luiz/Downloads/binary.png", binary, params);

        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();
        int ddepth = CvType.CV_16S;
        // Gradient X
        Imgproc.Sobel(gray, grad_x, ddepth, 1, 0);
        Core.convertScaleAbs(grad_x, abs_grad_x);

        // Gradient Y
        Imgproc.Sobel(gray, grad_y, ddepth, 0, 1);
        Core.convertScaleAbs(grad_y, abs_grad_y);

        // Total Gradient (approximate)
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, detectedEdges);
        Imgcodecs.imwrite("/home/luiz/Downloads/detectedEdges.png", detectedEdges, params);

        Imgcodecs.imwrite("/home/luiz/Downloads/cupom3.png", gray, params);
        File file = new File("/home/luiz/Downloads/cupom3.png");
*/
        String text = null;
        try {
            text = tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
        return text;
    }
}
