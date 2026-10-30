import { authActions, authStore } from '../../../lib/stores/authStore';
import { useStore } from '@tanstack/react-store';
import { useRouter } from 'next/navigation';
import { useMutation } from '@tanstack/react-query';
import { authApi } from '../../../lib/api/authApi';
import styles from "../LogoutButton/LogoutButton.module.css";

export function LogoutButton() {
    const token = useStore(authStore, (state) => state.token);
    const router = useRouter();

    const logoutMutation = useMutation({
        mutationFn: () => {
            if (token) {
                return authApi.logout(token);
            }
            return Promise.resolve();
        },

        onSuccess: () => {
            authActions.logout();
            router.push('/login');
        },

        onError: (error) => {
            console.error("Logout API failed, but forcing local logout:", error);
            authActions.logout();
            router.push('/login');
        }
    });
    const handleLogout = () => {
        logoutMutation.mutate();
    };

    return (
        <div className={styles.container}>
            <button onClick={handleLogout} disabled={logoutMutation.isPending} className={styles.logoutButton}>
                {logoutMutation.isPending ? 'Выход...' : 'Выйти'}
            </button>
        </div>
    );
}