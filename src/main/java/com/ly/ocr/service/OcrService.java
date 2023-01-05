package com.ly.ocr.service;

import com.ly.ocr.model.OcrEntity;
import com.ly.ocr.repository.OcrRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class OcrService implements IOcrService {

    private final String DATA_PATH = "/home/luiz/dev/Tess4J/tessdata/";
    private final int ENGINE_MODE = 2;
    private final int PAGE_MODE = 1;
    private final String LANG = "por";

    @Autowired
    private OcrRepository ocrRepository;

    public String getOcrText(UUID id){

        return ocrRepository.findById(id).get().getText();

    }

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


        OpenCV.loadLocally();

        Mat original = img2Mat(bufferedImage);

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

        bufferedImage = mat2Img(gray);

        String text = null;

        try {
            text = tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }

        OcrEntity ocrEntity = new OcrEntity();
        ocrEntity.setText(text);
        ocrEntity = ocrRepository.save(ocrEntity);

        return ocrEntity.getId().toString();
    }

    private BufferedImage mat2Img(Mat m) {
        if (!m.empty()) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);
            return image;
        }

        return null;
    }

    public static Mat img2Mat(BufferedImage in)
    {
        Mat out;
        byte[] data;
        int r, g, b;

        out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
        data = new byte[in.getWidth() * in.getHeight() * (int)out.elemSize()];
        int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
        for(int i = 0; i < dataBuff.length; i++)
        {
            data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
            data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
            data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
        }

        out.put(0, 0, data);
        return out;
    }


}
