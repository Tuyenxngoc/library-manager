import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.jsx';

import { AuthProvider } from './contexts/AuthProvider';
import { ConfigProvider } from 'antd';
import viVN from 'antd/es/locale/vi_VN';

import 'normalize.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles/index.css';

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <ConfigProvider locale={viVN}>
            <AuthProvider>
                <App />
            </AuthProvider>
        </ConfigProvider>
    </StrictMode>,
);
