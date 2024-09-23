import { BrowserRouter, Route, Routes } from 'react-router-dom';

import DefaultLayout from './layouts/DefaultLayout';

import RequireAuth from './utils/RequireAuth';
import { ROLES } from './common/roleConstants';

import Home from './pages/Home';
import Login from './pages/Login';
import ForgetPassword from './pages/ForgetPassword';
import AccessDenied from './pages/AccessDenied';
import NotFound from './pages/NotFound';
import News from './pages/News';
import About from './pages/About';
import HolidaySchedule from './pages/HolidaySchedule';
import Rules from './pages/Rules';
import Report from './pages/Report';
import Search from './pages/Search';
import AdminLayout from './layouts/AdminLayout';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<DefaultLayout />}>
                    <Route index element={<Home />} />
                    <Route path="login" element={<Login />} />
                    <Route path="forget-password" element={<ForgetPassword />} />

                    <Route path="news" element={<News />} />
                    <Route path="about" element={<About />} />
                    <Route path="holiday-schedule" element={<HolidaySchedule />} />
                    <Route path="rules" element={<Rules />} />
                    <Route path="report" element={<Report />} />
                    <Route path="search" element={<Search />} />

                    <Route element={<RequireAuth />}>{/* Đường dẫn yêu cầu đăng nhập */}</Route>
                </Route>

                <Route element={<RequireAuth allowedRoles={[ROLES.SuperAdmin, ROLES.Admin]} />}>
                    <Route path="admin/" element={<AdminLayout />}>
                        {/* Đường dẫn yêu cầu quyền quản trị */}
                    </Route>
                </Route>

                <Route path="access-denied" element={<AccessDenied />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
