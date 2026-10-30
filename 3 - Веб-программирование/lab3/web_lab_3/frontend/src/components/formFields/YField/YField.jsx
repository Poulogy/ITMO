import styles from './YField.module.css';

export function YField({ register, error, showError}) {
    return (
        <div className={styles.field}>
            <div className={styles.inputContainer}>
                <span className={styles.label}>Выберите Y:</span>
                <input
                    type="text"
                    {...register('y', {
                        required: 'Координата Y обязательна',
                        validate: (value) => {
                            const num = Number(value);
                            if (isNaN(num)) return 'Координата Y должна быть числом';
                            if (num < -5 || num > 3) return 'Координата Y должна быть в диапазоне (-5; 3)';
                            return true;
                        }
                    })}

                    className={styles.input}
                />
            </div>
            {error && (
                <div className={`${styles.error} ${showError ? styles.show : ''}`}>
                    {error.message}
                </div>
            )}
        </div>
    );
}