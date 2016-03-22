package com.ivik.model;


import com.ivik.util.IterableFunction;

import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Created by Sir Royal Air Benny on 18-2-2016.
 */
public class Mandelbrot implements IterableFunction {
    private double startX;
    private double startY;
    private double width;
    private double height;

    private int pixelRows;
    private int pixelColumns;
    private int maxIterations;

    private int[][] counts;

    private double dX;
    private double dY;

    private BufferedImage image;

    public Mandelbrot(double sX, double sY, double w, double h, int rows, int cols, int max) {
        startX = sX;
        startY = sY;
        width = w;
        height = h;
        pixelRows = rows;
        pixelColumns = cols;
        maxIterations = max;

        dX = width / (cols-1);
        dY = height / (rows-1);

        counts = new int[pixelRows][pixelColumns];

        //mapMandelbrot();
        iterateFunction();
    }

    private void mapMandelbrot() {
        image = new BufferedImage(pixelRows, pixelColumns, BufferedImage.TYPE_INT_RGB);

        int black = 0;
        int[] colors = new int[maxIterations];
        for (int i = 0; i<maxIterations; i++) {
            colors[i] = Color.HSBtoRGB(i/256f, 1, i/(i+8f));
        }

        for (int i = 0; i < pixelRows; i++) {
            for (int j = 0; j < pixelColumns; j++) {

                Complex z = new Complex(0, 0);
                double c_re = startX + j * dX;
                double c_im = startY - i * dY;
                Complex c = new Complex(c_re, c_im);

                int iterations = 0;

                while(true) {
                    // f(x) = x² + c
                    z = (z.times(z)).plus(c);
                    iterations++;
                    if(iterations > maxIterations || z.modulus() > 2) break;
                }
                if (iterations >= maxIterations) {
                    counts[i][j] = 0;
                    image.setRGB(j, i, black);
                } else {
                    counts[i][j] = iterations;
                    image.setRGB(j, i, colors[iterations]);
                }
            }
        }
        //try {
        //   ImageIO.write(image, "png", new File("src/com/ivik/resources/"+fileName));
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }
    @Override
    public void iterateFunction() {
        image = new BufferedImage(pixelRows, pixelColumns, BufferedImage.TYPE_INT_RGB);

        int black = 0;
        int[] colors = new int[maxIterations];
        for (int i = 0; i<maxIterations; i++) {
            colors[i] = Color.HSBtoRGB(i/256f, 1, i/(i+8f));
        }

        for (int i = 0; i < pixelRows; i++) {
            for (int j = 0; j < pixelColumns; j++) {

                Complex z = new Complex(0, 0);
                double c_re = startX + j * dX;
                double c_im = startY - i * dY;
                Complex c = new Complex(c_re, c_im);

                int iterations = 0;

                while(true) {
                    // f(x) = x² + c
                    z = (z.times(z)).plus(c);
                    iterations++;
                    if(iterations > maxIterations || z.modulus() > 2) break;
                }
                if (iterations >= maxIterations) {
                    counts[i][j] = 0;
                    image.setRGB(j, i, black);
                } else {
                    counts[i][j] = iterations;
                    image.setRGB(j, i, colors[iterations]);
                }
            }
        }
        //try {
        //   ImageIO.write(image, "png", new File("src/com/ivik/resources/"+fileName));
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
    }

    public BufferedImage getMandelbrotBufferedImage() {
        return image;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getdX() {
        return dX;
    }

    public double getdY() {
        return dY;
    }

}
