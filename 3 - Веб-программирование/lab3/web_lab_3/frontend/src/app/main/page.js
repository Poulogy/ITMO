'use client';
import { ProtectedRoute } from '../../components/auth/ProtectedRoute/ProtectedRoute';
import { Header } from '../../components/ui/Header/Header';
import { MainContainer } from '../../components/ui/MainContainer/MainContainer';
import { LogoutButton } from '../../components/ui/LogoutButton/LogoutButton';
import { MainForm } from '../../components/canvas/MainForm/MainForm';

export default function MainPage() {
    return (
        <ProtectedRoute>
            <MainContainer>
                <Header />
                <LogoutButton />
                <MainForm />
            </MainContainer>
        </ProtectedRoute>
    );
}