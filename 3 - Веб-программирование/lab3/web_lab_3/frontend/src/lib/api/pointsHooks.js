import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useStore } from '@tanstack/react-store';
import { authStore } from '../stores/authStore';
import { pointsApi } from './pointsApi';

export function usePoints() {
    const token = useStore(authStore, (state) => state.token);
    const isAuthenticated = useStore(authStore, (state) => state.isAuthenticated);
    console.log("Trying to get Points: " + isAuthenticated + token);

    return useQuery({
        queryKey: ['points', token],
        queryFn: () => pointsApi.getPoints(token),
        enabled: isAuthenticated && !!token,
    });
}

export function useAddPoint() {
    const queryClient = useQueryClient();
    console.log("Trying to add point");
    return useMutation({
        mutationFn: ({ point, token }) => pointsApi.addPoint(point, token),

        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['points'] });
        },
    });
};