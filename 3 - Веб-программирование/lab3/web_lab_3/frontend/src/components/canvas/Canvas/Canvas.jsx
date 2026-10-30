'use client';
import { useRef, useEffect, useState } from 'react';
import { canvasSize, center, drawPointOnCoordinatePlane, redraw } from '../../../lib/utils/canvas';
import { useErrorVisibility } from '../../../hooks/useErrorVisibility';
import { useAddPoint } from '../../../lib/api/pointsHooks';
import styles from './Canvas.module.css';
import {useStore} from "@tanstack/react-store";
import {authStore} from "../../../lib/stores/authStore";

export function Canvas({ r, points }) {
    const token = useStore(authStore, (state) => state.token);
    const canvasRef = useRef(null);
    const [errorMessage, setErrorMessage] = useState(null);
    const [serverError, setServerError] = useState('');
    const serverShowError = useErrorVisibility(serverError);
    const showError = useErrorVisibility(errorMessage);
    const addPointMutation = useAddPoint();

    const showCanvasError = (message) => {
        setErrorMessage(null);
        setTimeout(() => setErrorMessage(message), 10);
    };
    const showServerError = (message) => {
        setServerError(null);
        setTimeout(() => setServerError(message), 10);
    };


    const handleCanvasClick = async (event) => {
        if (r === null || isNaN(r)) {
            showCanvasError("Нужно сначала выбрать радиус");
            return;
        } else if (r <= 0) {
            showCanvasError("Радиус R должен быть больше 0");
            return;
        }

        const canvas = canvasRef.current;
        const rect = canvas.getBoundingClientRect();
        const gridCellsPerRadius = 2;
        const scale = (canvasSize / 2) / (r * gridCellsPerRadius);
        const x_px = event.clientX - rect.left;
        const y_px = event.clientY - rect.top;
        const realX = (x_px - center) / scale;
        const realY = -((y_px - center) / scale);
        const x = parseFloat(realX.toFixed(4));
        const y = parseFloat(realY.toFixed(4));

        newPoints(x, y);

        if (x < -2 || x > 2) {
            showCanvasError("Координата X должна быть в диапазоне [-2; 2]");
            return;
        }
        if (y <= -5 || y >= 3) {
            showCanvasError("Координата Y должна быть в диапазоне (-5; 3)");
            return;
        }
        if (!token) {
            showServerError("Ошибка: Токен не найден после авторизации.");
            return;
        }
        console.log("Submitting canvas..." + x, y, r, token);
        try {
            await addPointMutation.mutateAsync({
                point: {
                    x: x,
                    y: y,
                    r: r
                },
                token: token
            });
        } catch (error) {
            console.log("Error in server..." + error.message);
            showServerError(error.message);
            showCanvasError(error.message);
        }

    };

    useEffect(() => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        const ctx = canvas.getContext("2d");
        if (!ctx) return;

        redraw(r > 0 ? r : null, ctx);

        if (points && points.length > 0) {
            const gridCellsPerRadius = 2;
            const scale = (canvasSize / 2) / (r * gridCellsPerRadius);

            points
                .filter(p => p.r === r)
                .forEach((p) => {
                    drawPointOnCoordinatePlane(ctx, p.x, p.y, p.hit, scale)
                });
        }
    }, [r, points]);

    return (
        <div className={styles.canvasContainer}>
            <canvas
                ref={canvasRef}
                width="400"
                height="400"
                onClick={handleCanvasClick}
                className={styles.canvas}
            ></canvas>
            <div className={styles.errorContainer}>
                {errorMessage && (
                    <div className={`${styles.error} ${showError ? styles.show : ''}`}>
                        {errorMessage}
                    </div>
                )}
            </div>
            {serverError && (
                <div className={`${styles.serverError} ${serverShowError ? styles.show : ''}`}>
                    {serverError}
                </div>
            )}
        </div>
    );
}