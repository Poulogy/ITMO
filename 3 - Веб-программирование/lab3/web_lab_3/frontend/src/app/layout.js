import { Providers } from '../lib/utils/providers';
import '../styles/globals.css';

export const metadata = {
    title: 'Web Lab 3 - Вариант 985217',
    description: 'Лабораторная работа по веб-программированию',
};

export default function RootLayout({ children }) {
    return (
        <html lang="ru">
        <body>
        <Providers>
            {children}
        </Providers>
        </body>
        </html>
    );
}