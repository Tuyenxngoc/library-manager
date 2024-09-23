import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Breadcrumb, Layout, Menu, theme } from 'antd';

import { AiFillDashboard } from 'react-icons/ai';
import { IoMdSettings } from 'react-icons/io';
import { FaUsers } from 'react-icons/fa';
import { FaUser } from 'react-icons/fa';

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
    getItem('Thiết lập thệ thống', '2', <IoMdSettings />),
    getItem('Quản lý người dùng', 'sub1', <FaUsers />, [
        getItem('Quẩn lý nhóm', '3'),
        getItem('Quản lý người dùng', '4'),
    ]),
    getItem('Quản lý bạn đọc', 'sub2', <FaUser />, [
        getItem('Thẻ bạn đọc', '6'),
        getItem('Xử lý vi phạm', '7'),
        getItem('Vào ra thư viện', '8'),
    ]),
];

function AdminLayout() {
    const [collapsed, setCollapsed] = useState(false);
    const {
        token: { colorBgContainer, borderRadiusLG },
    } = theme.useToken();

    return (
        <Layout
            style={{
                minHeight: '100vh',
            }}
        >
            {/* Sider */}
            <Sider collapsible width={250} collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                <h2 className="text-white">Library Manager</h2>
                <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline" items={items} />
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
                    >
                        <Breadcrumb.Item>User</Breadcrumb.Item>
                        <Breadcrumb.Item>Bill</Breadcrumb.Item>
                    </Breadcrumb>
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
