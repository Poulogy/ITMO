import styles from './Header.module.css';

export function Header() {
    return (
        <header className={styles.header}>
            <h1>Веб-программирование| Лабораторная работа №3| Вариант 985217</h1>
            <div className={styles.headerContent}>
                <h2>Джантуре Назерке| Группа: Р3208</h2>
                <a href="https://github.com/Nazerkusha" target="_blank" rel="noreferrer">
                    <img className={styles.githubIcon} src="/images/GitLable.png" alt="GitHub"/>
                </a>
            </div>
        </header>
    );
}