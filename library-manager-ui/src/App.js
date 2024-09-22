import { BrowserRouter, Route, Routes } from 'react-router-dom';

import DefaultLayout from './layouts/DefaultLayout';

import RequireAuth from './utils/RequireAuth';
import { ROLES } from './common/roleConstants';

import Home from './pages/Home';
import Login from './pages/Login';
import ForgetPassword from './pages/ForgetPassword';
import AccessDenied from './pages/AccessDenied';
import NotFound from './pages/NotFound';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<DefaultLayout />}>
                    <Route index element={<Home />} />

                    <Route element={<RequireAuth />}>{/* Đường dẫn yêu cầu đăng nhập */}</Route>

                    <Route element={<RequireAuth allowedRoles={[ROLES.SuperAdmin, ROLES.Admin]} />}>
                        {/* Đường dẫn yêu cầu quyền quản trị */}
                    </Route>
                </Route>

                <Route path="login" element={<Login />} />
                <Route path="forget-password" element={<ForgetPassword />} />
                <Route path="access-denied" element={<AccessDenied />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;