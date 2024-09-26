import React, { useState } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
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
    getItem('Trang chủ', '1', <AiFillDashboard />),
    getItem('Thiết lập hệ thống', '2', <IoMdSettings />),
    getItem('Quản lý người dùng', 'sub1', <FaUsers />, [
        getItem('Quản lý nhóm', 'sub1a'),
        getItem('Quản lý người dùng', 'sub1b'),
    ]),
    getItem('Quản lý bạn đọc', 'sub2', <FaUser />, [
        getItem('Thẻ bạn đọc', 'sub2a'),
        getItem('Xử lý vi phạm', 'sub2b'),
        getItem('Vào ra thư viện', 'sub2c'),
    ]),
    getItem('Quản lý danh mục', 'sub3', <BiCategory />, [
        getItem('Biên mục', 'sub3a'),
        getItem('Loại sách', 'sub3b'),
        getItem('Bộ sách', 'sub3c'),
        getItem('Tác giả', 'author'),
    ]),
    getItem('Quản lý sách', 'sub4', <FaBook />, [
        getItem('Danh sách sách', 'sub4a'),
        getItem('Danh sách sách điện tử', 'sub4b'),
        getItem('Nhập sách', 'Book/Inwardbook'),
        getItem('Kiểm kê sách', 'sub4d'),
        getItem('Xuất sách', 'sub4e'),
    ]),
    getItem('Quản lý lưu thông', 'sub5', <FaRecycle />, [
        getItem('Mượn sách', 'sub5a'),
        getItem('Trả-Gia hạn sách', 'sub5b'),
    ]),
    getItem('Thống kê báo cáo', 'sub6', <FaChartBar />, [getItem('todo', 'sub6a')]),
    getItem('Quản lý tin tức', 'sub7', <BsNewspaper />, [getItem('todo', 'sub7a')]),
    getItem('Lịch sử truy cập', '8', <FaHistory />),
];

function AdminLayout() {
    const [collapsed, setCollapsed] = useState(false);
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();

    const navigate = useNavigate();

    const handleMenuItemClick = ({ key }) => {
        navigate(key);
    };

    return (
        <Layout
            style={{
                minHeight: '100vh',
            }}
        >
            {/* Sider */}
            <Sider collapsible width={220} collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                <div>
                    <img src={images.logo} alt="logo" width={34} />
                </div>
                <Menu
                    theme="dark"
                    defaultSelectedKeys={['1']}
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
                                title: <a href="">Application Center</a>,
                            },
                            {
                                title: <a href="">Application List</a>,
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
