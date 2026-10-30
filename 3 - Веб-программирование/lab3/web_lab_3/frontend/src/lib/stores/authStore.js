import { Store } from '@tanstack/react-store';

export const authStore = new Store({
    user: null,
    token: null,
    isAuthenticated: false,
});

export const authActions = {
    login: (userData) => {
        console.log("Trying to save login data: " + userData.token, userData.username);
        authStore.setState((state) => ({
            ...state,
            user: {username: userData.username},
            token: userData.token,
            isAuthenticated: true
        }));
    },

    logout: () => {
        console.log("Trying to log out");
        authStore.setState((state) => ({
            ...state,
            user: null,
            token: null,
            isAuthenticated: false
        }));
    }
};