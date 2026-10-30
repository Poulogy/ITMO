import styles from './RField.module.css';

const rOptions = [
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

export function RField({ register, error, showError }) {
    return (
        <div className={styles.field}>
            <span className={styles.label}>Выберите R:</span>
            <div className={styles.radioGroup}>
                {rOptions.map(option => (
                    <label key={option.value} className={styles.label}>
                        <input
                            type="radio"
                            value={option.value}
                            {...register('r', {
                                required: 'Радиус R обязателен',
                                validate: value => {
                                    if (Number(value) <= 0) return 'Радиус R должен быть больше 0';
                                    return true;
                                }
                            })}
                            className={styles.input}
                        />
                        {option.label}
                    </label>
                ))}
            </div>
    {
        error && (
            <div className={`${styles.error} ${showError ? styles.show : ''}`}>
                {error.message}
            </div>
        )
    }
</div>
)
    ;
}