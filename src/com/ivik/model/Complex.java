package com.ivik.model;

/**
 * Created by Sir Royal Air Benny on 18-2-2016.
 */
public class Complex {
    private double re; // real number part
    private double im; // imaginary number part

    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    public Complex plus(Complex b) {
        Complex a = this;
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    public double modulus() {
        return Math.sqrt(re*re + im*im);
    }
}
