package com.example.tescik;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.MalformedURLException;


public class ButtonController {
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    @FXML
    public ImageView ImageOutput;
    public ImageView ImageInput;
    public ImageView ImageInput2;
    public LineChart Hist1;
    public javafx.scene.control.TextField BinValue1;


    public void ImgInputButtonClick() throws MalformedURLException {
        FileChooser ImgChooser = new FileChooser();
        ImgChooser.setTitle("Wybierz obraz");
        ImgChooser.getExtensionFilters().addAll
                (
                        new FileChooser.ExtensionFilter("Obrazy", "*.png", "*.jpg", "*.gif", "*.jpeg"),
                        new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")

                );
        File file = ImgChooser.showOpenDialog(Global.stage);
        if (file != null)
        {
            Image image = new Image(file.toURI().toURL().toString());
            ImageInput.setImage(image);

        }
    }

    public void ImgInputButtonClick2() throws MalformedURLException{
        FileChooser ImgChooser = new FileChooser();
        ImgChooser.setTitle("Wybierz obraz");
        ImgChooser.getExtensionFilters().addAll
                (
                        new FileChooser.ExtensionFilter("Obrazy", "*.png", "*.jpg", "*.gif", "*.jpeg"),
                        new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")

                );
        File file = ImgChooser.showOpenDialog(Global.stage);
        if (file != null)
        {
            Image image = new Image(file.toURI().toURL().toString());
            ImageInput2.setImage(image);

        }
    }
    @FXML
    protected void onHSVButtonClick()
    {
        Mat colorImage = Functions.imageToMat(ImageInput.getImage());
        Mat grayImage = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_8UC1);

        for (int y = 0; y < colorImage.rows(); y++) {
            for (int x = 0; x < colorImage.cols(); x++) {
                double[] rgb = colorImage.get(y, x);
                double grayValue = (rgb[0] + rgb[1] + rgb[2]) / 3;
                grayImage.put(y, x, grayValue);
            }
        }
        

        String outputFilePath = "/Users/szymon/Library/Mobile Documents/com~apple~CloudDocs/Nauka/Java/tescik/src/main/resources/ConImg.jpeg";
        Imgcodecs.imwrite(outputFilePath, grayImage);

        String imagePath = outputFilePath;

        File imageFile = new File(imagePath);

        // Tworzenie obiektu Image z załadowanego obrazu
        Image image = new Image(imageFile.toURI().toString());

        // Ustawienie obrazu w ImageView
        ImageOutput.setImage(image);

        Mat histogram = new Mat();
        Imgproc.calcHist(
                java.util.Collections.singletonList(grayImage),
                new MatOfInt(0),
                new Mat(),
                histogram,
                new MatOfInt(256),
                new MatOfFloat(0, 256)
        );

        Core.normalize(histogram, histogram, 0, 255, Core.NORM_MINMAX);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < histogram.rows(); i++) {
            double binValue = histogram.get(i, 0)[0];
            series.getData().add(new XYChart.Data<>(i, binValue));
        }

        Hist1.getData().add(series);


    }


    public void onYUVButtonClick(ActionEvent actionEvent)
    {
        Mat colorImage = Functions.imageToMat(ImageInput.getImage());

        Mat grayImage = new Mat(colorImage.rows(), colorImage.cols(), CvType.CV_8UC1);
        // petla ktora czyta pixele i zamienia je zgodnie ze wzorem
        for (int y = 0; y < colorImage.rows(); y++) {
            for (int x = 0; x < colorImage.cols(); x++) {
                double[] rgb = colorImage.get(y, x);
                double grayValue = 0.299 * rgb[2] + 0.587 * rgb[1] + 0.114 * rgb[0];
                grayImage.put(y, x, grayValue);
            }
        }


        String outputFilePath = "/Users/szymon/Library/Mobile Documents/com~apple~CloudDocs/Nauka/Java/tescik/src/main/resources/ConImg.jpeg";
        Imgcodecs.imwrite(outputFilePath, grayImage);

        String imagePath = outputFilePath;

        File imageFile = new File(imagePath);

        // Tworzenie obiektu Image z załadowanego obrazu
        Image image = new Image(imageFile.toURI().toString());

        // Ustawienie obrazu w ImageView
        ImageOutput.setImage(image);

        Mat histogram = new Mat();
        Imgproc.calcHist(
                java.util.Collections.singletonList(grayImage),
                new MatOfInt(0),
                new Mat(),
                histogram,
                new MatOfInt(256),
                new MatOfFloat(0, 256)
        );

        Core.normalize(histogram, histogram, 0, 255, Core.NORM_MINMAX);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < histogram.rows(); i++) {
            double binValue = histogram.get(i, 0)[0];
            series.getData().add(new XYChart.Data<>(i, binValue));
        }

        Hist1.getData().add(series);


    }

    //Przycisk do binaryzacji
    public void OnBINButtonClick(ActionEvent actionEvent) {
        int value = (BinValue1 == null || BinValue1.getLength() == 0) ? 128 : Integer.parseInt(BinValue1.getText().trim()); // Jesli textfield wynosi null wtedy domyslna wartosc
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.binarizeImage(inputImage, value);
        ImageOutput.setImage(outputImage);

    }

    //Przycisk odejmujacy 2 obrazy
    public void OnTwoImgButtonClick(ActionEvent actionEvent)
    {
        Image originalImage = ImageInput.getImage();
        Image subtractedImage = ImageInput2.getImage();

        Image resultImage = Functions.subtractImages(originalImage, subtractedImage);
        ImageOutput.setImage(resultImage);
    }

    // Przycisk do filtru mediany
    public void OnMedFiltrButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.medianFilter(inputImage);
        ImageOutput.setImage(outputImage);
    }

    // Przycisk do filtru minimalnego
    public void OnMinFiltrButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.minFilter(inputImage);
        ImageOutput.setImage(outputImage);
    }

    // Przycisk do filtru maksymalnego
    public void OnMaxFiltrButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.maxFilter(inputImage);
        ImageOutput.setImage(outputImage);
    }

    //Przycisk do krawedzi poziomej
    public void OnHoriEdgeButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.HoriEdgeDetection(inputImage);
        ImageOutput.setImage(outputImage);
    }

    //Przycisk do krawedzi pionowej
    public void OnVertEdgeButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.vertEdgeDetection(inputImage);
        ImageOutput.setImage(outputImage);
    }

    //Przycisk do krawedzi ukosnej
    public void OnDiagEdgeButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.diagEdgeDetection(inputImage);
        ImageOutput.setImage(outputImage);
    }

    //Przycisk do wszystkich krawedzi
    public void OnAllEdgeButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.AllEdgeDetection(inputImage);
        ImageOutput.setImage(outputImage);
    }

    //Przycisk do filtracji górnoprzepustowej
    public void OnGpFiltrButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.applyHighPassFilter(inputImage);
        ImageOutput.setImage(outputImage);
    }
    //Przycisk do filtracji dolnoprzepustowej
    public void OnDpFiltrButton(ActionEvent actionEvent) {
        Image inputImage = ImageInput.getImage();
        Image outputImage = Functions.applyLowPassFilter(inputImage);
        ImageOutput.setImage(outputImage);

    }

    public class Global {
        public static Stage stage;
    }
    }
