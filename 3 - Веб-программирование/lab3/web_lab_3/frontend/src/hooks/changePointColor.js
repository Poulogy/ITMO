import {useCallback, useState} from 'react';


export const changePointColor = (points) => {
    const [coordinates, setChange] = useState([])
    const pointsHighlighted = useCallback((x,y) => {
        return coordinates.includes(`${x},${y}`); }, [coordinates]);
    const newPoints = useCallback((newX, newY) => {
        let isColored = false;
        for (const p of points) {
            const d = Math.sqrt(Math.pow(p.x - newX, 2) + Math.pow(p.y - newY, 2));
            if (d < 0.5){
                isColored = true;
                break;
            }
        }
        if (isColored) {
            setChange(prev => {
                    const newCoords = `${newX},${newY}`;
                    return prev.includes(newCoords) ? prev : [...prev, newCoords];
                });
        } else {
            setChange([])
        }
        return isColored;
    }, [points]);
    return {newPoints, pointsHighlighted};
}