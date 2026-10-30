import styles from './XField.module.css';

const xOptions = [
    { value: -2, label: '-2' },
    { value: -1.5, label: '-1.5' },
    { value: -1, label: '-1' },
    { value: -0.5, label: '-0.5' },
    { value: 0, label: '0' },
    { value: 0.5, label: '0.5' },
    { value: 1, label: '1' },
    { value: 1.5, label: '1.5' },
    { value: 2, label: '2' }
];



export function XField({ register, error, showError }) {
    return (
        <div className={styles.field}>
            <span className={styles.label}>Выберите X:</span>

            <div className={styles.radioGroup}>
                {xOptions.map(option => (
                    <label key={option.value} className={styles.label}>
                        <input
                            type="radio"
                            value={option.value}
                            {...register('x', {
                                required: 'X обязателен',
                                validate: value => {
                                    if (value === '') return 'Выберите значение X';
                                    if (isNaN(value)) return 'Координата X должна быть числом';
                                    if (value < -2 || value > 2) return 'Координата X должна быть в диапазоне (-2; 2)';
                                    return true;
                                }
                            })}
                            className={styles.input}
                        />
                        {option.label}
                    </label>
                ))}
            </div>

            {error && (
                <div className={`${styles.error} ${showError ? styles.show : ''}`}>
                    {error.message}
                </div>
            )}
        </div>
    );
}