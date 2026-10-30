import {changePointColor} from "../../hooks/changePointColor";

const canvasSize = 400;
const lineWidth = 2;
const textMargin = 9;
const mainColor = '#2f0535';
const center = canvasSize / 2;
const gridCellsPerRadius = 2;

function setupCanvas(ctx) {
    ctx.font = '13px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    return ctx;
}

function drawGrid(ctx, scale) {
    ctx.strokeStyle = 'rgba(207,4,179,0.5)';
    ctx.lineWidth = 1;
    const step = scale / gridCellsPerRadius;

    for (let i = 0; i <= canvasSize; i += step) {
        ctx.beginPath();
        ctx.moveTo(i, 0);
        ctx.lineTo(i, canvasSize);
        ctx.stroke();
    }

    for (let j = 0; j <= canvasSize; j += step) {
        ctx.beginPath();
        ctx.moveTo(0, j);
        ctx.lineTo(canvasSize, j);
        ctx.stroke();
    }
}

function drawAxisLabels(ctx, r, scale) {
    const steps = [
        { value: -1, label: '-R' },
        { value: -0.5, label: '-R/2' },
        { value: 0.5, label: 'R/2' },
        { value: 1, label: 'R' }
    ];

    const tickLength = 6;
    ctx.fillStyle = mainColor;

    steps.forEach(step => {
        const xValue = center + step.value * scale * (r || 1);
        const xLabel = r ? (step.value * r).toString() : step.label;

        ctx.fillText(xLabel, xValue, center + textMargin * 1.5);
        ctx.beginPath();
        ctx.moveTo(xValue, center - tickLength / 2);
        ctx.lineTo(xValue, center + tickLength / 2);
        ctx.stroke();
    });

    steps.forEach(step => {
        const yValue = center - step.value * scale * (r || 1);
        const yLabel = r ? (step.value * r).toString() : step.label;

        ctx.fillText(yLabel, center - textMargin * 1.5, yValue);
        ctx.beginPath();
        ctx.moveTo(center - tickLength / 2, yValue);
        ctx.lineTo(center + tickLength / 2, yValue);
        ctx.stroke();
    });
}

function drawCoordinatePlane(ctx, r, scale) {
    ctx.lineWidth = lineWidth;
    ctx.strokeStyle = mainColor;

    // Ось X
    ctx.beginPath();
    ctx.moveTo(0, center);
    ctx.lineTo(canvasSize, center);
    ctx.stroke();

    // Ось Y
    ctx.beginPath();
    ctx.moveTo(center, canvasSize);
    ctx.lineTo(center, 0);
    ctx.stroke();

    drawAxisLabels(ctx, r, scale);
}

function convertIntoCanvasCoordinates(x, y, scale) {
    return [center + x * scale, center - y * scale];
}

function drawShapes(ctx, r, scale) {
    const rForDrawing = r || 1;
    ctx.lineWidth = lineWidth;
    ctx.strokeStyle = 'rgba(186,4,161,0.3)';
    ctx.fillStyle = 'rgba(255,220,250,0.6)';

    // Окружность (1-я четверть)
    ctx.beginPath();
    ctx.arc(center, center, rForDrawing * scale,  (3 * Math.PI) / 2, 2 * Math.PI,false);
    ctx.lineTo(center, center);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    // Треугольник (4-я четверть)
    ctx.beginPath();
    ctx.moveTo(center, center);
    ctx.lineTo(center + rForDrawing * scale, center);
    ctx.lineTo(center, center + (rForDrawing / 2) * scale);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();

    // Прямоугольник (3-я четверть)
    ctx.beginPath();
    ctx.moveTo(center, center);
    ctx.lineTo(center - rForDrawing * scale, center);
    ctx.lineTo(center - rForDrawing * scale, center + (rForDrawing/2) * scale);
    ctx.lineTo(center, center + (rForDrawing/2) * scale);
    ctx.closePath();
    ctx.fill();
    ctx.stroke();
}

function drawPointOnCoordinatePlane(ctx, x, y, wasThereHit, scale) {
    ctx.fillStyle = wasThereHit ? '#3c8f15' : '#D14545';
    ctx.beginPath();
    const [newX, newY] = convertIntoCanvasCoordinates(x, y, scale);
    ctx.arc(newX, newY, 4, 0, 2 * Math.PI);
    ctx.fill();
}

function redraw(r, ctx) {
    if (!ctx) return;

    setupCanvas(ctx);
    ctx.clearRect(0, 0, canvasSize, canvasSize);

    const scale = (canvasSize / 2) / ((r || 1) * gridCellsPerRadius);
    drawGrid(ctx, scale);
    drawCoordinatePlane(ctx, r, scale);
    drawShapes(ctx, r, scale);
}

export {drawPointOnCoordinatePlane, redraw, center, canvasSize };