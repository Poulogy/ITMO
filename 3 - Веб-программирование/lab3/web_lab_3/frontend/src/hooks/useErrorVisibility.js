import { useEffect, useState } from 'react';

export const useErrorVisibility = (errorMessage) => {
    const [showError, setShowError] = useState(false);

    useEffect(() => {
        if (!errorMessage) return;

        const timeout1 = setTimeout(() => setShowError(true), 10);
        const timeout2 = setTimeout(() => setShowError(false), 4000);

        return () => {
            clearTimeout(timeout1);
            clearTimeout(timeout2);
        };
    }, [errorMessage]);

    return showError;
};