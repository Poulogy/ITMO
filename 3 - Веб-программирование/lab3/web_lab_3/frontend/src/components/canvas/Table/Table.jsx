import styles from './Table.module.css';

export function Table({ points }) {
    if (!points || points.length === 0) {
        return <div className={styles.noData}>Нет данных</div>;
    }

    return (
        <div className={styles.tableContainer}>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Попали?</th>
                    <th>Текущая дата</th>
                    <th>Время работы скрипта</th>
                </tr>
                </thead>
                <tbody>
                {points.map((point, index) => (
                    <tr key={index}>
                        <td>{point.x}</td>
                        <td>{point.y}</td>
                        <td>{point.r}</td>
                        <td style={{ color: point.hit ? "#79d34a" : "#bc2929" }}>
                            {point.hit ? 'Да' : 'Нет'}
                        </td>
                        <td>{point.serverTime}</td>
                        <td>{point.scriptTime}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}