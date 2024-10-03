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
import InwardBook from './pages/Books/InwardBook';
import Author from './pages/Authors/Author';
import AuthorForm from './pages/Authors/AuthorForm';
import BookSet from './pages/BookSet/BookSet';
import Category from './pages/Category/Category';
import Publisher from './pages/Publisher/Publisher';
import BookDefinition from './pages/BookDefinition/BookDefinition';
import BookDefinitionForm from './pages/BookDefinition/BookDefinitionForm';
import ClassificationSymbol from './pages/ClassificationSymbol/ClassificationSymbol';
import History from './pages/History/History';
import OutwardBook from './pages/Books/OutwardBook';
import InwardBookForm from './pages/Books/InwardBookForm';
import BookList from './pages/Books/BookList';
import NewsArticles from './pages/NewsArticles/NewsArticles';
import NewsArticlesForm from './pages/NewsArticles/NewsArticlesForm';
import InventoryBook from './pages/Books/InventoryBook';
import Dashboard from './pages/Dashboard/Dashboard';
import LibraryInfo from './pages/settings/LibraryInfo';
import LibraryRules from './pages/settings/LibraryRules';
import Holidays from './pages/settings/Holidays';
import GeneralConfig from './pages/settings/GeneralConfig';
import SlideConfig from './pages/settings/SlideConfig';
import UserManagement from './pages/UserManagement/UserManagement';
import UserManagementForm from './pages/UserManagement/UserManagementForm';
import UserGroupManagement from './pages/UserGroupManagement/UserGroupManagement';
import UserGroupForm from './pages/UserGroupManagement/UserGroupForm';

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

                <Route
                    element={
                        <RequireAuth
                            allowedRoles={[
                                ROLES.ManageAuthor,
                                ROLES.ManageNewsArticle,
                                ROLES.ManageUser,
                                ROLES.ManageClassificationSymbol,
                                ROLES.ManageCategory,
                                ROLES.ManageBookSet,
                                ROLES.ManageLog,
                                ROLES.ManageRole,
                                ROLES.ManageBook,
                                ROLES.ManageImportReceipt,
                                ROLES.ManageBookDefinition,
                                ROLES.ManageCategoryGroup,
                                ROLES.ManagePublisher,
                            ]}
                        />
                    }
                >
                    <Route path="admin/" element={<AdminLayout />}>
                        {/* Đường dẫn yêu cầu quyền quản trị */}

                        {/* Trang chủ */}
                        <Route path="home" element={<Dashboard />} />

                        {/* Thiết lập hệ thống */}
                        <Route path="settings">
                            <Route path="library-info" element={<LibraryInfo />} />
                            <Route path="library-rules" element={<LibraryRules />} />
                            <Route path="holidays" element={<Holidays />} />
                            <Route path="general-config" element={<GeneralConfig />} />
                            <Route path="slide-config" element={<SlideConfig />} />
                        </Route>

                        {/* Quản lý người dùng */}
                        <Route path="users">
                            <Route index element={<UserManagement />} />
                            <Route path="new" element={<UserManagementForm />} />
                            <Route path="edit/:id" element={<UserManagementForm />} />
                        </Route>

                        {/* Quản lý nhóm người dùng */}
                        <Route path="user-groups">
                            <Route index element={<UserGroupManagement />} />
                            <Route path="new" element={<UserGroupForm />} />
                            <Route path="edit/:id" element={<UserGroupForm />} />
                        </Route>

                        {/* Sách */}
                        <Route path="books">
                            <Route path="list" element={<BookList />} />

                            <Route path="inward">
                                <Route index element={<InwardBook />} />
                                <Route path="new" element={<InwardBookForm />} />
                                <Route path="edit/:id" element={<InwardBookForm />} />
                            </Route>

                            <Route path="inventory">
                                <Route index element={<InventoryBook />} />
                            </Route>

                            <Route path="outward" element={<OutwardBook />} />
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

                        {/* Biên mục */}
                        <Route path="book-definitions">
                            <Route index element={<BookDefinition />} />
                            <Route path="new" element={<BookDefinitionForm mode="new" />} />
                            <Route path="edit/:id" element={<BookDefinitionForm mode="edit" />} />
                            <Route path="copy/:id" element={<BookDefinitionForm mode="copy" />} />
                        </Route>

                        {/* Kí hiệu phân loại */}
                        <Route path="classifications">
                            <Route index element={<ClassificationSymbol />} />
                        </Route>

                        {/* Tin tức */}
                        <Route path="news-articles">
                            <Route index element={<NewsArticles />} />
                            <Route path="new" element={<NewsArticlesForm />} />
                            <Route path="edit/:id" element={<NewsArticlesForm />} />
                        </Route>

                        {/* Lịch sử */}
                        <Route path="histories">
                            <Route index element={<History />} />
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
