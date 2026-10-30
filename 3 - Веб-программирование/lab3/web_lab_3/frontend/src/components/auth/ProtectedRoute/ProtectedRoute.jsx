'use client';
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useStore } from '@tanstack/react-store';
import { authStore } from '../../../lib/stores/authStore';

export function ProtectedRoute({ children }) {
    const router = useRouter();
    const isAuthenticated = useStore(authStore, (state) => state.isAuthenticated);

    useEffect(() => {
        if (!isAuthenticated) {
            router.push('/login');
        }
    }, [isAuthenticated, router]);

    if (!isAuthenticated) {
        return <div>Проверка авторизации...</div>;
    }

    return children;
}