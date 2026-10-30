package com.lab3.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValidationService {


    public void validatePoint(double x, double y, double r) {
        if (x <= -2 || x >= 2) {
            throw new IllegalArgumentException("Координата X должна быть одним из значений: -2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2");
        }

        if (y < -5 || y > 3) {
            throw new IllegalArgumentException("Координата Y должна быть в диапазоне (-5; 3)");
        }

        if (r < 0 || r > 2) {
            throw new IllegalArgumentException("Радиус R должен быть одним из значений: -2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2");
        }

        if (r <= 0) {
            throw new IllegalArgumentException("Радиус R должен быть больше 0");
        }
    }
}