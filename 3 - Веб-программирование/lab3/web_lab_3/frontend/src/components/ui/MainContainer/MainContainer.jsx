import styles from './MainContainer.module.css';

export function MainContainer({ children }) {
    return (
        <div className={styles.container}>
            <div className={styles.backgroundImage}></div>
            <div className={styles.blur}></div>
            {children}
        </div>
    );
}