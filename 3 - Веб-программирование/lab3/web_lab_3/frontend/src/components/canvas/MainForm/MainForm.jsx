'use client';
import { useStore } from "@tanstack/react-store";
import { authStore } from "../../../lib/stores/authStore";
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { usePoints, useAddPoint } from '../../../lib/api/pointsHooks';
import { useErrorVisibility } from '../../../hooks/useErrorVisibility';
import { XField } from '../../formFields/XField/XField';
import { YField } from '../../formFields/YField/YField';
import { RField } from '../../formFields/RField/RField';
import { Canvas } from '../Canvas/Canvas';
import { Table } from '../Table/Table';
import styles from './MainForm.module.css';

export function MainForm() {
    const isAuthenticated = useStore(authStore, (state) => state.isAuthenticated);
    const token = useStore(authStore, (state) => state.token);
    const { data: points, isLoading, error} = usePoints();
    const addPointMutation = useAddPoint();
    const [yValue, setYValue] = useState('0');
    const [serverError, setServerError] = useState('');

    const { register, handleSubmit, reset,
        watch, formState: { errors } } = useForm({
        defaultValues: { x: '', y: '0', r: '' }
    });

    const xShowError = useErrorVisibility(errors.x);
    const yShowError = useErrorVisibility(errors.y);
    const rShowError = useErrorVisibility(errors.r);
    const serverShowError = useErrorVisibility(serverError);

    const currentR = watch('r');

    const onSubmit = async (data) => {
        setServerError('');
        if (!token) {
            setServerError("Ошибка: Токен не найден после авторизации.");
            return;
        }
        try {
            console.log("Submitting..." + data.x, data.y, data.r, token);
            await addPointMutation.mutateAsync({
                point: {
                    x: Number(data.x),
                    y: Number(data.y),
                    r: Number(data.r)
                },
                token: token
            });
        } catch (error) {
            setServerError(error.message);
        }
    };

    const handleClearForm = () => {
        reset();
        setYValue('0');
        setServerError('');
    };

    if (!isAuthenticated) return <div className={styles.loading}>Ожидание авторизации...</div>;
    if (isLoading) return <div className={styles.loading}>Загрузка истории...</div>;
    if (error) return <div className={styles.error}>Ошибка загрузки: {error.message}</div>;

    return (
        <div className={styles.container}>
            <div className={styles.leftColumn}>
                <form className={styles.form} onSubmit={handleSubmit(onSubmit)}>
                    <XField
                        register={register}
                        error={errors.x}
                        showError={xShowError}
                    />

                    <YField
                        register={register}
                        error={errors.y}
                        showError={yShowError}
                        value={yValue}
                        onChange={setYValue}
                    />

                    <RField
                        register={register}
                        error={errors.r}
                        showError={rShowError}
                    />

                    <div className={styles.buttonContainer}>
                        <button type="submit" className={styles.button}>
                            Подтвердить
                        </button>
                        <button
                            type="button"
                            className={styles.button}
                            onClick={handleClearForm}
                        >
                            Очистить форму
                        </button>
                    </div>

                    {serverError && (
                        <div className={`${styles.serverError} ${serverShowError ? styles.show : ''}`}>
                            {serverError}
                        </div>
                    )}
                </form>

                <h3 className={styles.historyHeader}>История проверок</h3>
                <Table points={points} />
            </div>

            <Canvas r={currentR ? Number(currentR) : null} points={points} />
        </div>
    );
}