'use client';
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import { authApi } from '../../lib/api/authApi';
import { useStore } from '@tanstack/react-store';
import { authStore } from '../../lib/stores/authStore';
import { Header } from '../../components/ui/Header/Header';
import { MainContainer } from '../../components/ui/MainContainer/MainContainer';
import { AuthInput } from '../../components/auth/AuthInput/AuthInput';
import styles from './page.module.css';

export default function RegisterPage() {
    const router = useRouter();
    const isAuthenticated = useStore(authStore, (state) => state.isAuthenticated);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const { register, handleSubmit,
        formState: { errors } } = useForm({
        defaultValues: { username: '', password: '' } });

    const registerMutation = useMutation({
        mutationFn: authApi.register,
        onSuccess: (data) => {
            setSuccessMessage(data.message || 'Регистрация успешна!');
            setTimeout(() => {
                router.push('/login');
            }, 2000);
        },
        onError: (error) => {
            setErrorMessage(error.message);
        }
    });

    const onSubmit = (data) => {
        console.log("Submitting register..." + data);
        setErrorMessage('');
        setSuccessMessage('');
        registerMutation.mutate(data);
    };

    useEffect(() => {
        if (isAuthenticated) {
            router.push('/main');
        }
    }, [isAuthenticated, router]);

    if (isAuthenticated) {
        return <div>Перенаправление...</div>;
    }
    const handleRegisterClick = (e) => {
        e.preventDefault();
        router.push('/login');
    };


    return (
        <MainContainer>
            <Header />
            <form className={styles.loginForm} onSubmit={handleSubmit(onSubmit)}>
                <AuthInput
                    name="username"
                    type="text"
                    label="Логин"
                    placeholder="Введите логин"
                    register={register}
                    error={errors.username}
                    rules={{
                        required: 'Логин обязателен',
                        minLength: { value: 5, message: 'Минимум 5 символов' },
                        pattern: {
                            value: /^[a-zA-Z0-9._-]+$/,
                            message: 'Только английские буквы, цифры и . _ -'
                        }
                    }}
                />

                <AuthInput
                    name="password"
                    type="password"
                    label="Пароль"
                    placeholder="Введите пароль"
                    register={register}
                    error={errors.password}
                    rules={{
                        required: 'Пароль обязателен',
                        minLength: { value: 8, message: 'Минимум 8 символов' },
                        pattern: {
                            value: /^[a-zA-Z0-9#$%&@~?!_]+$/,
                            message: 'Только английские буквы, цифры и # $ % & @ ~ ? ! _'
                        }
                    }}
                />

                <div className={styles.buttonContainer}>
                    <button
                        type="submit"
                        className={styles.button}
                        disabled={registerMutation.isPending}
                    >
                        {registerMutation.isPending ? 'Регистрация...' : 'Зарегистрироваться'}
                    </button>
                    <button onClick={handleRegisterClick} className={styles.link}>
                        Войти
                    </button>
                </div>

                {errorMessage && (
                    <div className={styles.serverError}>
                        {errorMessage}
                    </div>
                )}

                {successMessage && (
                    <div className={styles.successMessage}>
                        {successMessage}
                    </div>
                )}
            </form>
        </MainContainer>
    );
}