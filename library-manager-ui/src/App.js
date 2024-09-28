import { BrowserRouter, Route, Routes } from 'react-router-dom';

import DefaultLayout from './layouts/DefaultLayout';

import RequireAuth from './utils/RequireAuth';
import { ROLES } from './common/roleConstants';

import Home from './pages/Misc/Home';
import Login from './pages/Auth/Login';
import ForgotPassword from './pages/Auth/ForgotPassword';
import AccessDenied from './pages/Misc/AccessDenied';
import NotFound from './pages/Misc/NotFound';
import News from './pages/Misc/News';
import About from './pages/Misc/About';
import HolidaySchedule from './pages/Misc/HolidaySchedule';
import Rules from './pages/Misc/Rules';
import Report from './pages/Misc/Report';
import Search from './pages/Misc/Search';
import AdminLayout from './layouts/AdminLayout';
import AdminLogin from './pages/Auth/AdminLogin';
import AdminForgotPassword from './pages/Auth/AdminForgotPassword';
import BookDetail from './pages/Books/BookDetail';
import InwardBook from './pages/Misc/InwardBook';
import Author from './pages/Authors/Author';
import AuthorForm from './pages/Authors/AuthorForm';
import BookSet from './pages/BookSet/BookSet';
import Category from './pages/Category/Category';
import Publisher from './pages/Publisher/Publisher';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<DefaultLayout />}>
                    <Route index element={<Home />} />
                    <Route path="login" element={<Login />} />
                    <Route path="forgot-password" element={<ForgotPassword />} />

                    <Route path="news" element={<News />} />
                    <Route path="about" element={<About />} />
                    <Route path="holiday-schedule" element={<HolidaySchedule />} />
                    <Route path="rules" element={<Rules />} />
                    <Route path="report" element={<Report />} />
                    <Route path="search" element={<Search />} />
                    <Route path="book/:bookId" element={<BookDetail />} />

                    <Route element={<RequireAuth />}>{/* Đường dẫn yêu cầu đăng nhập */}</Route>
                </Route>

                <Route element={<RequireAuth allowedRoles={[ROLES.SuperAdmin, ROLES.Admin]} />}>
                    <Route path="admin/" element={<AdminLayout />}>
                        {/* Đường dẫn yêu cầu quyền quản trị */}

                        <Route path="Book/">
                            <Route path="BookList" element={<h2>BookList</h2>} />
                            <Route path="Inwardbook" element={<InwardBook />} />
                        </Route>

                        {/* Tác giả */}
                        <Route path="authors">
                            <Route index element={<Author />} />
                            <Route path="new" element={<AuthorForm />} />
                            <Route path="edit/:id" element={<AuthorForm />} />
                        </Route>

                        {/* Bộ sách */}
                        <Route path="collections">
                            <Route index element={<BookSet />} />
                        </Route>

                        {/* Danh mục */}
                        <Route path="categories">
                            <Route index element={<Category />} />
                        </Route>

                        {/* Nhà xuất bản */}
                        <Route path="publishers">
                            <Route index element={<Publisher />} />
                        </Route>
                    </Route>
                </Route>

                <Route path="admin/login" element={<AdminLogin />} />
                <Route path="admin/forgot-password" element={<AdminForgotPassword />} />

                <Route path="access-denied" element={<AccessDenied />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
