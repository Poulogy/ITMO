import { useErrorVisibility } from '../../../hooks/useErrorVisibility';
import styles from './AuthInput.module.css';

export function AuthInput({ name, type, label, placeholder, register, rules, error }) {
    const showError = useErrorVisibility(error);

    return (
        <div className={styles.formGroup}>
            <label htmlFor={name}>{label}</label>
            <input
                type={type}
                id={name}
                placeholder={placeholder}
                {...register(name, rules)}
                className={styles.input}
            />
            {error && (
                <div className={`${styles.error} ${showError ? styles.show : ''}`}>
                    {error.message}
                </div>
            )}
        </div>
    );
}