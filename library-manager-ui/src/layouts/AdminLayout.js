import React, { useEffect, useState } from 'react';
import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { Breadcrumb, Layout, Menu, theme } from 'antd';

import { AiFillDashboard } from 'react-icons/ai';
import { IoMdSettings } from 'react-icons/io';
import { FaUsers } from 'react-icons/fa';
import { FaUser } from 'react-icons/fa';
import { FaHistory } from 'react-icons/fa';
import { BsNewspaper } from 'react-icons/bs';
import { FaChartBar } from 'react-icons/fa';
import { FaRecycle } from 'react-icons/fa';
import { BiCategory } from 'react-icons/bi';
import { FaBook } from 'react-icons/fa';
import images from '~/assets';

const { Header, Content, Footer, Sider } = Layout;
function getItem(label, key, icon, children) {
    return {
        key,
        icon,
        children,
        label,
    };
}

const items = [
    getItem('Trang chủ', '/admin/home', <AiFillDashboard />),
    getItem('Thiết lập hệ thống', '/admin/settings', <IoMdSettings />, [
        getItem('Thông tin thư viện', '/admin/settings/library-info'),
        getItem('Nội quy thư viện', '/admin/settings/library-rules'),
        getItem('Kì nghỉ ngày lễ', '/admin/settings/holidays'),
        getItem('Cấu hình chung', '/admin/settings/general-config'),
        getItem('Thiết lập Slide', '/admin/settings/slide-config'),
    ]),
    getItem('Quản lý người dùng', '/admin/users', <FaUsers />, [
        getItem('Quản lý nhóm', '/admin/users/groups'),
        getItem('Quản lý người dùng', '/admin/users/manage'),
    ]),
    getItem('Quản lý bạn đọc', '/admin/readers', <FaUser />, [
        getItem('Thẻ bạn đọc', '/admin/readers/cards'),
        getItem('Xử lý vi phạm', '/admin/readers/violations'),
        getItem('Vào ra thư viện', '/admin/readers/access'),
    ]),
    getItem('Quản lý danh mục', '/admin', <BiCategory />, [
        getItem('Biên mục', '/admin/book-definitions'),
        getItem('Loại sách', '/admin/categories'),
        getItem('Bộ sách', '/admin/collections'),
        getItem('Tác giả', '/admin/authors'),
        getItem('Ký hiệu phân loại', '/admin/classifications'),
        getItem('Nhà xuất bản', '/admin/publishers'),
    ]),
    getItem('Quản lý sách', '/admin/books', <FaBook />, [
        getItem('Danh sách sách', '/admin/books/list'),
        getItem('Nhập sách', '/admin/books/inward'),
        getItem('Kiểm kê sách', '/admin/books/inventory'),
        getItem('Xuất sách', '/admin/books/outward'),
    ]),
    getItem('Quản lý lưu thông', '/admin/circulation', <FaRecycle />, [
        getItem('Mượn sách', '/admin/circulation/borrow'),
        getItem('Trả-Gia hạn sách', '/admin/circulation/return-renew'),
    ]),
    getItem('Thống kê báo cáo', '/admin/reports', <FaChartBar />, [getItem('Báo cáo', '/admin/reports/statistics')]),
    getItem('Quản lý tin tức', '/admin/news-articles', <BsNewspaper />),
    getItem('Lịch sử truy cập', '/admin/histories', <FaHistory />),
];

function AdminLayout() {
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();

    const navigate = useNavigate();
    const location = useLocation();
    const [collapsed, setCollapsed] = useState(false);
    const [selectedKey, setSelectedKey] = useState(location.pathname);

    const handleMenuItemClick = ({ key }) => {
        navigate(key);
    };

    useEffect(() => {
        setSelectedKey(location.pathname);
    }, [location.pathname]);

    return (
        <Layout
            style={{
                minHeight: '100vh',
            }}
        >
            {/* Sider */}
            <Sider collapsible width={220} collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                <div className="text-center py-2">
                    <img src={images.logo} alt="logo" width={34} />
                </div>
                <Menu
                    theme="dark"
                    selectedKeys={[selectedKey]}
                    mode="inline"
                    items={items}
                    onClick={handleMenuItemClick}
                />
            </Sider>

            <Layout>
                {/* Header */}
                <Header
                    style={{
                        padding: 0,
                        background: colorBgContainer,
                    }}
                    className="shadow-sm"
                />

                {/* Content */}
                <Content
                    style={{
                        margin: '0 16px',
                    }}
                >
                    <Breadcrumb
                        style={{
                            margin: '16px 0',
                        }}
                        items={[
                            {
                                title: 'Home',
                            },
                            {
                                title: <Link to="">Application Center</Link>,
                            },
                            {
                                title: <Link to="">Application List</Link>,
                            },
                            {
                                title: 'An Application',
                            },
                        ]}
                    />

                    <div
                        style={{
                            padding: 24,
                            minHeight: 360,
                            background: colorBgContainer,
                            borderRadius: borderRadiusLG,
                        }}
                    >
                        <Outlet />
                    </div>
                </Content>

                {/* Footer */}
                <Footer
                    style={{
                        textAlign: 'center',
                    }}
                >
                    {new Date().getFullYear()} All Rights Reserved By © NHÓM 16
                </Footer>
            </Layout>
        </Layout>
    );
}
export default AdminLayout;
